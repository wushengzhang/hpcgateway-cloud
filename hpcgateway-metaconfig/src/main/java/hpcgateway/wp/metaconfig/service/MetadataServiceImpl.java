package hpcgateway.wp.metaconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import hpcgateway.wp.core.page.TreeNode;
import hpcgateway.wp.core.page.TreeNodeDefine;
import hpcgateway.wp.metaconfig.dao.hibernate.HpcgatewayDao;
import hpcgateway.wp.orm.beans.CloudGroup;
import hpcgateway.wp.orm.beans.CloudUser;
import hpcgateway.wp.orm.beans.Cluster;
import hpcgateway.wp.orm.beans.ClusterUser;
import hpcgateway.wp.orm.beans.Connect;
import hpcgateway.wp.orm.beans.ConnectType;
import hpcgateway.wp.orm.beans.Node;
import hpcgateway.wp.orm.beans.NodeConnect;
import hpcgateway.wp.orm.beans.NodeRole;
import hpcgateway.wp.orm.beans.ParameterValue;
import hpcgateway.wp.orm.beans.PasswordHelper;
import hpcgateway.wp.orm.beans.PropertyValue;
import hpcgateway.wp.orm.beans.Rm;
import hpcgateway.wp.orm.beans.Role;

@Service("metadataService")
public class MetadataServiceImpl implements MetadataService
{	
	@Resource
	protected HpcgatewayDao hpcgatewayDao;
	
	final static TreeNodeDefine[] nodesUnderOneCluster = new TreeNodeDefine[] {
			new TreeNodeDefine("rms","ResourceManagers","/%d/rms",true),
			new TreeNodeDefine("res","Resources","/%d/resources",true),
			new TreeNodeDefine("roles","Roles","/%d/roles",true),
			new TreeNodeDefine("nodes","Nodes","/%d/nodes",true),
			new TreeNodeDefine("properties","Properties","/%d/properties",true),
			new TreeNodeDefine("parameters","Parameters","/%d/parameters",true),
			new TreeNodeDefine("connects","Connects","/%d/connects",true)
		};
	
	@Override
	public List<TreeNode> queryTreeNode(String path) throws Exception
	{
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		if( path==null || path.isEmpty() || path.compareTo("/") == 0 )
		{
			List<Cluster> clusters = hpcgatewayDao.queryClusters(null, null, null);
			for( Cluster cluster : clusters )
			{
				String clusterPath = String.format("/%s", cluster.getId());
				TreeNode node = new TreeNode(cluster.getId(),cluster.getName(),clusterPath,true);
				node.setLevel(1);
				treeNodes.add(node);
			}
			return treeNodes;
		}

		Pattern pattern = Pattern.compile("^/(\\d+)(/[^/]+){0,1}");
		Matcher matcher = pattern.matcher(path);
		if( matcher == null || !matcher.find() )
		{
			return treeNodes;
		}
		
		Long clusterId = Long.parseLong(matcher.group(1));
		String residue = matcher.group(2);
		if( residue == null || residue.isEmpty() )
		{
			for(TreeNodeDefine d : nodesUnderOneCluster)
			{
				TreeNode treeNode = new TreeNode();
				treeNode.setIdentifier(d.getIdentifier());
				treeNode.setName(d.getName());
				treeNode.setIsParent(d.isParent());
				treeNode.setPath(d.getPath(clusterId));
				treeNodes.add(treeNode);
			}
		}
		else if( residue.startsWith("/rms") )
		{
			List<Rm> rms = hpcgatewayDao.queryRms(clusterId, null, null, null);
			if( rms != null && !rms.isEmpty() )
			{
				for(Rm rm : rms)
				{
					TreeNode treeNode = new TreeNode();
					treeNode.setName(rm.getName());
					treeNode.setIdentifier(rm.getId());
					treeNode.setPath(String.format("/%d/rms/%d", clusterId,rm.getId()));
					treeNode.setIsParent(true);
					treeNodes.add(treeNode);
				}
			}
		}
		else if( residue.startsWith("/resources") )
		{
			List<hpcgateway.wp.orm.beans.Resource> reses = hpcgatewayDao.queryResources(clusterId,null,null, null);
			if( reses != null && !reses.isEmpty() )
			{
				for(hpcgateway.wp.orm.beans.Resource res : reses)
				{
					TreeNode treeNode = new TreeNode();
					treeNode.setName(res.getType());
					treeNode.setIdentifier(res.getId());
					treeNode.setPath(String.format("/%d/resources/%d", clusterId,res.getId()));
					treeNode.setIsParent(false);
					treeNodes.add(treeNode);
				}
			}
		}
		else if( residue.startsWith("/roles") )
		{
			NodeRole[] roles = NodeRole.class.getEnumConstants();
			for(NodeRole role : roles)
			{
				TreeNode treeNode = new TreeNode();
				treeNode.setName(role.getName());
				treeNode.setIdentifier(role.getCode());
				treeNode.setPath(String.format("/%d/roles/%d", clusterId,role.getCode()));
				treeNode.setIsParent(true);
				treeNodes.add(treeNode);
			}
		}
		else if( residue.startsWith("/nodes") )
		{
			List<Node> nodes = hpcgatewayDao.queryNodes(clusterId, null, null, null, null);
			for(Node node : nodes)
			{
				TreeNode treeNode = new TreeNode();
				treeNode.setName(node.getName());
				treeNode.setIdentifier(node.getId());
				treeNode.setPath(String.format("/%s/nodes/%d", clusterId,node.getId()));
				treeNode.setIsParent(false);
				treeNodes.add(treeNode);
			}
		}
		else if( residue.startsWith("/properties") )
		{
			List<PropertyValue> properties = hpcgatewayDao.queryProperties(clusterId);
			if( properties != null && !properties.isEmpty() )
			{
				for(PropertyValue property : properties)
				{
					TreeNode treeNode = new TreeNode();
					treeNode.setName(property.getName());
					treeNode.setIdentifier(property.getName());
					treeNode.setPath(String.format("/%d/properties/%s", clusterId,treeNode.getName()));
					treeNode.setIsParent(false);
					treeNodes.add(treeNode);
				}
			}
		}
		else if( residue.startsWith("/parameters") )
		{
			List<ParameterValue> parameters = hpcgatewayDao.queryParameters(clusterId);
			if( parameters != null && !parameters.isEmpty() )
			{
				for(ParameterValue parameter : parameters)
				{
					TreeNode treeNode = new TreeNode();
					treeNode.setName(parameter.getName());
					treeNode.setIdentifier(parameter.getName());
					treeNode.setPath(String.format("/%d/parameter/%s", clusterId,parameter.getName()));
					treeNode.setIsParent(false);
					treeNodes.add(treeNode);
				}
			}
		}
		else if( residue.startsWith("/connects") )
		{
			ConnectType[] types = ConnectType.class.getEnumConstants();
			for(ConnectType type:types)
			{
				TreeNode treeNode = new TreeNode();
				treeNode.setName(type.getName());
				treeNode.setIdentifier(type.getCode());
				treeNode.setPath(String.format("/%d/connects/%s", clusterId,type.getCode()));
				treeNode.setIsParent(true);
				treeNodes.add(treeNode);
			}
		}

		return treeNodes;
	}
	
	/***************************************************\
	 * CLUSTER
	 *****************************************************/
	@Override
	public int countClusters(String name)
	{
		return hpcgatewayDao.countClusters(name);
	}
	
	@Override
	public List<Cluster> queryClusters(String name,Integer pageNo,Integer pageSize)
	{
		return hpcgatewayDao.queryClusters(name, pageNo, pageSize);
	}
	
	@Override
	public Cluster fetchCluster(Long id) throws Exception
	{
		return hpcgatewayDao.fetchCluster(id);
	}
	
	@Override
	public void saveCluster(Cluster cluster) throws Exception 
	{
		hpcgatewayDao.saveCluster(cluster);
	}
	
	@Override
	public void removeCluster(Long clusterId) throws Exception
	{
		hpcgatewayDao.removeCluster(clusterId);
	}
	
	
	/***************************************************\
	 * GROUP
	 *****************************************************/
	@Override
	public List<CloudGroup> queryGroupsForTree() throws Exception
	{
		return hpcgatewayDao.queryGroupsForTree();
	}
	
	@Override
	public List<CloudGroup> querySubGroups(Long groupId) throws Exception
	{
		return hpcgatewayDao.querySubGroups(groupId);
	}
	
	@Override
	public List<CloudGroup> queryGroups(Long userId) throws Exception
	{
		return hpcgatewayDao.queryGroups(userId);
	}
	
	@Override
	public int countGroups(String name) throws Exception
	{
		return hpcgatewayDao.countGroups(name);
	}
	
	@Override
	public List<CloudGroup> queryGroups(String name,Integer pageNo,Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryGroups(name, pageNo, pageSize);
	}

	@Override
	public void removeGroup(Long groupId) throws Exception
	{
		hpcgatewayDao.removeGroup(groupId);
	}

	@Override
	public void saveGroup(CloudGroup group) throws Exception
	{
		hpcgatewayDao.saveGroup(group);
	}
	
	@Override 
	public CloudGroup fetchGroup(Long groupId) throws Exception
	{
		CloudGroup group = hpcgatewayDao.fetchGroup(groupId);
		if( group == null ) {
			throw new Exception("The group not found for ID:"+groupId);
		}
		List<CloudUser> users = hpcgatewayDao.queryUsers(groupId);
		group.setUsers(users);
		return group;
	}

	
	/***************************************************\
	 * USER
	 *****************************************************/
	@Override
	public void setpwd(Long id,String password) throws Exception
	{
		String cryptedPassword = PasswordHelper.encryptedPassword(password);
		hpcgatewayDao.setpwd(id,cryptedPassword);
	}
	
	@Override
	public int countUsers(String name) throws Exception
	{
		return hpcgatewayDao.countUsers(name);
	}
	
	@Override
	public List<CloudUser> queryAllPayingUsers() throws Exception
	{
		return hpcgatewayDao.queryAllPayingUsers();
	}
	
	@Override
	public List<CloudUser> queryUsers(String name,Integer pageNo,Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryUsers(name, pageNo, pageSize);
	}
	
	@Override
	public CloudUser fetchUser(Long userId) throws Exception
	{
		return hpcgatewayDao.fetchUser(userId);
	}

	@Override
	public void saveUser(CloudUser user) throws Exception
	{
		if( user.getId() == null )
		{
			String password = user.getPassword();
			if( password == null )
			{
				password = "";
			}
			user.setPassword(PasswordHelper.encryptedPassword(password));
		}
		hpcgatewayDao.saveUser(user);
	}

	@Override
	public void removeUser(Long userId) throws Exception
	{
		hpcgatewayDao.removeUser(userId);
	}

	@Override
	public int countRoles(String name) throws Exception
	{
		return hpcgatewayDao.countRoles(name);
	}

	@Override
	public List<Role> queryRoles(String name, Integer pageNo, Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryRoles(name, pageNo, pageSize);
	}

	@Override
	public Role fetchRole(Long roleId) throws Exception
	{
		return hpcgatewayDao.fetchRole(roleId);
	}

	@Override
	public void saveRole(Role role) throws Exception
	{
		hpcgatewayDao.saveRole(role);
	}

	@Override
	public void removeRole(Long roleId) throws Exception
	{
		hpcgatewayDao.removeRole(roleId);
	}

	@Override
	public int countClusterUsers(Long clusterId, String name) throws Exception
	{
		return hpcgatewayDao.countClusterUsers(clusterId, name);
	}

	@Override
	public List<ClusterUser> queryClusterUsers(Long clusterId, String name, Integer pageNo, Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryClusterUsers(clusterId, name,pageNo,pageSize);
	}

	@Override
	public ClusterUser fetchClusterUser(Long clusterId, Long userId) throws Exception
	{
		return hpcgatewayDao.fetchClusterUser(clusterId,userId);
	}

	@Override
	public void saveClusterUser(Long clusterId, ClusterUser clusterUser) throws Exception
	{
		hpcgatewayDao.saveClusterUser(clusterId,clusterUser);
		
	}

	@Override
	public void removeClusterClusterUser(Long clusterId, Long userId) throws Exception
	{
		hpcgatewayDao.removeClusterUser(clusterId, userId);;
	}

	@Override
	public int countRms(Long clusterId,String name) throws Exception
	{
		return hpcgatewayDao.countRms(clusterId, name);
	}

	@Override
	public List<Rm> queryRms(Long clusterId,String name,Integer pageNo,Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryRms(clusterId,name,pageNo,pageSize);
	}
	
	@Override
	public Rm fetchRm(Long clusterId,Long id) throws Exception
	{
		return hpcgatewayDao.fetchRm(clusterId, id);
	}
	
	@Override
	public void saveRm(Long clusterId,Rm rm) throws Exception
	{
		hpcgatewayDao.saveRm(clusterId, rm);
	}
	
	@Override
	public void removeRm(Long clusterId,Long id) throws Exception
	{
		hpcgatewayDao.removeRm(clusterId, id);
	}


	@Override
	public int countResources(Long clusterId, String type) throws Exception
	{
		return hpcgatewayDao.countResources(clusterId, type);
	}

	@Override
	public List<hpcgateway.wp.orm.beans.Resource> queryResources(Long clusterId, String type, Integer pageNo, Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryResources(clusterId, type, pageNo, pageSize);
	}

	@Override
	public hpcgateway.wp.orm.beans.Resource fetchResource(Long clusterId, Long resId) throws Exception
	{
		return hpcgatewayDao.fetchResource(clusterId,resId);
	}

	@Override
	public void saveResource(Long clusterId, hpcgateway.wp.orm.beans.Resource resource) throws Exception
	{
		hpcgatewayDao.saveResource(clusterId, resource);		
	}

	@Override
	public void removeResource(Long clusterId, Long resId) throws Exception
	{
		hpcgatewayDao.removeResource(clusterId, resId);
	}

	@Override
	public int countNodes(Long clusterId, NodeRole role, String name) throws Exception
	{
		return hpcgatewayDao.countNodes(clusterId, role, name);
	}

	@Override
	public List<Node> queryNodes(Long clusterId,NodeRole role, String name, Integer pageNo, Integer pageSize) throws Exception
	{
		return hpcgatewayDao.queryNodes(clusterId, role, name, pageNo, pageSize);
	}

	@Override
	public Node fetchNode(Long clusterId, Long id) throws Exception
	{
		return hpcgatewayDao.fetchNode(clusterId,id);
	}

	@Override
	public void saveNode(Long clusterId, Node node) throws Exception
	{
		hpcgatewayDao.saveNode(clusterId, node);
		
	}

	@Override
	public void removeNode(Long clusterId, Long id) throws Exception
	{
		hpcgatewayDao.removeNode(clusterId, id);
	}
	


	@Override
	public Map<String,String> getClusterUser(String username,String password) throws Exception
	{
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", username);
		map.put("password", password);
		List<Cluster> clusters = hpcgatewayDao.queryClusters(null, null, null);
		if( clusters.size() != 1 )
		{
			return map;
		}
		Cluster cluster = clusters.get(0);
		ClusterUser clusterUser = hpcgatewayDao.fetchClusterUser(cluster.getId(), username);
		if( clusterUser != null )
		{
			map.put("username", clusterUser.getName());
			map.put("password", clusterUser.getPassword());
		}
		return map;
	}

	@Override
	public List<NodeConnect> queryConnections(Long clusterId, ConnectType connectType, NodeRole nodeRole) throws Exception
	{
		return  hpcgatewayDao.queryNodeConnects(clusterId, nodeRole, connectType);
	}

	
}
