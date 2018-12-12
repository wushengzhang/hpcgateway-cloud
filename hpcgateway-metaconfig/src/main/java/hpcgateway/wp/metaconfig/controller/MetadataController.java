package hpcgateway.wp.metaconfig.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hpcgateway.wp.acl.ACL;
import hpcgateway.wp.acl.DefaultInterceptor;
import hpcgateway.wp.acl.DefaultSessionController;
import hpcgateway.wp.core.page.Errors;
import hpcgateway.wp.core.page.HpcgatewayException;
import hpcgateway.wp.core.page.Pager;
import hpcgateway.wp.core.page.Result;
import hpcgateway.wp.core.page.SelectItem;
import hpcgateway.wp.core.page.SelectItemHelper;
import hpcgateway.wp.core.page.TreeNode;
import hpcgateway.wp.metaconfig.service.MetadataService;
import hpcgateway.wp.orm.beans.CloudGroup;
import hpcgateway.wp.orm.beans.CloudUser;
import hpcgateway.wp.orm.beans.Cluster;
import hpcgateway.wp.orm.beans.ClusterType;
import hpcgateway.wp.orm.beans.ClusterUser;
import hpcgateway.wp.orm.beans.Connect;
import hpcgateway.wp.orm.beans.ConnectType;
import hpcgateway.wp.orm.beans.Interface;
import hpcgateway.wp.orm.beans.Node;
import hpcgateway.wp.orm.beans.NodeConnect;
import hpcgateway.wp.orm.beans.NodeRole;
import hpcgateway.wp.orm.beans.NodeRoleAlloc;
import hpcgateway.wp.orm.beans.ParameterValue;
import hpcgateway.wp.orm.beans.PropertyValue;
import hpcgateway.wp.orm.beans.Rm;
import hpcgateway.wp.orm.beans.Role;
import hpcgateway.wp.orm.beans.TypeHelper;


@Controller
@Scope("request")
@RequestMapping(value = "/metadata")
public class MetadataController
{
	@Resource
	private MetadataService metadataService;
	@Resource
	private DefaultSessionController sessionController;

	private TreeNode createTreeNode(String path,Long id,String name,boolean parent)
	{
		String p = String.format("%s%s%d", path,path.endsWith("/")?"":"/",id);
		TreeNode node = new TreeNode(String.valueOf(id),name,p,parent);		
		return node;
	}
	
	@ACL("metadata")
	@RequestMapping(value="/items",method=RequestMethod.GET)
	public @ResponseBody List<TreeNode> listMetadataItems(
			@RequestParam(value="path",required=true) String path
    ){
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if( path==null || path.isEmpty() ||  path.compareTo("/") == 0 )
		{
			nodes.add(new TreeNode("users","Users","/users"));
			nodes.add(new TreeNode("groups","Groups","/groups"));
			nodes.add(new TreeNode("roles","Roles","/roles"));
			nodes.add(new TreeNode("clusters","Clusters","/clusters"));
			return nodes;
		}
		
		Pattern pattern = Pattern.compile("/([^\\/]+)(/\\d+){0,1}(.*)");
		Matcher matcher = pattern.matcher(path);
		if( !matcher.find() )
		{
			return nodes;
		}
		
		String catalog = matcher.group(1);
		String id = matcher.group(2);
		String residue = matcher.group(3);
		
		try
		{
			if( catalog.compareTo("users") == 0 )
			{
				Long userId = null;
				if( id != null && !id.isEmpty() )
				{
					userId = Long.parseLong(id.substring(1));
				}
				
				if( userId == null )
				{
					List<CloudUser> users = metadataService.queryUsers(null,null,null);
					if( users != null && !users.isEmpty() )
					{
						for(CloudUser user : users)
						{
							nodes.add(createTreeNode(path,user.getId(),user.getName(),false));							
						}
					}
				}
			}
			else if( catalog.compareTo("groups") == 0 )
			{
				Long groupId = null;
				if( residue != null && !residue.isEmpty() )
				{
					int pos = residue.lastIndexOf("/");
					groupId = Long.parseLong(residue.substring(pos+1));
				}
				else if( id != null && !id.isEmpty() )
				{
					groupId = Long.parseLong(id.substring(1));
				}

				List<CloudGroup> groups = metadataService.querySubGroups(groupId);
				if( groups != null && !groups.isEmpty() )
				{
					for(CloudGroup group : groups) {
						nodes.add(createTreeNode(path,group.getId(),group.getName(),true));
					}
				}
			}
			else if( catalog.compareTo("roles") == 0 )
			{
				Long roleId = null;
				if( id != null && !id.isEmpty() )
				{
					roleId = Long.parseLong(id.substring(1));
				}
				
				if( roleId == null )
				{
					List<Role> roles = metadataService.queryRoles(null, null, null);
					if( roles != null && !roles.isEmpty() )
					{
						for(Role role : roles)
						{
							nodes.add(createTreeNode(path,role.getId(),role.getName(),false));									
						}
					}
				}
			}
			else if( catalog.compareTo("clusters") == 0 )
			{
				Long clusterId = null;
				if( id != null && !id.isEmpty() )
				{
					clusterId = Long.parseLong(id.substring(1));
				}
				if( clusterId == null )
				{
					List<Cluster> clusters = metadataService.queryClusters(null,null,null);
					if( clusters != null && !clusters.isEmpty() )
					{
						for(Cluster cluster : clusters)
						{
							nodes.add(createTreeNode(path,cluster.getId(),cluster.getName(),true));
						}
					}
				}
				else if( residue == null || residue.isEmpty() )
				{
					nodes.add(new TreeNode("ClusterUsers","ClusterUsers",String.format("%s/users", path)));
					nodes.add(new TreeNode("Hosts","Hosts",String.format("%s/hosts", path)));
					nodes.add(new TreeNode("RMs","Resource Managers",String.format("%s/rms",path)));
					nodes.add(new TreeNode("Resources","Resources",String.format("%s/reses", path)));
				}
				else if( residue.compareTo("/users") == 0 )
				{
					List<ClusterUser> clusterUsers = metadataService.queryClusterUsers(clusterId,null,null,null);
					if( clusterUsers != null )
					{
						for(ClusterUser clusterUser : clusterUsers)
						{
							nodes.add(createTreeNode(path,clusterUser.getId(),clusterUser.getName(),false));
						}
					}
				}
				else if( residue.compareTo("/rms") == 0 )
				{
					List<Rm> rms = metadataService.queryRms(clusterId,null,null,null);
					if( rms != null )
					{
						for(Rm rm : rms)
						{
							String title = String.format("%s-%s", rm.getName(),rm.getVersion());
							nodes.add(createTreeNode(path,rm.getId(),title,false));
						}
					}
				}
				else if( residue.compareTo("/hosts") == 0 )
				{
					List<Node> ns = metadataService.queryNodes(clusterId,null,null,null,null);
					if( ns != null )
					{
						for(Node n : ns)
						{
							nodes.add(createTreeNode(path,n.getId(),n.getName(),false));
						}
					}
				}
				else if( residue.compareTo("/reses") == 0 )
				{
					List<hpcgateway.wp.orm.beans.Resource> rs = metadataService.queryResources(clusterId,null,null,null);
					if( rs != null )
					{
						for(hpcgateway.wp.orm.beans.Resource r : rs)
						{
							nodes.add(createTreeNode(path,r.getId(),r.getType(),false));
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return nodes;
	}

	@RequestMapping(value="/clustertypes",method=RequestMethod.GET)
	public @ResponseBody List<SelectItem> queryClusterTypes()
	{
		List<SelectItem> items = null;
		try
		{
			items = SelectItemHelper.getSelectItems(ClusterType.class,"name","code","desc");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			items = new ArrayList<SelectItem>();
		}
		return items;
	}
	
	@RequestMapping(value="/noderoles",method=RequestMethod.GET)
	public @ResponseBody List<SelectItem> queryAllNodeRoles()
	{
		List<SelectItem> list = null;
		
		try
		{
			list = SelectItemHelper.getSelectItems(NodeRole.class, "name", "code", "desc");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			list = new ArrayList<SelectItem>();
		}
		return list;		
		
	}
	
	@RequestMapping(value="/roles/all",method=RequestMethod.GET)
	public @ResponseBody Result queryAllRoles()
	{
		Result result = new Result();
		try
		{
			List<Role> list = metadataService.queryRoles(null, null, null);
			result.setData(list);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/roles",method=RequestMethod.GET)
	public @ResponseBody Result queryRoles(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	){
		Result result = new Result();
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countRoles(name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<Role> list = null;
			if( count == 0 )
			{
				list = new ArrayList<Role>();
			}
			else
			{
				list = metadataService.queryRoles(name, pager.getPageNo(), pager.getPageSize());
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
			
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/role",method=RequestMethod.POST)
	public @ResponseBody Result createRole(
			@RequestBody Role role
	) {
		Result result = new Result();
		if( role.getName() == null || role.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's name is required");
			return result;
		}
		if( role.getCode() == 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's code is required.");
			return result;
		}
		
		role.setId(null);
		try
		{
			metadataService.saveRole(role);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/role/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchRole(
			@PathVariable("id") Long roleId
	){
		Result result = new Result();
		if( roleId == null || roleId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's ID is required.");
			return result;
		}
		try
		{
			Role role = metadataService.fetchRole(roleId);
			result.setData(role);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/role/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyRole(
			@PathVariable("id") Long roleId,
			@RequestBody Role role
	) {
		Result result = new Result();
		if( roleId == null || roleId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's ID is required.");
			return result;
		}
		if( role.getName() == null || role.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's name is required");
			return result;
		}
		if( role.getCode() == 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's code is required.");
			return result;
		}
		
		role.setId(roleId);
		try
		{
			metadataService.saveRole(role);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;		
	}
	
	@RequestMapping(value="/role/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result removeRole(
			@PathVariable("id") Long roleId
	){
		Result result = new Result();
		if( roleId == null || roleId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The role's ID is required.");
			return result;
		}
		
		try
		{
			metadataService.removeRole(roleId);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	
	@RequestMapping(value="/groups/{type}",method=RequestMethod.GET)
	public @ResponseBody Result queryGroups(
			@PathVariable("type") String type
	) {
		if( type == null || type.isEmpty() )
		{
			type = "tree";
		}
		Result result = new Result();
		
		try
		{
			List<CloudGroup> groups = null;
			if( type.compareTo("tree") == 0 )
			{
				groups = metadataService.queryGroupsForTree();
			}
			result.setData(groups);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());			
		}
		return result;
	}
	
	@RequestMapping(value="/groups",method=RequestMethod.GET)
	public @ResponseBody Result queryGroups(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	) {
		Result result = new Result();
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countGroups(name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<CloudGroup> list = null;
			if( count == 0 )
			{
				list = new ArrayList<CloudGroup>();
			}
			else
			{
				list = metadataService.queryGroups(name, pager.getPageNo(), pager.getPageSize());
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}	
	
	public static class GroupParam
	{
		private Long parentId;
		private String name;
		private String code;
		private String desc;
		private Long[] users;
		public Long getParentId()
		{
			return parentId;
		}
		public void setParentId(Long parentId)
		{
			this.parentId = parentId;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getCode()
		{
			return code;
		}
		public void setCode(String code)
		{
			this.code = code;
		}
		public String getDesc()
		{
			return desc;
		}
		public void setDesc(String desc)
		{
			this.desc = desc;
		}
		public Long[] getUsers()
		{
			return users;
		}
		public void setUsers(Long[] users)
		{
			this.users = users;
		}
	}
	
	private void saveGroup(Long gid,GroupParam param) throws Exception
	{
		CloudGroup group = new CloudGroup();
		group.setId(gid);
		group.setName(param.getName());
		group.setCode(param.getCode());
		group.setDesc(param.getDesc());
		group.setParentId(param.getParentId());
		Long[] userIds = param.getUsers();
		if( userIds != null && userIds.length > 0 )
		{
			List<CloudUser> users = new ArrayList<CloudUser>();
			for(Long userId : userIds) {
				CloudUser u = new CloudUser();
				u.setId(userId);
				users.add(u);
			}
			group.setUsers(users);
		}
		
		metadataService.saveGroup(group);
	}
	
	@RequestMapping(value="/group",method=RequestMethod.POST)
	public @ResponseBody Result createGroup(
			@RequestBody GroupParam param
	){
		Result result = new Result();
		
		try
		{
			saveGroup(null,param);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/group/{id}",method=RequestMethod.POST)
	public @ResponseBody Result createGroup(
			@PathVariable("id") Long id,
			@RequestBody GroupParam param
	){
		Result result = new Result();
		
		try
		{
			saveGroup(id,param);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/group/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchGroup(
			@PathVariable("id") Long id
	) {
		Result result = new Result();
		if( id == null || id <= 0 ) {
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("invalid group Id");
			return result;
		}
		
		try
		{
			CloudGroup group = metadataService.fetchGroup(id);
			result.setData(group);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/group/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result removeGroup(
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( id == null || id <= 0 ) {
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("invalid group Id");
			return result;
		}
		
		try
		{
			metadataService.removeGroup(id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}

	@RequestMapping(value="/users/{type}",method=RequestMethod.GET)
	public @ResponseBody Result queryUsers(
			@PathVariable("type") String type
	){
		if( type == null || type.isEmpty() )
		{
			type = "pay";
		}
		Result result = new Result();
		try
		{
			List<CloudUser> list = null;
			if( type.compareTo("all") == 0 )
			{
				list = metadataService.queryUsers(null, null, null);
			}
			else if( type.compareTo("pay") == 0 )
			{
				list = metadataService.queryUsers(null, null, null);
			}
			for(CloudUser u : list) {
				u.setPassword("******");
			}
			result.setData(list);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public @ResponseBody Result queryUsers(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	) {
		Result result = new Result();
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countUsers(name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<CloudUser> list = null;
			if( count == 0 )
			{
				list = new ArrayList<CloudUser>();
			}
			else
			{
				list = metadataService.queryUsers(name, pager.getPageNo(), pager.getPageSize());
			}
			for(CloudUser u :list) {
				u.setPassword("******");
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	public static class Setpwd {
		private String password1;
		private String password2;
		public String getPassword1()
		{
			return password1;
		}
		public void setPassword1(String password1)
		{
			this.password1 = password1;
		}
		public String getPassword2()
		{
			return password2;
		}
		public void setPassword2(String password2)
		{
			this.password2 = password2;
		}
		
	}
	@RequestMapping(value="/user/{id}/pwd",method=RequestMethod.POST)
	public @ResponseBody Result setpwd(
			@PathVariable("id") Long id,
			@RequestBody Setpwd setpwd
	){
		Result result = new Result();
		String pwd1 = setpwd.getPassword1();
		if( pwd1 == null ) pwd1 = "";
		String pwd2 = setpwd.getPassword2();
		if( pwd2 == null ) pwd2 = "";
		if( pwd1.compareTo(pwd2) != 0 ) {
			result.setCode(Errors.CODE_PARAM);
			result.setMessage("password dismatch");
			return result;
		}
		try
		{
			metadataService.setpwd(id,pwd1);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public @ResponseBody Result createUser(
			@RequestBody UserParam param
	) {
		Result result = new Result();
		try
		{
			saveUser(null,param);
		}
		catch(HpcgatewayException ex)
		{
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchUser(
			@PathVariable("id") Long userId
	){
		Result result = new Result();
		if( userId == null || userId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The user's ID is required.");
			return result;
		}
		try
		{
			CloudUser user = metadataService.fetchUser(userId);
			user.setPassword("******");
			List<CloudGroup> groups = metadataService.queryGroups(userId);
			user.setGroups(groups);
			result.setData(user);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	

	
	static public class UserParam
	{
		private String name;
		private Long askTo;
		private Long[] groupIds;
		
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public Long getAskTo()
		{
			return askTo;
		}
		public void setAskTo(Long askTo)
		{
			this.askTo = askTo;
		}
		public Long[] getGroupIds()
		{
			return groupIds;
		}
		public void setGroupIds(Long[] groupIds)
		{
			this.groupIds = groupIds;
		}			
	}
	
	public void saveUser(Long id,UserParam param) throws HpcgatewayException
	{
		//
		CloudUser user = new CloudUser();

		//
		user.setId(id);
		
		// setup initializing password for new user
		if( id == null ) {
			user.setPassword("123456");
		}

		// check user's name
		String name = param.getName();
		if( name == null || name.isEmpty() )
		{
			throw new HpcgatewayException(Errors.CODE_PARAM,"the user's name is required.");
		}
		user.setName(name);

		// paying user
		user.setAskTo(param.getAskTo());
		
		// group allocations
		Long[] groupIds = param.getGroupIds();
		if( groupIds != null && groupIds.length > 0 )
		{
			List<CloudGroup> groups = new ArrayList<CloudGroup>();
			for(Long groupId : groupIds)
			{
				CloudGroup group = new CloudGroup();
				group.setId(groupId);
				groups.add(group);
			}
			user.setGroups(groups);
		}

		try
		{
			metadataService.saveUser(user);
		}
		catch(Exception ex)
		{
			throw new HpcgatewayException(Errors.CODE_RUN,ex.getMessage());
		}
	}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyUser(
			@PathVariable("id") Long userId,
			@RequestBody UserParam param
	) {
		Result result = new Result();
		if( userId == null || userId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The user's ID is required.");
			return result;
		}
		
		try
		{
			saveUser(userId,param);
		}
		catch(HpcgatewayException ex)
		{
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		}
		return result;		
	}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result removeUser(
			@PathVariable("id") Long userId
	){
		Result result = new Result();
		if( userId == null || userId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The user's ID is required.");
			return result;
		}
		
		try
		{
			metadataService.removeUser(userId);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	
	@RequestMapping(value="/connectTypes",method=RequestMethod.GET)
	public @ResponseBody List<SelectItem> queryAllConnectTypes()
	{
		List<SelectItem> list = null;
		
		try
		{
			list = SelectItemHelper.getSelectItems(ConnectType.class, "name", "code", "desc");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			list = new ArrayList<SelectItem>();
		}
		return list;		
		
	}
	
	@RequestMapping(value="/cluster/{id}/bootstrapers",method=RequestMethod.GET)
	public @ResponseBody Result queryBootstraperConnections(
			@PathVariable("id") Long clusterId
	){
		Result result = new Result();
		
		try
		{
			List<NodeConnect> connects = metadataService.queryConnections(clusterId,ConnectType.HTTP,NodeRole.Bootstraper);
			result.setData(connects);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/clusters/all",method=RequestMethod.GET)
	public @ResponseBody List<Cluster> queryAllClusters()
	{
		List<Cluster> list = null;
		try
		{
			list = metadataService.queryClusters(null,null,null);
		}
		catch(Exception ex)
		{
			list = new ArrayList<Cluster>();
		}
		return list;
	}
	
	@RequestMapping(value="/clusters",method=RequestMethod.GET)
	public @ResponseBody Result queryClusters(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	) {
		Result result = new Result();
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countClusters(name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<Cluster> list = null;
			if( count == 0 )
			{
				list = new ArrayList<Cluster>();
			}
			else
			{
				list = metadataService.queryClusters(name, pager.getPageNo(), pager.getPageSize());
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_DATABASE);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/cluster",method=RequestMethod.POST)
	public @ResponseBody Result createCluster(
			@RequestBody Cluster metaCluster
	){
		Result result = new Result();
		if( metaCluster.getName() == null || metaCluster.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Cluster's name is required");
			return result;
		}
		if( metaCluster.getUsername() == null )
		{ 
			metaCluster.setUsername("");
		}
		if( metaCluster.getPassword() == null )
		{ 
			metaCluster.setPassword("");
		}
		if( metaCluster.getIdentifier() == null )
		{
			metaCluster.setIdentifier("");
		}
		
		// set the cluster's ID to null, creating a new cluster.
		
		metaCluster.setId(null);
		try
		{
			metadataService.saveCluster(metaCluster);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchCluster(
			@PathVariable("id") Long id
	) {
		Result result = new Result();
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("invalid cluster's ID");
			return result;
		}
		
		try
		{
			Cluster metaCluster = metadataService.fetchCluster(id);
			result.setData(metaCluster);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyCluster(
			@PathVariable("id") Long id,
			@RequestBody Cluster metaCluster
	) {
		Result result = new Result();
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster's ID is required");
			return result;
		}
		if( metaCluster.getName() == null || metaCluster.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster's name is required.");
		}
		if( metaCluster.getUsername() == null )
		{ 
			metaCluster.setUsername("");
		}
		if( metaCluster.getPassword() == null )
		{ 
			metaCluster.setPassword("");
		}
		if( metaCluster.getIdentifier() == null )
		{
			metaCluster.setIdentifier("");
		}
		
		// set Cluster's ID for modifying 
		metaCluster.setId(id);
		try
		{
			metadataService.saveCluster(metaCluster);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result removeCluster(
			@PathVariable("id") Long id
	) {
		Result result = new Result();
		
		try
		{
			metadataService.removeCluster(id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/nodes/all",method=RequestMethod.GET)
	public @ResponseBody Result queryAllNodes(
			@PathVariable("clusterId") Long clusterId
	) {
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster ID is required.");
			return result;
		}
		
		try
		{
			List<Node> nodes = metadataService.queryNodes(clusterId, null, null, null, null);
			result.setData(nodes);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/cluster/{clusterId}/nodes",method=RequestMethod.GET)
	public @ResponseBody Result queryNodes(
			@PathVariable("clusterId") Long clusterId,
			@RequestParam(value="role",required=false) Integer code,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster ID is required.");
			return result;
		}
		
		NodeRole role = null;
		if( code != null )
		{
			try
			{
				role = TypeHelper.findType(NodeRole.class, code);
			}
			catch(Exception ex)
			{
				result.setCode(Errors.CODE_INVLD_PARAM);
				result.setMessage("Invalid Node role's code.");
				return result;
			}
		}
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countNodes(clusterId,role,name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<Node> list = null;
			if( count == 0 )
			{
				list = new ArrayList<Node>();
			}
			else
			{
				list = metadataService.queryNodes(clusterId, role, name, pager.getPageNo(), pager.getPageSize());
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	

	@RequestMapping(value="/cluster/{clusterId}/node/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchNode(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster ID is required");
			return result;
		}
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The node ID is required");
			return result;
		}
		
		try
		{
			Node metaNode = metadataService.fetchNode(clusterId,id);
			result.setData(metaNode);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
		}
		return result;
	}
	
	
	public static class NodeParam
	{
		private String name;
		private String aliasname;
		private Interface[] ifcs;
		private Connect[] connects;
		private PropertyValue[] properties;
		private ParameterValue[] parameters;
		private Integer[] roleCodes;
		
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getAliasname()
		{
			return aliasname;
		}
		public void setAliasname(String aliasname)
		{
			this.aliasname = aliasname;
		}
		public Interface[] getIfcs()
		{
			return ifcs;
		}
		public void setIfcs(Interface[] ifcs)
		{
			this.ifcs = ifcs;
		}
		public Connect[] getConnects()
		{
			return connects;
		}
		public void setConnects(Connect[] connects)
		{
			this.connects = connects;
		}
		public Integer[] getRoleCodes()
		{
			return roleCodes;
		}
		public void setRoleCodes(Integer[] roleCodes)
		{
			this.roleCodes = roleCodes;
		}
		public PropertyValue[] getProperties()
		{
			return properties;
		}
		public void setProperties(PropertyValue[] properties)
		{
			this.properties = properties;
		}
		public ParameterValue[] getParameters()
		{
			return parameters;
		}
		public void setParameters(ParameterValue[] parameters)
		{
			this.parameters = parameters;
		}	
	}
	
	private Result saveNode(Result result,Long clusterId,Long nodeId,NodeParam param)
	{
		if( result == null )
		{
			result = new Result();
		}
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster's ID is required");
			return result;
		}
		if( param.getName()==null || param.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_PARAM);
			result.setMessage("The node's name is required");
			return result;
		}
		if( param.getAliasname() == null )
		{
			param.setAliasname("");
		}
		
		Node node = new Node();
		node.setId(nodeId);
		node.setName(param.getName());
		node.setAliasname(param.getAliasname());
		
		Interface[] ifcs = param.getIfcs();
		if( ifcs != null && ifcs.length > 0 )
		{
			for(Interface ifc : ifcs)
			{
				if( ifc.getName() == null || ifc.getName().isEmpty() )
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Interface's name is required");
					return result;
				}
				if( ifc.getAddr() == null || ifc.getAddr().isEmpty() )
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Interface's address is required.");
					return result;
				}
				if( ifc.getAlias() == null )
				{
					ifc.setAlias("");
				}
				node.addIfc(ifc);
			}
		}
		
		Connect[] connects = param.getConnects();
		if( connects != null && connects.length > 0 )
		{
			for(Connect connect : connects)
			{
				if( connect.getUrl() == null || connect.getUrl().isEmpty() )
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Connect's url is required.");
					return result;
				}
				if( connect.getType() == null )
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Connect's type is required.");
					return result;
				}
				node.addConnect(connect);
			}
		}

		PropertyValue[] properties = param.getProperties();
		if( properties != null && properties.length > 0 )
		{
			for(PropertyValue pa : properties)
			{
				if( pa.getName() == null || pa.getName().isEmpty() )
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Property's name is required");
					return result;
				}
				if( pa.getValue() == null )
				{
					pa.setValue("");
				}
				pa.setTime(new Date());
				node.addProperty(pa);
			}
		}
		
		ParameterValue[] parameters = param.getParameters();
		if( parameters != null && parameters.length > 0 )
		{
			for(ParameterValue pa : parameters)
			{
				if( pa.getName() == null || pa.getName().isEmpty() )
				{
					result.setCode(Errors.CODE_PARAM);
					result.setMessage("Parameter's name is required");
					return result;
				}
				if( pa.getValue() == null )
				{
					pa.setValue("");
				}
				pa.setTime(new Date());
				node.addParameter(pa);
			}
		}
		
		Integer[] roleCodes = param.getRoleCodes();
		if( roleCodes != null && roleCodes.length > 0 )
		{
			for(Integer roleCode : roleCodes)
			{
				NodeRoleAlloc roleAlloc = new NodeRoleAlloc();
				try
				{
					roleAlloc.setRoleCode(roleCode);
				}
				catch(Exception ex)
				{
					result.setCode(Errors.CODE_INVLD_PARAM);
					result.setMessage("Invalid role code "+roleCode);
					return result;
				}
				node.addRole(roleAlloc);
			}
		}
		
		try
		{
			metadataService.saveNode(clusterId, node);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		
		return result;		
	}
	
	@RequestMapping(value="/cluster/{clusterId}/node",method=RequestMethod.POST)
	public @ResponseBody Result createNode(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody NodeParam param
	){
		return saveNode(null,clusterId,null,param);
	}
	
	@RequestMapping(value="/cluster/{clusterId}/node/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyNode(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id,
			@RequestBody NodeParam param
	){
		Result result = new Result();
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid node's ID");
			return result;
		}
		
		return saveNode(result,clusterId,id,param);
	}
	
	@RequestMapping(value="/cluster/{clusterId}/node/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result deleteNode(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The cluster's ID is required");
			return result;
		}
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The node's ID is required.");
			return result;
		}
		
		try
		{
			metadataService.removeNode(clusterId,id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;		
	}
	
	static public class ClusterUserParam
	{
		private String name;
		private String password;
		private String idrsa;
		private Long userId;
		

		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
		public String getIdrsa()
		{
			return idrsa;
		}
		public void setIdrsa(String idrsa)
		{
			this.idrsa = idrsa;
		}
		public Long getUserId()
		{
			return userId;
		}
		public void setUserId(Long userId)
		{
			this.userId = userId;
		}		
		
		
	}
	
	@RequestMapping(value="/cluster/{clusterId}/users",method=RequestMethod.GET)
	public @ResponseBody Result queryClusterUsers(
			@PathVariable("clusterId") Long clusterId,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	){
		Result result = new Result();
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countClusterUsers(clusterId, name);
			Pager pager = new Pager(count,pageNo,pageSize);
			List<ClusterUser> list = null;
			if( count == 0 )
			{
				list = new ArrayList<ClusterUser>();
			}
			else
			{
				list = metadataService.queryClusterUsers(clusterId, name, pageNo, pageSize);
			}
			for(ClusterUser u:list) {
				u.setPassword("******");
			}
			map.put("pager", pager);
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/cluster/{clusterId}/user/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchClusterUser(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		
		try
		{
			ClusterUser clusterUser = metadataService.fetchClusterUser(clusterId, id);
			clusterUser.setPassword("******");
			result.setData(clusterUser);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	private Result saveClusterUser(Result result,Long clusterId,Long clusterUserId,ClusterUserParam param)
	{
		if( result == null )
		{
			result = new Result();
		}

		if( param.getUserId() == null || param.getUserId() <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("User's ID is required");
			return result;
		}
		
		if( param.getName() == null || param.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_PARAM);
			result.setMessage("User's name is required");
			return result;
		}
	
		ClusterUser clusterUser = new ClusterUser();
		clusterUser.setId(clusterUserId);
		clusterUser.setUserId(param.getUserId());
		clusterUser.setName(param.getName());
		clusterUser.setPassword(param.getPassword());
		clusterUser.setIdrsa(param.getIdrsa());
		
		try
		{
			metadataService.saveClusterUser(clusterId, clusterUser);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	
	@RequestMapping(value="/cluster/{clusterId}/user",method=RequestMethod.POST)
	public @ResponseBody Result createClusterUser(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody ClusterUserParam param
	){
		return saveClusterUser(null,clusterId,null,param);
	}
	
	@RequestMapping(value="/cluster/{clusterId}/user/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyUser(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id,
			@RequestBody ClusterUserParam param
	){
		Result result = new Result();
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Cluster user's ID is required.");
			return result;
		}
		return saveClusterUser(result,clusterId,id,param);
	}
	
	@RequestMapping(value="/cluster/{clusterId}/user/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result deleteClusterUser(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		try
		{
			metadataService.removeClusterClusterUser(clusterId, id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;		
	}

	@RequestMapping(value="/cluster/{clusterId}/rms",method=RequestMethod.GET)
	public @ResponseBody Result queryRms(
			@PathVariable("clusterId") Long clusterId,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("invalid cluster's ID");
			return result;
		}
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countRms(clusterId,name);
			Pager pager = new Pager(count,pageNo,pageSize);
			map.put("pager", pager);
			if( count == 0 )
			{
				map.put("list", new ArrayList<Rm>());
			}
			else
			{
				List<Rm> list = metadataService.queryRms(clusterId, name, pager.getPageNo(), pager.getPageSize());
				map.put("list", list);
			}
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/cluster/{clusterId}/rm/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchRm(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		if( id == null || id <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid RM's ID");
			return result;
		}
		
		try
		{
			Rm rm = metadataService.fetchRm(clusterId, id);
			result.setData(rm);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	static public class RmParam 
	{
		private Long id;
		private Long installer;
		private Long controller;
		private Long account;
		private Long storage;
		private Long schedule;
		private String name;
		private String version;
		private String topdir;
		private String bin;
		private String sbin;
		private String lib;
		private String etc;
		public Long getId()
		{
			return id;
		}
		public void setId(Long id)
		{
			this.id = id;
		}
		
		public Long getInstaller()
		{
			return installer;
		}
		public void setInstaller(Long installer)
		{
			this.installer = installer;
		}
		public Long getController()
		{
			return controller;
		}
		public void setController(Long controller)
		{
			this.controller = controller;
		}
		public Long getAccount()
		{
			return account;
		}
		public void setAccount(Long account)
		{
			this.account = account;
		}
		public Long getStorage()
		{
			return storage;
		}
		public void setStorage(Long storage)
		{
			this.storage = storage;
		}
		public Long getSchedule()
		{
			return schedule;
		}
		public void setSchedule(Long schedule)
		{
			this.schedule = schedule;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getVersion()
		{
			return version;
		}
		public void setVersion(String version)
		{
			this.version = version;
		}
		public String getTopdir()
		{
			return topdir;
		}
		public void setTopdir(String topdir)
		{
			this.topdir = topdir;
		}
		public String getBin()
		{
			return bin;
		}
		public void setBin(String bin)
		{
			this.bin = bin;
		}
		public String getSbin()
		{
			return sbin;
		}
		public void setSbin(String sbin)
		{
			this.sbin = sbin;
		}
		public String getLib()
		{
			return lib;
		}
		public void setLib(String lib)
		{
			this.lib = lib;
		}
		public String getEtc()
		{
			return etc;
		}
		public void setEtc(String etc)
		{
			this.etc = etc;
		}
	}
	
	@RequestMapping(value="/cluster/{clusterId}/rm",method=RequestMethod.POST)
	public @ResponseBody Result createRm(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody RmParam param
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		if( param.getName() == null || param.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The RM's name is required.");
			return result;
		}
		if( param.getVersion() == null || param.getVersion().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The RM's version is required");
			return result;
		}
		
		Rm rm = new Rm();
		rm.setId(null);
		rm.setName(param.getName());
		rm.setVersion(param.getVersion());
		rm.setTopdir(param.getTopdir());
		rm.setBin(param.getBin());
		rm.setSbin(param.getSbin());
		rm.setLib(param.getLib());
		rm.setEtc(param.getEtc());
		if( param.getInstaller() != null && param.getInstaller() > 0 ) {
			Node node = new Node();
			node.setId(param.getInstaller());
			rm.setInstaller(node);
		}
		if( param.getController() != null && param.getController() > 0 )
		{
			Node node = new Node();
			node.setId(param.getController());
			rm.setController(node);
		}
		if( param.getAccount() != null && param.getAccount() > 0 )
		{
			Node node = new Node();
			node.setId(param.getAccount());
			rm.setAccount(node);
		}
		if( param.getSchedule() != null && param.getSchedule() > 0 )
		{
			Node node = new Node();
			node.setId(param.getSchedule());
			rm.setSchedule(node);
		}
		if( param.getStorage() != null && param.getStorage() > 0 )
		{
			Node node = new Node();
			node.setId(param.getStorage());
			rm.setStorage(node);
		}
	
		try
		{
			metadataService.saveRm(clusterId, rm);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/rm/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyRm(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id,
			@RequestBody RmParam param
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		if( param.getName() == null || param.getName().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The RM's name is required.");
			return result;
		}
		if( param.getVersion() == null || param.getVersion().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The RM's version is required");
			return result;
		}
		
		Rm rm = new Rm();
		rm.setId(id);
		rm.setName(param.getName());
		rm.setVersion(param.getVersion());
		rm.setTopdir(param.getTopdir());
		rm.setBin(param.getBin());
		rm.setSbin(param.getSbin());
		rm.setLib(param.getLib());
		rm.setEtc(param.getEtc());
		if( param.getInstaller() != null && param.getInstaller() > 0 ) {
			Node node = new Node();
			node.setId(param.getInstaller());
			rm.setInstaller(node);
		}
		if( param.getController() != null && param.getController() > 0 )
		{
			Node node = new Node();
			node.setId(param.getController());
			rm.setController(node);
		}
		if( param.getAccount() != null && param.getAccount() > 0 )
		{
			Node node = new Node();
			node.setId(param.getAccount());
			rm.setAccount(node);
		}
		if( param.getSchedule() != null && param.getSchedule() > 0 )
		{
			Node node = new Node();
			node.setId(param.getSchedule());
			rm.setSchedule(node);
		}
		if( param.getStorage() != null && param.getStorage() > 0 )
		{
			Node node = new Node();
			node.setId(param.getStorage());
			rm.setStorage(node);
		}
	
		
		try
		{
			metadataService.saveRm(clusterId, rm);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/rm/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result deleteRm(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}

		try
		{
			metadataService.removeRm(clusterId, id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;		
	}
	
	@RequestMapping(value="/cluster/{clusterId}/reses",method=RequestMethod.GET)
	public @ResponseBody Result queryReses(
			@PathVariable("clusterId") Long clusterId,
			@RequestParam(value="type",required=false) String type,
			@RequestParam(value="no",required=true) Integer pageNo,
			@RequestParam(value="size",required=true) Integer pageSize
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			int count = metadataService.countResources(clusterId, type);
			Pager pager = new Pager(count,pageNo,pageSize);
			map.put("pager", pager);
			List<hpcgateway.wp.orm.beans.Resource> list = null;
			if( count == 0 )
			{
				list = new ArrayList<hpcgateway.wp.orm.beans.Resource>();
			}
			else
			{
				list = metadataService.queryResources(clusterId, type, pager.getPageNo(), pager.getPageSize());
			}
			map.put("list", list);
			result.setData(map);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	

	@RequestMapping(value="/cluster/{clusterId}/res/{id}",method=RequestMethod.GET)
	public @ResponseBody Result fetchRes(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		
		try
		{
			hpcgateway.wp.orm.beans.Resource resource = metadataService.fetchResource(clusterId, id);
			result.setData(resource);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/res",method=RequestMethod.POST)
	public @ResponseBody Result createRes(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody hpcgateway.wp.orm.beans.Resource res
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		if( res.getType() == null || res.getType().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The resource's type is requeired");
			return result;
		}
		
		res.setId(null);
		
		try
		{
			metadataService.saveResource(clusterId, res);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/res/{id}",method=RequestMethod.POST)
	public @ResponseBody Result modifyRes(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id,
			@RequestBody hpcgateway.wp.orm.beans.Resource res
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		if( res.getType() == null || res.getType().isEmpty() )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("The resource's type is requeired");
			return result;
		}
		
		res.setId(id);
		
		try
		{
			metadataService.saveResource(clusterId, res);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value="/cluster/{clusterId}/res/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Result deleteRes(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("id") Long id
	){
		Result result = new Result();
		if( clusterId == null || clusterId <= 0 )
		{
			result.setCode(Errors.CODE_INVLD_PARAM);
			result.setMessage("Invalid cluster's ID");
			return result;
		}
		
		try
		{
			metadataService.removeResource(clusterId, id);
		}
		catch(Exception ex)
		{
			result.setCode(Errors.CODE_RUN);
			result.setMessage(ex.getMessage());
		}
		return result;		
	}
}
