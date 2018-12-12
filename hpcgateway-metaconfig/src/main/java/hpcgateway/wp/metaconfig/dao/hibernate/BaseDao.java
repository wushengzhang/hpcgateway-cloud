package hpcgateway.wp.metaconfig.dao.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import hpcgateway.wp.orm.beans.CloudGroup;
import hpcgateway.wp.orm.beans.CloudUser;
import hpcgateway.wp.orm.beans.Cluster;
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
import hpcgateway.wp.orm.beans.Resource;
import hpcgateway.wp.orm.beans.Rm;
import hpcgateway.wp.orm.beans.Role;
import hpcgateway.wp.orm.beans.RoleAlloc;
import hpcgateway.wp.orm.beans.TypeHelper;
import hpcgateway.wp.orm.hibernate.dao.EntityDao;
import hpcgateway.wp.orm.hibernate.mapping.HpcCluster;
import hpcgateway.wp.orm.hibernate.mapping.HpcClusterType;
import hpcgateway.wp.orm.hibernate.mapping.HpcClusterUser;
import hpcgateway.wp.orm.hibernate.mapping.HpcConnect;
import hpcgateway.wp.orm.hibernate.mapping.HpcConnectType;
import hpcgateway.wp.orm.hibernate.mapping.HpcGroup;
import hpcgateway.wp.orm.hibernate.mapping.HpcInterface;
import hpcgateway.wp.orm.hibernate.mapping.HpcNode;
import hpcgateway.wp.orm.hibernate.mapping.HpcNodeRole;
import hpcgateway.wp.orm.hibernate.mapping.HpcNodeRoleAlloc;
import hpcgateway.wp.orm.hibernate.mapping.HpcParamAssign;
import hpcgateway.wp.orm.hibernate.mapping.HpcParameter;
import hpcgateway.wp.orm.hibernate.mapping.HpcProperty;
import hpcgateway.wp.orm.hibernate.mapping.HpcPropertyAssign;
import hpcgateway.wp.orm.hibernate.mapping.HpcResource;
import hpcgateway.wp.orm.hibernate.mapping.HpcRm;
import hpcgateway.wp.orm.hibernate.mapping.HpcRole;
import hpcgateway.wp.orm.hibernate.mapping.HpcRoleAlloc;
import hpcgateway.wp.orm.hibernate.mapping.HpcCloudUser;

public class BaseDao extends EntityDao
{
	final public static String default_date_str = "yyyy-MM-dd HH:mm:ss";
	final public static DateFormat default_dateformat = new SimpleDateFormat(default_date_str);

	@Autowired(required=true)
	@Qualifier("hgMetaConfigSessionFactory") 
	public void setSessionFactory(SessionFactory sessionFactory) {
		if (this.hibernateTemplate == null || sessionFactory != this.hibernateTemplate.getSessionFactory()) {
			this.hibernateTemplate = createHibernateTemplate(sessionFactory);
		}
	}
	@Override
	protected final void checkDaoConfig() 
	{
		if (this.hibernateTemplate == null) {
			throw new IllegalArgumentException("'sessionFactory' or 'hibernateTemplate' is required");
		}
	}
	
	protected Cluster copyClusterFromDb(Cluster metaCluster, HpcCluster hpcCluster) 
	{
		if( metaCluster == null )
		{
			metaCluster = new Cluster();
		}
		
		metaCluster.setId(hpcCluster.getHpcClusterId());
		metaCluster.setName(hpcCluster.getHpcClusterName());
		metaCluster.setDesc(hpcCluster.getHpcClusterDesc());
		metaCluster.setUsername(hpcCluster.getHpcClusterUsername());
		metaCluster.setPassword(hpcCluster.getHpcClusterPassword());
		metaCluster.setIdentifier(hpcCluster.getHpcClusterIdentifier());
		metaCluster.setRealname(hpcCluster.getHpcClusterRealname());
		metaCluster.setEmail(hpcCluster.getHpcClusterEmail());
		metaCluster.setPhone(hpcCluster.getHpcClusterPhone());
		metaCluster.setWechat(hpcCluster.getHpcClusterWechat());

		HpcClusterType hpcClusterType = hpcCluster.getHpcClusterType();
		if( hpcClusterType != null )
		{
			try
			{
				metaCluster.setTypeCode(hpcClusterType.getHpcClusterTypeCode());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return metaCluster;
	}
	
	protected HpcCluster copyClusterToDb(HpcCluster hpcCluster, Cluster metaCluster)
	{
		if (hpcCluster == null)
		{
			hpcCluster = new HpcCluster();
		}

		hpcCluster.setHpcClusterName(metaCluster.getName());
		hpcCluster.setHpcClusterDesc(metaCluster.getDesc());
		hpcCluster.setHpcClusterUsername(metaCluster.getUsername());
		hpcCluster.setHpcClusterPassword(metaCluster.getPassword());
		hpcCluster.setHpcClusterIdentifier(metaCluster.getIdentifier());
		hpcCluster.setHpcClusterRealname(metaCluster.getRealname());
		hpcCluster.setHpcClusterEmail(metaCluster.getEmail());
		hpcCluster.setHpcClusterPhone(metaCluster.getPhone());
		hpcCluster.setHpcClusterWechat(metaCluster.getWechat());

		return hpcCluster;
	}
	
	protected NodeConnect copyNodeConnectFromDb(NodeConnect nodeConnect, HpcConnect hpcConnnect)
	{
		if( nodeConnect == null )
		{
			nodeConnect = new NodeConnect();
		}
		HpcNode hpcNode = hpcConnnect.getHpcNode();
		if( hpcNode != null )
		{
			this.copyNodeFromDb(nodeConnect, hpcNode);
		}
		
		nodeConnect.setId(hpcConnnect.getHpcConnId());
		nodeConnect.setUrl(hpcConnnect.getHpcConnUrl());
		nodeConnect.setPort(hpcConnnect.getHpcConnPort());
		nodeConnect.setUser(hpcConnnect.getHpcConnUser());
		nodeConnect.setPass(hpcConnnect.getHpcConnPass());
		nodeConnect.setCrypt(hpcConnnect.getHpcConnCrypt());
		HpcConnectType hpcConnectType = hpcConnnect.getHpcConnectType();
		if( hpcConnectType != null )
		{
			try
			{
				nodeConnect.setTypeCode(hpcConnectType.getHpcConnTypeCode());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return nodeConnect;
	}
	
	protected Node copyNodeFromDb(Node metaNode,HpcNode hpcNode)
	{
		if( metaNode == null )
		{
			metaNode = new Node();
		}
		
		metaNode.setId(hpcNode.getHpcNodeId());
		metaNode.setName(hpcNode.getHpcNodeName());
		metaNode.setAliasname(hpcNode.getHpcNodeAliasnames());

		return metaNode;
	}
	
	protected HpcNode copyNodeToDb(HpcNode hpcNode,Node metaNode)
	{
		if( hpcNode == null )
		{
			hpcNode = new HpcNode();
		}
		
		hpcNode.setHpcNodeName(metaNode.getName());
		hpcNode.setHpcNodeAliasnames(metaNode.getAliasname());

		return hpcNode;
	}
	
	protected NodeRoleAlloc copyNodeRoleAllocFromDb(NodeRoleAlloc ra, HpcNodeRoleAlloc hpcRa)
	{
		if( ra == null )
		{
			ra = new NodeRoleAlloc();
		}
		ra.setDescription(hpcRa.getHpcNodeRoleAllocDescription());
		ra.setId(hpcRa.getHpcNodeRoleAllocId());
		ra.setName(hpcRa.getHpcNodeRoleAllocName());
		ra.setPriority(hpcRa.getHpcNodeRoleAllocPriority());
		ra.setTime(hpcRa.getHpcNodeRoleAllocTime());
		HpcNodeRole role = hpcRa.getHpcNodeRole();
		if( role != null )
		{
			try
			{
				ra.setRoleCode(role.getHpcNodeRoleCode());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return ra;
	}
	
	
	
	protected CloudGroup copyGroupFromDb(CloudGroup g, HpcGroup hpcGroup)
	{
		if( g == null )
		{
			g = new CloudGroup();
		}
		HpcGroup parent = hpcGroup.getHpcGroup();
		g.setId(hpcGroup.getHpcGroupId());
		if( parent != null)
		{
			g.setParentId(parent.getHpcGroupId());
		}
		g.setName(hpcGroup.getHpcGroupName());
		g.setCode(hpcGroup.getHpcGroupCode());
		g.setDesc(hpcGroup.getHpcGroupDesc());
		return g;
	}
	
	protected HpcGroup copyGroupToDb(HpcGroup hpcGroup,CloudGroup g)
	{
		if( hpcGroup == null ) {
			hpcGroup = new HpcGroup();
		}
		hpcGroup.setHpcGroupCode(g.getCode());
		hpcGroup.setHpcGroupDesc(g.getDesc());
		hpcGroup.setHpcGroupName(g.getName());
		return hpcGroup;
	}
	
	protected NodeRole copyNodeRoleFromDb(HpcNodeRole hpcNodeRole) throws Exception
	{
		return TypeHelper.findType(NodeRole.class, hpcNodeRole.getHpcNodeRoleCode());
	}
	
	protected HpcNodeRole copyNodeRoleToDb(HpcNodeRole hpcNodeRole,NodeRole r)
	{
		if( hpcNodeRole == null )
		{
			hpcNodeRole = new HpcNodeRole();
		}
		
		hpcNodeRole.setHpcNodeRoleCode(r.getCode());
		hpcNodeRole.setHpcNodeRoleDesc(r.getDesc());
		hpcNodeRole.setHpcNodeRoleName(r.getName());
		
		return hpcNodeRole;
	}
	
	protected HpcNodeRoleAlloc copyNodeRoleAllocToDb(HpcNodeRoleAlloc hpcNodeRoleAlloc,NodeRoleAlloc ra)
	{
		if( hpcNodeRoleAlloc == null )
		{
			hpcNodeRoleAlloc = new HpcNodeRoleAlloc();
		}
		
		hpcNodeRoleAlloc.setHpcNodeRoleAllocName(ra.getName());
		hpcNodeRoleAlloc.setHpcNodeRoleAllocPriority(ra.getPriority());
		hpcNodeRoleAlloc.setHpcNodeRoleAllocDescription(ra.getDescription());
		hpcNodeRoleAlloc.setHpcNodeRoleAllocTime(ra.getTime());
		
		return hpcNodeRoleAlloc;
	}
	
	protected PropertyValue copyPropertyValueFromDb(PropertyValue pa,HpcPropertyAssign hpcPa)
	{
		HpcProperty hpcProperty = hpcPa.getHpcProperty();
		if( hpcProperty == null  )
		{
			return null;
		}
		if( pa == null )
		{
			pa = new PropertyValue();
		}

		pa.setName(hpcProperty.getHpcPropName());
		pa.setDescription(hpcProperty.getHpcPropDesc());
		pa.setValue(hpcPa.getHpcPropertyAssignValue());
		pa.setTime(hpcPa.getHpcPropertyAssignTime());
		
		return pa;
	}
	
	protected HpcProperty copyPropertyToDb(HpcProperty hpcProperty, PropertyValue pa)
	{
		if( hpcProperty == null )
		{
			hpcProperty = new HpcProperty();
		}
		
		hpcProperty.setHpcPropName(pa.getName());
		hpcProperty.setHpcPropDesc(pa.getDescription());
		
		return hpcProperty;
	}
	
	protected HpcPropertyAssign copyPropertyValueToDb(HpcPropertyAssign hpcPropertyAssign, PropertyValue pa)
	{
		if( hpcPropertyAssign == null )
		{
			hpcPropertyAssign = new HpcPropertyAssign();
		}
		
		hpcPropertyAssign.setHpcPropertyAssignValue(pa.getValue());
		hpcPropertyAssign.setHpcPropertyAssignTime(pa.getTime());

		return hpcPropertyAssign;
	}
	
	
	protected ParameterValue copyParameterValueFromDb(ParameterValue pa,HpcParamAssign hpcPa)
	{
		HpcParameter hpcParameter = hpcPa.getHpcParameter();
		if( hpcParameter == null )
		{
			return null;
		}

		if( pa == null )
		{
			pa = new ParameterValue();
		}
		
		pa.setName(hpcParameter.getHpcParamName());
		pa.setDescription(hpcParameter.getHpcParamDesc());
		pa.setValue(hpcPa.getHpcParamAssignValue());
		pa.setTime(hpcPa.getHpcParamAssignTime());
		
		return pa;
	}
	
	protected HpcParameter copyParameterToDb(HpcParameter hpcParameter,ParameterValue pa)
	{
		if( hpcParameter == null )
		{
			hpcParameter = new HpcParameter();
		}
		hpcParameter.setHpcParamName(pa.getName());
		hpcParameter.setHpcParamDesc(pa.getDescription());
		return hpcParameter;
	}
	
	protected HpcParamAssign copyParameterValueToDb(HpcParamAssign hpcParamAssign,ParameterValue pa)
	{
		if( hpcParamAssign == null )
		{
			hpcParamAssign = new HpcParamAssign();
		}
		hpcParamAssign.setHpcParamAssignValue(pa.getValue());
		hpcParamAssign.setHpcParamAssignTime(pa.getTime());
		return hpcParamAssign;
	}
	
	protected Interface copyInterfaceFromDb(Interface metaInterface, HpcInterface hpcInterface)
	{
		if( metaInterface == null )
		{
			metaInterface = new Interface();
		}
		
		metaInterface.setId(hpcInterface.getHpcIfcId());
		metaInterface.setName(hpcInterface.getHpcIfcName());
		metaInterface.setAddr(hpcInterface.getHpcIfcAddr());
		metaInterface.setAlias(hpcInterface.getHpcIfcAlias());
		
		return metaInterface;
	}
	
	protected HpcInterface copyInterfaceToDb(HpcInterface hpcInterface, Interface metaInterface)
	{
		if( hpcInterface == null )
		{
			hpcInterface = new HpcInterface();
		}
		
		hpcInterface.setHpcIfcName(metaInterface.getName());
		hpcInterface.setHpcIfcAddr(metaInterface.getAddr());
		hpcInterface.setHpcIfcAlias(metaInterface.getAlias());
		
		return hpcInterface;
	}

	protected HpcConnect copyConnectToDb(HpcConnect hpcConnect, Connect metaConnect)
	{
		if( hpcConnect == null )
		{
			hpcConnect = new HpcConnect();
		}
		
		hpcConnect.setHpcConnCrypt(metaConnect.getCrypt());
		hpcConnect.setHpcConnPass(metaConnect.getPass());
		hpcConnect.setHpcConnPort(metaConnect.getPort());
		hpcConnect.setHpcConnUrl(metaConnect.getUrl());
		hpcConnect.setHpcConnUser(metaConnect.getUser());

		return hpcConnect;
	}

	protected HpcConnectType copytConnectTypeToDb(HpcConnectType hpcConnectType, ConnectType metaConnectType)
	{
		if( hpcConnectType == null )
		{
			hpcConnectType = new HpcConnectType();
		}
		hpcConnectType.setHpcConnTypeCode(metaConnectType.getCode());
		hpcConnectType.setHpcConnTypeDesc(metaConnectType.getDesc());
		hpcConnectType.setHpcConnTypeName(metaConnectType.getName());
		return hpcConnectType;
	}
	
	
	protected CloudUser copyUserFromDb(CloudUser user,HpcCloudUser hpcUser)
	{
		if( user == null )
		{
			user = new CloudUser();
		}
		
		user.setId(hpcUser.getHpcUserId());
		user.setName(hpcUser.getHpcUserName());
		user.setPassword(hpcUser.getHpcUserPasswd());
		user.setBalance(hpcUser.getHpcUserBalance());
		user.setCharged(hpcUser.getHpcUserCharged());
		user.setJobFee(hpcUser.getHpcUserJobFee());
		user.setJobNum(hpcUser.getHpcUserJobNum());
		user.setJobHosts(hpcUser.getHpcUserJobHosts());
		user.setJobCores(hpcUser.getHpcUserJobCores());
		Date jobTime = hpcUser.getHpcUserJobTime();
		if( jobTime != null )
		{
			user.setJobTime(jobTime);
			user.setJobTimeStr(default_dateformat.format(jobTime));
		}
		else
		{
			user.setJobTime(null);
			user.setJobTimeStr("n/a");
		}
		
		user.setStorageFee(hpcUser.getHpcUserStorageFee());
		user.setStorageCap(hpcUser.getHpcUserStorageCap());
		user.setStorageUnit(hpcUser.getHpcUserStorageUnit());
		Date storageTime = hpcUser.getHpcUserStorageTime();
		if( storageTime != null )
		{
			user.setStorageTime(storageTime);
			user.setStorageTimeStr(default_dateformat.format(storageTime));
		}
		else
		{
			user.setStorageTime(null);
			user.setStorageTimeStr("n/a");
		}
				
		return user;
	}
	

	protected HpcCloudUser copyUserToDb(HpcCloudUser hpcUser, CloudUser user) 
	{
		if( hpcUser == null )
		{
			hpcUser = new HpcCloudUser();
		}

		hpcUser.setHpcUserName(user.getName());

		return hpcUser;
	}
	
	protected ClusterUser copyClusterUserFromDb(ClusterUser clusterUser,HpcClusterUser hpcClusterUser)
	{
		if( clusterUser == null )
		{
			clusterUser = new ClusterUser();
		}
		
		clusterUser.setId(hpcClusterUser.getHpcClstrUsrId());
		clusterUser.setName(hpcClusterUser.getHpcClstrUsrName());
		clusterUser.setPassword(hpcClusterUser.getHpcClstrUsrPasswd());
		clusterUser.setAuto(hpcClusterUser.getHpcClstrUsrAuto());
		clusterUser.setIdrsa(hpcClusterUser.getHpcClstrUsrIdrsa());
		clusterUser.setBalance(hpcClusterUser.getHpcClstrUsrBalance());
		clusterUser.setCharged(hpcClusterUser.getHpcClstrUsrCharged());
		clusterUser.setConsumed(hpcClusterUser.getHpcClstrUsrConsumed());
		clusterUser.setJobFee(hpcClusterUser.getHpcClstrUsrJobFee());
		clusterUser.setJobNum(hpcClusterUser.getHpcClstrUsrJobNum());
		clusterUser.setJobHosts(hpcClusterUser.getHpcClstrUsrJobHosts());
		clusterUser.setJobCores(hpcClusterUser.getHpcClstrUsrJobCores());
		Date jobTime = hpcClusterUser.getHpcClstrUsrJobTime();
		if( jobTime != null )
		{
			clusterUser.setJobTime(jobTime);
			clusterUser.setJobTimeStr(default_dateformat.format(jobTime));
		}
		else
		{
			clusterUser.setJobTime(null);
			clusterUser.setJobTimeStr("n/a");
		}
		clusterUser.setStorageFee(hpcClusterUser.getHpcClstrUsrStrgFee());
		clusterUser.setStorageCap(hpcClusterUser.getHpcClstrUsrStrgCap());
		clusterUser.setStorageUnit(hpcClusterUser.getHpcClstrUsrStrgUnit());
		Date storageTime = hpcClusterUser.getHpcClstrUsrStrgTime();
		if( storageTime != null )
		{
			clusterUser.setStorageTime(storageTime);
			clusterUser.setStorageTimeStr(default_dateformat.format(storageTime));
		}
		else
		{
			clusterUser.setStorageTime(null);
			clusterUser.setStorageTimeStr("n/a");
		}
		return clusterUser;
	}
	
	protected HpcClusterUser copyClusterUserToDb(HpcClusterUser hpcClusterUser,ClusterUser clusterUser)
	{
		if( hpcClusterUser == null )
		{
			hpcClusterUser = new HpcClusterUser();
		}
		
		hpcClusterUser.setHpcClstrUsrName(clusterUser.getName());
		hpcClusterUser.setHpcClstrUsrPasswd(clusterUser.getPassword());
		hpcClusterUser.setHpcClstrUsrAuto(clusterUser.getAuto());
		hpcClusterUser.setHpcClstrUsrIdrsa(clusterUser.getIdrsa());
		hpcClusterUser.setHpcClstrUsrBalance(clusterUser.getBalance());
		hpcClusterUser.setHpcClstrUsrCharged(clusterUser.getCharged());
		hpcClusterUser.setHpcClstrUsrConsumed(clusterUser.getConsumed());
		hpcClusterUser.setHpcClstrUsrJobFee(clusterUser.getJobFee());
		hpcClusterUser.setHpcClstrUsrJobNum(clusterUser.getJobNum());
		hpcClusterUser.setHpcClstrUsrJobHosts(clusterUser.getJobHosts());
		hpcClusterUser.setHpcClstrUsrJobCores(clusterUser.getJobCores());
		hpcClusterUser.setHpcClstrUsrJobTime(clusterUser.getJobTime());
		hpcClusterUser.setHpcClstrUsrStrgFee(clusterUser.getStorageFee());
		hpcClusterUser.setHpcClstrUsrStrgCap(clusterUser.getStorageCap());
		hpcClusterUser.setHpcClstrUsrStrgUnit(clusterUser.getStorageUnit());
		hpcClusterUser.setHpcClstrUsrStrgTime(clusterUser.getStorageTime());
		
		return hpcClusterUser;
	}
	
	
	protected RoleAlloc copyRoleAllocFromDb(RoleAlloc roleAlloc,HpcRoleAlloc hpcRoleAlloc)
	{
		if( roleAlloc == null )
		{
			roleAlloc = new RoleAlloc();
		}
		
		roleAlloc.setId(hpcRoleAlloc.getHpcRoleAllocId());
		roleAlloc.setTime(hpcRoleAlloc.getHpcRoleAllocTime());
		roleAlloc.setStart(hpcRoleAlloc.getHpcRoleAllocStart());
		roleAlloc.setExpire(hpcRoleAlloc.getHpcRoleAllocExpire());
		
		return roleAlloc;
	}
	
	protected HpcRoleAlloc copyRoleAllocToDb(HpcRoleAlloc hpcRoleAlloc,RoleAlloc roleAlloc)
	{
		if( hpcRoleAlloc == null )
		{
			hpcRoleAlloc = new HpcRoleAlloc();
		}
		
		hpcRoleAlloc.setHpcRoleAllocTime(roleAlloc.getTime());
		hpcRoleAlloc.setHpcRoleAllocStart(roleAlloc.getStart());
		hpcRoleAlloc.setHpcRoleAllocExpire(roleAlloc.getExpire());
		
		return hpcRoleAlloc;
	}
	
	protected Role copyRoleFromDb(Role role,HpcRole hpcRole)
	{
		if( role == null )
		{
			role = new Role();
		}
		
		role.setId(hpcRole.getHpcRoleId());
		role.setName(hpcRole.getHpcRoleName());
		role.setCode(hpcRole.getHpcRoleCode());
		role.setDesc(hpcRole.getHpcRoleDescription());
		role.setRights(hpcRole.getHpcRoleRights());

		return role;
	}
	
	protected HpcRole copyRoleToDb(HpcRole hpcRole,Role role)
	{
		if( hpcRole == null )
		{
			hpcRole = new HpcRole();
		}
		
		hpcRole.setHpcRoleName(role.getName());
		hpcRole.setHpcRoleCode(role.getCode());
		hpcRole.setHpcRoleDescription(role.getDesc());
		hpcRole.setHpcRoleRights(role.getRights());
		
		return hpcRole;
	}
	
	protected Rm copyRmFromDb(Rm rm,HpcRm hpcRm)
	{
		if( rm == null )
		{
			rm = new Rm();
		}
		
		rm.setId(hpcRm.getHpcRmId());
		rm.setName(hpcRm.getHpcRmName());
		rm.setVersion(hpcRm.getHpcRmVersion());
		rm.setTopdir(hpcRm.getHpcRmTopdir());
		rm.setBin(hpcRm.getHpcRmBin());
		rm.setSbin(hpcRm.getHpcRmSbin());
		rm.setLib(hpcRm.getHpcRmLib());
		rm.setEtc(hpcRm.getHpcRmEtc());
		
		return rm;
	}
	
	protected HpcRm copyRmToDb(HpcRm hpcRm,Rm rm)
	{
		if( hpcRm == null )
		{
			hpcRm = new HpcRm();
		}
		
		hpcRm.setHpcRmName(rm.getName());
		hpcRm.setHpcRmVersion(rm.getVersion());
		hpcRm.setHpcRmTopdir(rm.getTopdir());
		hpcRm.setHpcRmBin(rm.getBin());
		hpcRm.setHpcRmSbin(rm.getSbin());
		hpcRm.setHpcRmLib(rm.getLib());
		hpcRm.setHpcRmEtc(rm.getEtc());
		
		return hpcRm;
	}
	
	protected Resource copyResourceFromDb(Resource resource,HpcResource hpcResource)
	{
		if( resource == null )
		{
			resource = new Resource();
		}

		resource.setId(hpcResource.getHpcResourceId());
		resource.setType(hpcResource.getHpcResourceType());
		resource.setUnit(hpcResource.getHpcResourceUnit());
		resource.setLimit(hpcResource.getHpcResourceLimit());
		
		return resource;
	}
	
	protected HpcResource copyResourceToDb(HpcResource hpcResource,Resource resource)
	{
		if( hpcResource == null )
		{
			hpcResource = new HpcResource();
		}
		
		hpcResource.setHpcResourceType(resource.getType());
		hpcResource.setHpcResourceUnit(resource.getUnit());
		hpcResource.setHpcResourceLimit(resource.getLimit());
		
		return hpcResource;
	}
	
	protected Connect copyConnectFromDb(Connect connect,HpcConnect hpcConnect)
	{
		if( connect == null )
		{
			connect = new Connect();
		}

		HpcConnectType hpcConnectType = hpcConnect.getHpcConnectType();
		if( hpcConnectType != null )
		{
			try
			{
				connect.setTypeCode(hpcConnectType.getHpcConnTypeCode());
			}
			catch(Exception ex)
			{
				logger.warn(String.format("Connection type %d is not defined",hpcConnectType.getHpcConnTypeCode()));
			}
		}
		
		connect.setUrl(hpcConnect.getHpcConnUrl());
		connect.setPort(hpcConnect.getHpcConnPort());
		connect.setUser(hpcConnect.getHpcConnUser());
		connect.setPass(hpcConnect.getHpcConnPass());
		connect.setCrypt(hpcConnect.getHpcConnCrypt());
		
		return connect;
	}
	

	
}
