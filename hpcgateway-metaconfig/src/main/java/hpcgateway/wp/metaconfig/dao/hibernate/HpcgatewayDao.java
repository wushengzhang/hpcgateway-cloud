package hpcgateway.wp.metaconfig.dao.hibernate;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hpcgateway.wp.metaconfig.dao.hibernate.BaseDao;
import hpcgateway.wp.orm.hibernate.mapping.HpcCluster;
import hpcgateway.wp.orm.hibernate.mapping.HpcClusterType;
import hpcgateway.wp.orm.hibernate.mapping.HpcClusterUser;
import hpcgateway.wp.orm.hibernate.mapping.HpcConnect;
import hpcgateway.wp.orm.hibernate.mapping.HpcConnectType;
import hpcgateway.wp.orm.hibernate.mapping.HpcGroup;
import hpcgateway.wp.orm.hibernate.mapping.HpcGroupAlloc;
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
import hpcgateway.wp.orm.beans.Resource;
import hpcgateway.wp.orm.beans.Rm;
import hpcgateway.wp.orm.beans.Role;
import hpcgateway.wp.orm.hibernate.mapping.HpcCloudUser;

//@Scope("session")

@Repository("hpcgatewayDao")
public class HpcgatewayDao extends BaseDao
{
	private Logger logger = Logger.getLogger(this.getClass());

	public <T> T findEnumValue(Class<T> clazz,int code) throws Exception
	{
		if( !clazz.isEnum() )
		{
			throw new Exception(String.format("Class %s not enum class",clazz.getName()));
		}
		Method method = clazz.getMethod("getCode");
		T[] ts = clazz.getEnumConstants();
		for(T t : ts)
		{
			Object object = method.invoke(t);
			if( !(object instanceof Integer) )
			{
				throw new Exception("non-integer result for method getCode");
			}
			Integer __code = (Integer)object;
			if( __code != null && __code == code )
			{
				return t;
			}
		}
		return null;
	}
	
	/**
	 * 用于查询指定节点 连接类型的连接信息
	 *  这个方法主要用于SSH远程执行命令/读写文件时查询连接所需的信息
	 * @param nodeId
	 * 	 	node's ID for query
	 * @param connectType
	 * 		connection type for query
	 * @return
	 * 		NodeConnect列表
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<NodeConnect> queryNodeConnects(Long nodeId, ConnectType connectType) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcConnect c left join fetch c.hpcConnectType where c.hpcNode.hpcNodeId=:nodeId");
		map.put("nodeId", nodeId);
		if( connectType != null )
		{
			buffer.append(" and c.hpcConnectType.hpcConnTypeCode=:connCode");
			map.put("connCode", connectType.getCode());
		}
		String hql = buffer.toString();		
		List<HpcConnect> list = this.query(HpcConnect.class, hql, map, null, null);
		List<NodeConnect> ncs = new ArrayList<NodeConnect>();
		for(HpcConnect c : list)
		{
			NodeConnect nc = copyNodeConnectFromDb(null,c);
			ncs.add(nc);
		}
		return ncs;
	}
	
	/**
	 * 用于查询指定角色和连接类型的节点和连接信息
	 *  这个方法主要用于SSH远程执行命令/读写文件时查询连接所需的信息
	 * @param clusterId
	 *     集群ID
	 * @param role
	 *     节点角色类型
	 * @param connType
	 *     远程连接类型，当前只有SSH
	 * @return
	 *     NodeConnect列表
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<NodeConnect> queryNodeConnects(Long clusterId, NodeRole role, ConnectType connectType) throws Exception
	{
		if( clusterId == null || clusterId <= 0 )
		{
			throw new Exception("queryNodeConnects: cluster's ID is required.");
		}
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcConnect c left join fetch c.hpcNode left join fetch c.hpcConnectType where c.hpcNode.hpcCluster.hpcClusterId=:clusterId");
		map.put("clusterId", clusterId);
		if( role != null )
		{
			buffer.append(" and c.hpcNode.hpcNodeId in (select a.hpcNode.hpcNodeId from HpcNodeRoleAlloc a where a.hpcNodeRole.hpcNodeRoleCode=:roleCode)");
			map.put("roleCode", role.getCode());
		}
		if( connectType != null )
		{
			buffer.append(" and c.hpcConnectType.hpcConnTypeCode=:connCode");
			map.put("connCode", connectType.getCode());
		}
		String hql = buffer.toString();		
		List<HpcConnect> list = this.query(HpcConnect.class, hql, map, null, null);
		List<NodeConnect> ncs = new ArrayList<NodeConnect>();
		for(HpcConnect c : list)
		{
			NodeConnect nc = copyNodeConnectFromDb(null,c);
			ncs.add(nc);
		}
		return ncs;
	}
	
	
	/*
	 * cluster
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public Cluster fetchCluster(Long id) throws Exception
	{
		HpcCluster hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c left join fetch c.hpcClusterType where c.hpcClusterId=:clusterId", "clusterId", id);
		Cluster metaCluster = copyClusterFromDb(null,hpcCluster);
		return metaCluster;
	}
	
	/**
	 * query the number of clusters
	 * @param name
	 *   cluster name
	 * @return
	 *   the number of returned clusters
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countClusters(String name)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from HpcCluster c");
		Map<String,Object> map = new HashMap<String,Object>();
		if( name != null && !name.isEmpty() )
		{
			buffer.append(" where c.hpcClusterName like :name");
			map.put("name", String.format("%%%s%%", name));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	
	/*
	 * 
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Cluster> queryClusters(String name,Integer pageNo,Integer pageSize)
	{
		List<Cluster> list = new ArrayList<Cluster>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcCluster c left join fetch c.hpcClusterType");
		Map<String,Object> map = new HashMap<String,Object>();
		if( name != null && !name.isEmpty() )
		{
			buffer.append(" where c.hpcClusterName like :name");
			map.put("name", String.format("%%%s%%", name));
		}
		String hql = buffer.toString();
		List<HpcCluster> clusters = this.query(HpcCluster.class, hql, map, pageNo, pageSize);
		if( clusters != null && !clusters.isEmpty() )
		{
			for(HpcCluster cluster : clusters)
			{
				Cluster metaCluster = copyClusterFromDb(null,cluster);
				list.add(metaCluster);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param metaCluster
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveCluster(Cluster metaCluster) throws Exception
	{
		HpcCluster hpcCluster = null;
		if( metaCluster.getId() != null )
		{
			hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c left join fetch c.hpcClusterType where c.hpcClusterId=:clusterId","clusterId",metaCluster.getId());
			if( hpcCluster == null )
			{
				throw new Exception(String.format("正在修改的集群【%d】已经不存在",metaCluster.getId()));
			}
		}
		
		hpcCluster = copyClusterToDb(hpcCluster,metaCluster);

		HpcClusterType hpcClusterType = null;
		ClusterType type = metaCluster.getType();
		if( type != null )
		{
			hpcClusterType = this.fetch(HpcClusterType.class, "from HpcClusterType t where t.hpcClusterTypeCode=:code", "code",type.getCode());
			if( hpcClusterType == null )
			{
				hpcClusterType = new HpcClusterType();
				hpcClusterType.setHpcClusterTypeCode(type.getCode());
				hpcClusterType.setHpcClusterTypeName(type.getName());
				hpcClusterType.setHpcClusterTypeDesc(type.getDesc());
				this.save(HpcClusterType.class, hpcClusterType);
			}
		}
		
		hpcCluster.setHpcClusterType(hpcClusterType);
		this.save(HpcCluster.class, hpcCluster);
	}

	/**
	 * 
	 * @param clusterId
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeCluster(Long clusterId) throws Exception
	{
		String[] hqls = new String[] {
				"delete HpcConnect c where c.hpcNode.hpcNodeId in (select n.hpcNodeId from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId)",
				"delete HpcPropertyAssign pa where pa.hpcNode.hpcNodeId in (select n.hpcNodeId from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId)",
				"delete HpcParamAssign pa where pa.hpcNode.hpcNodeId in (select n.hpcNodeId from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId)",
				"delete HpcNodeRoleAlloc ra where ra.hpcNode.hpcNodeId in (select n.hpcNodeId from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId)",
				"delete HpcInterface i where i.hpcNode.hpcNodeId in (select n.hpcNodeId from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId)",
				"delete HpcRm rm where rm.hpcCluster.hpcClusterId = :clusterId",
				"delete HpcNode n where n.hpcCluster.hpcClusterId = :clusterId",
				"delete HpcResource r where r.hpcCluster.hpcClusterId = :clusterId",
				"delete HpcClusterUser u where u.hpcCluster.hpcClusterId = :clusterId",
				"delete HpcCluster c where c.hpcClusterId = :clusterId"
		};
		
		for(String hql : hqls)
		{
			this.execute(hql, "clusterId", clusterId);
		}
	}
	
	/*
	 * 
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<NodeRole> queryNodeRoles(String name,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcNodeRole r");
		Map<String,Object> map = new HashMap<String,Object>();
		if( name != null ) {
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" r.hpcNodeRoleName like :name");	
			map.put("name", name);
		}
		buffer.append(" order by r.hpcNodeCode");
		String hql = buffer.toString();
		List<HpcNodeRole> hpcRoles = this.query(HpcNodeRole.class, hql, map, pageNo, pageSize);
		List<NodeRole> roles = new ArrayList<NodeRole>();
		for(HpcNodeRole hpcRole : hpcRoles)
		{
			try
			{
				NodeRole role = this.copyNodeRoleFromDb(hpcRole);
				roles.add(role);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return roles;
	}

	/*
	 * Node 
	 */
	
//	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
//	public List<Node> queryNodes(Long clusterId,NodeRole role) throws Exception
//	{
//		String hql = "from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId and n.hpcNodeId in (select a.hpcNode.hpcNodeId from HpcNodeRoleAlloc a where a.hpcNodeRole.hpcNodeRoleCode=:roleCode)";
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("clusterId", clusterId);
//		map.put("roleCode", role.getCode());
//		List<HpcNode> list = this.query(HpcNode.class, hql,map, null, null);
//		List<Node> nodes = new ArrayList<Node>();
//		for(HpcNode n : list)
//		{
//			Node node = copyFromHpcNodeToNode(null,n);
//			nodes.add(node);
//		}
//		return nodes;
//	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countNodes(Long clusterId,NodeRole role,String name) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcNode n");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( role != null )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcNodeId in (select a.hpcNode.hpcNodeId from HpcNodeRoleAlloc a where a.hpcNodeRole.hpcNodeRoleCode=:roleCode)");
			map.put("roleCode", role.getCode());
		}
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcNodeName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	



	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Node> queryNodes(Long clusterId,NodeRole role,String name,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcNode n");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( role != null )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcNodeId in (select a.hpcNode.hpcNodeId from HpcNodeRoleAlloc a where a.hpcNodeRole.hpcNodeRoleCode=:roleCode)");
			map.put("roleCode", role.getCode());
		}
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" n.hpcNodeName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		buffer.append(" order by n.hpcNodeName ASC");
		String hql = buffer.toString();
		List<HpcNode> hpcNodes = this.query(HpcNode.class, hql, map, pageNo, pageSize);
		List<Node> nodes = new ArrayList<Node>();
		List<Long> nodeIds = new ArrayList<Long>();
		Map<Long,Node> nodeMap = new TreeMap<Long,Node>();
		for(HpcNode hpcNode : hpcNodes)
		{
			Node node = copyNodeFromDb(null,hpcNode);
			nodes.add(node);
			nodeIds.add(node.getId());
			nodeMap.put(node.getId(), node);
		}
		
		String h = "select a.hpcNode.hpcNodeId,a from HpcNodeRoleAlloc a left join fetch a.hpcNodeRole where a.hpcNode.hpcNodeId in (:nodeIds)";
		List<Object[]> ras = this.query(Object[].class, h,"nodeIds",nodeIds,null,null);
		for(Object[] ra : ras)
		{
			Long nodeId = (Long)ra[0];
			Node node = nodeMap.get(nodeId);
			HpcNodeRoleAlloc hpcNodeRoleAlloc = (HpcNodeRoleAlloc)ra[1];
			NodeRoleAlloc nodeRoleAlloc = copyNodeRoleAllocFromDb(null,hpcNodeRoleAlloc);
			node.addRole(nodeRoleAlloc);
		}
		
		return nodes;
	}
	
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public Node fetchNode(Long clusterId, Long id) throws Exception
	{
		String hql = "from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId and n.hpcNodeId=:nodeId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("nodeId",id);
		HpcNode hpcNode = this.fetch(HpcNode.class, hql, map);
		if( hpcNode == null )
		{
			return null;
		}
		Node node = copyNodeFromDb(null,hpcNode);
		
		List<HpcConnect> hpcConnects = this.query(HpcConnect.class, "from HpcConnect c left join fetch c.hpcConnectType where c.hpcNode.hpcNodeId=:nodeId", "nodeId", id, null,null);
		if( hpcConnects != null && !hpcConnects.isEmpty() )
		{
			for(HpcConnect hpcConnect : hpcConnects)
			{
				if( hpcConnect.getHpcConnectType() != null )
				{
					Connect metaConnect = copyConnectFromDb(null,hpcConnect);
					node.addConnect(metaConnect);
				}
			}
		}
		List<HpcPropertyAssign> hpcPropertyAssigns = this.query(HpcPropertyAssign.class, "from HpcPropertyAssign pa left join fetch pa.hpcProperty where pa.hpcNode.hpcNodeId=:nodeId", "nodeId",id,null,null);
		if( hpcPropertyAssigns != null && !hpcPropertyAssigns.isEmpty() )
		{
			for(HpcPropertyAssign hpcPropertyAssign : hpcPropertyAssigns)
			{
				if( hpcPropertyAssign.getHpcProperty() != null )
				{
					PropertyValue propertyValue = copyPropertyValueFromDb(null,hpcPropertyAssign);
					node.addProperty(propertyValue);
				}
			}
		}
		
		List<HpcParamAssign> hpcParamAssigns = this.query(HpcParamAssign.class, "from HpcParamAssign pa left join fetch pa.hpcParameter where pa.hpcNode.hpcNodeId=:nodeId", "nodeId", id, null, null);
		if( hpcParamAssigns != null && !hpcParamAssigns.isEmpty() )
		{
			for( HpcParamAssign hpcParamAssign : hpcParamAssigns)
			{
				if( hpcParamAssign.getHpcParameter() != null )
				{
					ParameterValue parameterValue = copyParameterValueFromDb(null,hpcParamAssign);
					node.addParameter(parameterValue);
				}
			}
		}
		
		List<HpcNodeRoleAlloc> hpcNodeRoleAllocs = this.query(HpcNodeRoleAlloc.class, "from HpcNodeRoleAlloc na left join fetch na.hpcNodeRole where na.hpcNode.hpcNodeId=:nodeId", "nodeId",id, null, null);
		if( hpcNodeRoleAllocs != null && !hpcNodeRoleAllocs.isEmpty() )
		{
			for( HpcNodeRoleAlloc hpcNodeRoleAlloc : hpcNodeRoleAllocs )
			{
				if( hpcNodeRoleAlloc.getHpcNodeRole() != null )
				{
					NodeRoleAlloc nodeRoleAlloc = copyNodeRoleAllocFromDb(null,hpcNodeRoleAlloc);
					node.addRole(nodeRoleAlloc);				
				}
			}
		}

		List<HpcInterface> hpcInterfaces = this.query(HpcInterface.class, "from HpcInterface i where i.hpcNode.hpcNodeId=:nodeId", "nodeId",id, null, null);
		if( hpcInterfaces != null && !hpcInterfaces.isEmpty() )
		{
			for(HpcInterface hpcInterface : hpcInterfaces)
			{
				Interface metaInterface = copyInterfaceFromDb(null,hpcInterface);
				node.addIfc(metaInterface);
			}
		}
		return node;
	}

	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveNode(Long clusterId, Node node) throws Exception
	{
		HpcCluster hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c where c.hpcClusterId=:clusterId","clusterId",clusterId);
		if( hpcCluster == null )
		{
			throw new Exception(String.format("The cluster is not found for %d",clusterId));
		}

		HpcNode hpcNode = null;
		if( node.getId() != null && node.getId() > 0 )
		{
			String hql = "from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId and n.hpcNodeId=:nodeId";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("clusterId", clusterId);
			map.put("nodeId", node.getId());
			hpcNode = this.fetch(HpcNode.class,hql,map);
			if( hpcNode == null )
			{
				throw new Exception(String.format("The node %d was not found",node.getId()));
			}
			String[] hs = new String[] {
					"delete HpcInterface i where i.hpcNode.hpcNodeId=:nodeId",
					"delete HpcConnect c where c.hpcNode.hpcNodeId=:nodeId",
					"delete HpcPropertyAssign pa where pa.hpcNode.hpcNodeId=:nodeId",
					"delete HpcParamAssign pa where pa.hpcNode.hpcNodeId=:nodeId",
					"delete HpcNodeRoleAlloc ra where ra.hpcNode.hpcNodeId=:nodeId",
			};
			for(String h : hs)
			{
				this.execute(h, "nodeId", node.getId());
			}
		}
		hpcNode = copyNodeToDb(hpcNode,node);
		hpcNode.setHpcCluster(hpcCluster);
		this.save(HpcNode.class, hpcNode);

		// interfaces
		List<Interface> ifcs = node.getIfcs();
		if( ifcs != null && !ifcs.isEmpty() )
		{
			for(Interface ifc : ifcs)
			{
				HpcInterface hpcIfc = copyInterfaceToDb(null,ifc);
				hpcIfc.setHpcNode(hpcNode);
				this.save(HpcInterface.class, hpcIfc);
			}
		}
		
		// connects
		List<Connect> conns = node.getConnects();
		if( conns != null && !conns.isEmpty() )
		{
			for(Connect conn : conns)
			{
				ConnectType connectType = conn.getType();
				HpcConnectType hpcConnectType = null;
				if( connectType != null )
				{
					String hql = "from HpcConnectType ct where ct.hpcConnTypeCode=:code";
					hpcConnectType = this.fetch(HpcConnectType.class, hql, "code", connectType.getCode());
					if( hpcConnectType == null )
					{
						hpcConnectType = copytConnectTypeToDb(null,connectType);
						this.save(HpcConnectType.class, hpcConnectType);
					}
				}
				
				HpcConnect hpcConnect = copyConnectToDb(null,conn);
				hpcConnect.setHpcNode(hpcNode);
				hpcConnect.setHpcConnectType(hpcConnectType);
				this.save(HpcConnect.class, hpcConnect);
			}
		}

		// properties
		List<PropertyValue> properties = node.getProperties();
		if( properties != null && !properties.isEmpty() )
		{
			for(PropertyValue property : properties)
			{
				HpcProperty hpcProperty = this.fetch(HpcProperty.class,"from HpcProperty p where p.hpcPropName=:name","name",property.getName());
				if( hpcProperty == null )
				{
					hpcProperty = copyPropertyToDb(null,property);
					this.save(HpcProperty.class, hpcProperty);
				}
				HpcPropertyAssign pa = copyPropertyValueToDb(null,property);
				pa.setHpcNode(hpcNode);
				pa.setHpcProperty(hpcProperty);
				this.save(HpcPropertyAssign.class, pa);
			}
		}
		
		// parameters
		List<ParameterValue> parameters = node.getParameters();
		if( parameters != null && !parameters.isEmpty() )
		{
			for(ParameterValue parameter : parameters)
			{
				HpcParameter hpcParameter = this.fetch(HpcParameter.class,"from HpcParameter p where p.hpcParamName=:name","name",parameter.getName());
				if( hpcParameter == null )
				{
					hpcParameter = copyParameterToDb(null,parameter);
					this.save(HpcParameter.class, hpcParameter);
				}
				HpcParamAssign pa = copyParameterValueToDb(null,parameter);
				pa.setHpcNode(hpcNode);
				pa.setHpcParameter(hpcParameter);
				this.save(HpcParamAssign.class, pa);
			}
		}
		
		// roles
		List<NodeRoleAlloc> roles = node.getRoles();
		if( roles != null && !roles.isEmpty() )
		{
			for(NodeRoleAlloc role : roles)
			{
				HpcNodeRole hpcNodeRole = this.fetch(HpcNodeRole.class,"from HpcNodeRole r where r.hpcNodeRoleCode=:code","code",role.getRoleCode());
				if( hpcNodeRole == null )
				{
					hpcNodeRole = copyNodeRoleToDb(null,role.getRole());
					this.save(HpcNodeRole.class, hpcNodeRole);
				}
				if( role.getName() == null )
				{
					role.setName(hpcNodeRole.getHpcNodeRoleName());
				}
				if( role.getDescription() == null )
				{
					role.setDescription(hpcNodeRole.getHpcNodeRoleDesc());
				}
				role.setTime(new Date());
				role.setPriority(0);
				HpcNodeRoleAlloc ra = copyNodeRoleAllocToDb(null,role);
				ra.setHpcNode(hpcNode);
				ra.setHpcNodeRole(hpcNodeRole);
				this.save(HpcNodeRoleAlloc.class, ra);
			}
		}
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeNode(Long clusterId, Long nodeId) throws Exception
	{
		String hql = "from HpcNode n where n.hpcCluster.hpcClusterId=:clusterId and n.hpcNodeId=:nodeId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("nodeId", nodeId);
		HpcNode hpcNode = this.fetch(HpcNode.class, hql, map);
		if( hpcNode == null )
		{
			throw new Exception(String.format("The node is not found for %d",nodeId));
		}
		
		// remove node's content
		String[] rhs = new String[] {
				"delete HpcInterface i where i.hpcNode.hpcNodeId=:nodeId",
				"delete HpcConnect c where c.hpcNode.hpcNodeId=:nodeId",
				"delete HpcPropertyAssign pa where pa.hpcNode.hpcNodeId=:nodeId",
				"delete HpcParamAssign pa where pa.hpcNode.hpcNodeId=:nodeId",
				"delete HpcNodeRoleAlloc ra where ra.hpcNode.hpcNodeId=:nodeId",
		};
		for(String h : rhs)
		{
			this.execute(h, "nodeId", nodeId);
		}
		
		// update rms
		String[] uhs = new String[] {
			"update HpcRm rm set rm.hpcNodeByHpcRmControll = null where rm.hpcNodeByHpcRmControll.hpcNodeId=:nodeId",
			"update HpcRm rm set rm.hpcNodeByHpcRmSchedule = null where rm.hpcNodeByHpcRmSchedule.hpcNodeId=:nodeId",
			"update HpcRm rm set rm.hpcNodeByHpcRmAccount = null where rm.hpcNodeByHpcRmAccount.hpcNodeId=:nodeId",
			"update HpcRm rm set rm.hpcNodeByHpcRmStorage = null where rm.hpcNodeByHpcRmStorage.hpcNodeId=:nodeId",			
		};
		for(String h : uhs)
		{
			this.execute(h, "nodeId", nodeId);
		}
				
		// remove the node itself
		this.delete(hpcNode);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Rm> queryAllRms() throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcRm rm");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmControll");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmAccount");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmStorage");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmSchedule");
		buffer.append(" left join fetch rm.hpcCluster");
//		if( clusterId != null && clusterId > 0 )
//		{
//			buffer.append(" where rm.hpcCluster.hpcClusterId=:clusterId");
//			map.put("clusterId", clusterId);
//		}
		buffer.append(" order by rm.hpcRmName,rm.hpcRmVersion ASC");
		String hql = buffer.toString();
		List<HpcRm> hpcRms = this.query(HpcRm.class, hql, map, null, null);
		List<Rm> rms = new ArrayList<Rm>();
		for(HpcRm hpcRm : hpcRms)
		{
			HpcNode control = hpcRm.getHpcNodeByHpcRmControll();
			HpcNode account = hpcRm.getHpcNodeByHpcRmAccount();
			HpcNode storage = hpcRm.getHpcNodeByHpcRmStorage();
			HpcNode schedule = hpcRm.getHpcNodeByHpcRmSchedule();
			HpcCluster cluster = hpcRm.getHpcCluster();
			
			if( cluster == null )
			{
				continue;
			}

			Rm rm = copyRmFromDb(null,hpcRm);
			// store cluster's info.
			rm.setClusterId(cluster.getHpcClusterId());
			rm.setClusterName(cluster.getHpcClusterName());
			
			if( control != null )
			{
				Node node = copyNodeFromDb(null,control);
				rm.setController(node);
			}
			if( account != null )
			{
				Node node = copyNodeFromDb(null,account);
				rm.setAccount(node);
			}
			if( storage != null )
			{
				Node node = copyNodeFromDb(null,storage);
				rm.setStorage(node);
			}
			if( schedule != null )
			{
				Node node = copyNodeFromDb(null,schedule);
				rm.setSchedule(node);
			}
			rms.add(rm);
		}
		return rms;
	}

	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countRms(Long clusterId,String name) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcRm rm");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" rm.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" rm.hpcRmName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Rm> queryRms(Long clusterId,String name,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcRm rm");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" rm.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" rm.hpcRmName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		buffer.append(" order by rm.hpcRmName ASC");
		String hql = buffer.toString();
		List<HpcRm> hpcRms = this.query(HpcRm.class, hql, map, pageNo, pageSize);
		List<Rm> metaRms = new ArrayList<Rm>();
		for(HpcRm hpcRm : hpcRms)
		{
			Rm rm = copyRmFromDb(null,hpcRm);
			rm.setClusterId(clusterId);
			metaRms.add(rm);
		}
		return metaRms;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public Rm fetchRm(Long clusterId,String version) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcRm rm");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmControll");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmAccount");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmStorage");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmSchedule");
		buffer.append(" left join fetch rm.hpcCluster");
		buffer.append(" where rm.hpcCluster.hpcClusterId=:clusterId and rm.hpcRmVersion=:version");
		String hql = buffer.toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("version", version);
		HpcRm hpcRm = this.fetch(HpcRm.class,hql,map);
		if( hpcRm == null )
		{
			throw new Exception(String.format("The RM not found for version %s",version));
		} 
	
		HpcNode hpcAccount = hpcRm.getHpcNodeByHpcRmAccount();
		HpcNode hpcController = hpcRm.getHpcNodeByHpcRmControll();
		HpcNode hpcStorage = hpcRm.getHpcNodeByHpcRmStorage();
		HpcNode hpcSchedule = hpcRm.getHpcNodeByHpcRmSchedule();
		
		Rm rm = copyRmFromDb(null,hpcRm);
		if( hpcAccount != null )
		{
			Node account = copyNodeFromDb(null,hpcAccount);
			rm.setAccount(account);
		}
		if( hpcController != null )
		{
			Node controller = copyNodeFromDb(null,hpcController);
			rm.setController(controller);
		}
		if( hpcStorage != null )
		{
			Node storage = copyNodeFromDb(null,hpcStorage);
			rm.setStorage(storage);
		}
		if( hpcSchedule != null )
		{
			Node schedule = copyNodeFromDb(null,hpcSchedule);
			rm.setSchedule(schedule);
		}
		
		return rm;		
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public Rm fetchRm(Long clusterId,Long rmId) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcRm rm");
		buffer.append(" left join fetch rm.hpcNodeByHcRmInstall");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmControll");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmAccount");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmStorage");
		buffer.append(" left join fetch rm.hpcNodeByHpcRmSchedule");
		buffer.append(" left join fetch rm.hpcCluster");
		buffer.append(" where rm.hpcCluster.hpcClusterId=:clusterId and rm.hpcRmId=:rmId");
		String hql = buffer.toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("rmId", rmId);
		HpcRm hpcRm = this.fetch(HpcRm.class,hql,map);
		if( hpcRm == null )
		{
			throw new Exception(String.format("The RM not found for %d",rmId));
		}
		HpcNode hpcInstaller = hpcRm.getHpcNodeByHcRmInstall();
		HpcNode hpcAccount = hpcRm.getHpcNodeByHpcRmAccount();
		HpcNode hpcController = hpcRm.getHpcNodeByHpcRmControll();
		HpcNode hpcStorage = hpcRm.getHpcNodeByHpcRmStorage();
		HpcNode hpcSchedule = hpcRm.getHpcNodeByHpcRmSchedule();
		
		Rm rm = copyRmFromDb(null,hpcRm);
		if( hpcInstaller != null ) {
			Node installer = copyNodeFromDb(null,hpcInstaller);
			rm.setInstaller(installer);
		}
		if( hpcAccount != null )
		{
			Node account = copyNodeFromDb(null,hpcAccount);
			rm.setAccount(account);
		}
		if( hpcController != null )
		{
			Node controller = copyNodeFromDb(null,hpcController);
			rm.setController(controller);
		}
		if( hpcStorage != null )
		{
			Node storage = copyNodeFromDb(null,hpcStorage);
			rm.setStorage(storage);
		}
		if( hpcSchedule != null )
		{
			Node schedule = copyNodeFromDb(null,hpcSchedule);
			rm.setSchedule(schedule);
		}
		
		return rm;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveRm(Long clusterId,Rm rm) throws Exception
	{
		HpcCluster hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c where c.hpcClusterId=:clusterId", "clusterId", clusterId);
		if( hpcCluster == null )
		{
			throw new Exception(String.format("The cluster is not found for %d",clusterId));
		}
		Node installer = rm.getInstaller();
		Node account = rm.getAccount();
		Node controller = rm.getController();
		Node storage = rm.getStorage();
		Node schedule = rm.getSchedule();
		
		HpcNode hpcInstaller = null;
		HpcNode hpcAccount = null;
		HpcNode hpcController = null;
		HpcNode hpcStorage = null;
		HpcNode hpcSchedule = null;
		
		if( installer != null && installer.getId() != null && installer.getId() > 0)
		{
			hpcInstaller = this.fetch(HpcNode.class, "from HpcNode n where n.hpcNodeId=:id", "id", installer.getId());
			if( hpcInstaller == null ) {
				throw new Exception(String.format("The installer's node is not found for %d", installer.getId()));
			}
		}
		
		if( account != null && account.getId() != null && account.getId() > 0 )
		{
			hpcAccount = this.fetch(HpcNode.class, "from HpcNode n where n.hpcNodeId=:id", "id", account.getId());
			if( hpcAccount == null )
			{
				throw new Exception(String.format("The Account's Node is not found for %d",account.getId()));
			}
		}
		
		if( controller != null && controller.getId() != null && controller.getId() > 0 )
		{
			hpcController = this.fetch(HpcNode.class, "from HpcNode n where n.hpcNodeId=:id", "id",controller.getId());
			if( hpcController == null )
			{
				throw new Exception(String.format("The Controller's Node is not found for %d", controller.getId()));
			}
		}
		if( storage != null && storage.getId() != null && storage.getId() > 0 )
		{
			hpcStorage = this.fetch(HpcNode.class, "from HpcNode n where n.hpcNodeId=:id", "id",storage.getId());
			if( hpcStorage == null )
			{
				throw new Exception(String.format("The storage's Node is not found for %d", storage.getId()));
			}
		}
		if( schedule != null && schedule.getId() != null && schedule.getId() > 0 )
		{
			hpcSchedule = this.fetch(HpcNode.class, "from HpcNode n where n.hpcNodeId=:id", "id",schedule.getId());
			if( hpcSchedule == null )
			{
				throw new Exception(String.format("The schedule's Node is not found for %d", schedule.getId()));
			}
		}
		
		HpcRm hpcRm = null;
		if( rm.getId() != null && rm.getId() > 0 )
		{
			String hql = "from HpcRm rm where rm.hpcCluster.hpcClusterId=:clusterId and rm.hpcRmId=:rmId";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("clusterId", clusterId);
			map.put("rmId", rm.getId());
			hpcRm = this.fetch(HpcRm.class,hql,map);
			if( hpcRm == null )
			{
				throw new Exception(String.format("The RM not found for %d",rm.getId()));
			}
		}
		hpcRm = copyRmToDb(hpcRm,rm);
		hpcRm.setHpcCluster(hpcCluster);
		hpcRm.setHpcNodeByHcRmInstall(hpcInstaller);
		hpcRm.setHpcNodeByHpcRmAccount(hpcAccount);
		hpcRm.setHpcNodeByHpcRmControll(hpcController);
		hpcRm.setHpcNodeByHpcRmStorage(hpcStorage);
		hpcRm.setHpcNodeByHpcRmSchedule(hpcSchedule);
		this.save(HpcRm.class, hpcRm);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeRm(Long clusterId,Long rmId) throws Exception
	{
		String hql = "from HpcRm rm where rm.hpcCluster.hpcClusterId=:clusterId and rm.hpcRmId=:rmId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("rmId", rmId);
		HpcRm hpcRm = this.fetch(HpcRm.class,hql,map);
		if( hpcRm == null )
		{
			throw new Exception(String.format("The RM not found for %d",rmId));
		}
		this.delete(hpcRm);
	}
	
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countResources(Long clusterId,String type) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcResource res");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" res.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( type != null && !type.isEmpty() )
		{
			buffer.append( map.isEmpty()?" where":" and");
			buffer.append(" res.hpcResourceType like :type");
			map.put("type", String.format("%%%s%%", type));
		}
		String hql = buffer.toString();
		return this.count(hql, map);	
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Resource> queryResources(Long clusterId,String type,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcResource res");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" res.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( type != null && !type.isEmpty() )
		{
			buffer.append( map.isEmpty()?" where":" and");
			buffer.append(" res.hpcResourceType like :type");
			map.put("type", String.format("%%%s%%", type));
		}
		buffer.append(" order by res.hpcResourceType ASC");
		String hql = buffer.toString();
		List<HpcResource> hpcResources = this.query(HpcResource.class, hql, map, pageNo, pageSize);
		List<Resource> metaResources = new ArrayList<Resource>();
		for(HpcResource hpcResource:hpcResources)
		{
			Resource metaResource = copyResourceFromDb(null,hpcResource);
			metaResources.add(metaResource);
		}
		return metaResources;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveResource(Long clusterId,Resource res) throws Exception
	{
		HpcCluster hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c where c.hpcClusterId=:id", "id",clusterId);
		if( hpcCluster == null )
		{
			throw new Exception(String.format("The cluster's ID is not found for %d",clusterId));
		}
		HpcResource hpcResource = null;
		if( res.getId() != null && res.getId() > 0 )
		{
			String hql = "from HpcResource res where res.hpcCluster.hpcClusterId=:clusterId and res.hpcResourceId=:resId";
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("clusterId", clusterId);
			map.put("resId", res.getId());
			hpcResource = this.fetch(HpcResource.class, hql, map);
			if( hpcResource == null )
			{
				throw new Exception(String.format("The resource's ID is not found for %d",res.getId()));
			}
		}
		
		hpcResource = copyResourceToDb(hpcResource,res);
		hpcResource.setHpcCluster(hpcCluster);
		this.save(HpcResource.class, hpcResource);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Resource fetchResource(Long clusterId,Long resId) throws Exception
	{
		String hql = "from HpcResource res where res.hpcCluster.hpcClusterId=:clusterId and res.hpcResourceId=:resId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("resId", resId);
		HpcResource hpcResource = this.fetch(HpcResource.class, hql, map);
		if( hpcResource == null )
		{
			throw new Exception(String.format("The resource's ID is not found for %d",resId));
		}
		return copyResourceFromDb(null,hpcResource);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeResource(Long clusterId,Long resId) throws Exception
	{
		String hql = "from HpcResource res where res.hpcCluster.hpcClusterId=:clusterId and res.hpcResourceId=:resId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("resId", resId);
		HpcResource hpcResource = this.fetch(HpcResource.class, hql, map);
		if( hpcResource == null )
		{
			throw new Exception(String.format("The resource's ID is not found for %d",resId));
		}
		this.delete(hpcResource);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudGroup> queryGroups(Long userId) throws Exception
	{
		String hql = "from HpcGroupAlloc ga left join fetch ga.hpcGroup where ga.hpcCloudUser.hpcUserId=:userId";
		List<HpcGroupAlloc> list = this.query(HpcGroupAlloc.class, hql,"userId",userId, null, null);
		List<CloudGroup> groups = new ArrayList<CloudGroup>();
		if( list != null && !list.isEmpty() )
		{
			for(HpcGroupAlloc ga : list)
			{
				HpcGroup hpcGroup = ga.getHpcGroup();
				if( hpcGroup == null )
				{
					continue;
				}
				CloudGroup group = copyGroupFromDb(null,hpcGroup);
				groups.add(group);
			}
		}
		return groups;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudGroup> querySubGroups(Long groupId) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcGroup g where");
		Map<String,Object> map = new HashMap<String,Object>();
		if( groupId == null || groupId <= 0 ) {
			buffer.append(" g.hpcGroup is null");
		}
		else
		{
			buffer.append(" g.hpcGroup.hpcGroupId=:groupId");
			map.put("groupId", groupId);
		}
		String hql = buffer.toString();
		List<HpcGroup> hpcGroups = this.query(HpcGroup.class, hql, map, null, null);
		List<CloudGroup> groups = new ArrayList<CloudGroup>();
		if( hpcGroups != null && !hpcGroups.isEmpty() )
		{
			for(HpcGroup hpcGroup : hpcGroups)
			{
				CloudGroup group = copyGroupFromDb(null,hpcGroup);
				groups.add(group);
			}
		}
		return groups;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudGroup> queryGroupsForTree() throws Exception
	{
		List<CloudGroup> list = new ArrayList<CloudGroup>();
		
		String hql = "from HpcGroup g left join fetch g.hpcGroup order by g.hpcGroupId";
		List<HpcGroup> hpcGroups = this.query(HpcGroup.class, hql, null, null, null);
		if( hpcGroups!=null && !hpcGroups.isEmpty() )
		{
			for(HpcGroup hpcGroup : hpcGroups)
			{
				CloudGroup group = copyGroupFromDb((CloudGroup)null,hpcGroup);
				list.add(group);
			}
		}
		
		return list;
	}


	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countGroups(String name) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from HpcGroup g");
		Map<String,Object> map = new HashMap<String,Object>();
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" g.hpcGroupName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudGroup> queryGroups(String name,Integer pageNo,Integer pageSize) throws Exception
	{
		List<CloudGroup> list = new ArrayList<CloudGroup>();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcGroup g left join fetch g.hpcGroup");
		Map<String,Object> map = new HashMap<String,Object>();
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" g.hpcGroupName like :name");
			map.put("name", String.format("%%%s%%",name));
		}
		buffer.append(" order by g.hpcGroupId");
		String hql = buffer.toString();
		List<HpcGroup> hpcGroups = this.query(HpcGroup.class, hql, map, null, null);
		if( hpcGroups!=null && !hpcGroups.isEmpty() )
		{
			for(HpcGroup hpcGroup : hpcGroups)
			{
				CloudGroup group = copyGroupFromDb((CloudGroup)null,hpcGroup);
				HpcGroup hpcParent = hpcGroup.getHpcGroup();
				if( hpcParent != null ) {
					group.setParentId(hpcParent.getHpcGroupId());
					group.setParentName(hpcParent.getHpcGroupName());
				}
				else
				{
					group.setParentId(0L);
					group.setParentName("n/a");
				}
				list.add(group);
			}
		}
		
		return list;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeGroup(Long groupId) throws Exception
	{
		// remove the group's children
		String h1 = "from HpcGroup g where g.hpcGroup.hpcGroupId=:groupId";
		List<HpcGroup> list = this.query(HpcGroup.class, h1, "groupId", groupId, null, null);
		if( list != null && !list.isEmpty() )
		{
			for(HpcGroup one : list) {
				removeGroup(one.getHpcGroupId());
			}
		}
		
		String[] hqls = new String[] {
			// remove the group's price
			"delete HpcJobPrice p where p.hpcGroup.hpcGroupId=:groupId",
			// remove the group's allocation
			"delete HpcGroupAlloc ga where ga.hpcGroup.hpcGroupId=:groupId",
			// remove the group itself
			"delete HpcGroup g where g.hpcGroupId=:groupId"
		};
		for(String hql : hqls)
		{
			this.execute(hql, "groupId", groupId);
		}
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveGroup(CloudGroup group) throws Exception
	{
		Long groupId = group.getId();

		// check whether the group's name is duplication
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcGroup g where g.hpcGroupName=:name");
		map.put("name", group.getName());
		if( group.getId() != null && groupId > 0 ) {
			buffer.append(" and g.hpcGroupId!=:groupId");
			map.put("groupId", groupId);
		}
		String checkHql = buffer.toString();
		int count = this.count(checkHql, map);
		if( count > 0 ) {
			throw new Exception("The group's name is duplication");
		}
		
		// first, fetching the group object if groupId is valid
		String fetchHql = "from HpcGroup g where g.hpcGroupId=:groupId";
		HpcGroup hpcGroup = null;
		HpcGroup hpcParent = null;
		if( groupId != null && groupId > 0 ) {
			hpcGroup = this.fetch(HpcGroup.class, fetchHql, "groupId", groupId);
			if( hpcGroup == null ) {
				throw new Exception("The group not found.");
			}
			this.execute("delete HpcGroupAlloc ga where ga.hpcGroup.hpcGroupId=:groupId","groupId",groupId);
		}
		
		// second, try to fetch parent object
		Long parentId = group.getParentId();
		if( parentId != null && parentId > 0 )
		{
			hpcParent = this.fetch(HpcGroup.class, fetchHql, "groupId", parentId);
			if( hpcParent == null ) {
				throw new Exception("The group's parent not found.");
			}
		}
		hpcGroup = copyGroupToDb(hpcGroup,group);
		hpcGroup.setHpcGroup(hpcParent);
		this.save(HpcGroup.class, hpcGroup);
		
		// third, save all allocations
		List<CloudUser> users = group.getUsers();
		if( users != null && !users.isEmpty() ) {
			List<Long> ids = new ArrayList<Long>();
			for(CloudUser user : users) {
				Long id = user.getId();
				if( id != null && id > 0 ) {
					ids.add(id);
				}
			}
			String h = "from HpcCloudUser u where u.hpcUserId in (:ids)";
			List<HpcCloudUser> hpcCloudUsers = this.query(HpcCloudUser.class, h, "ids", ids, null, null);
			for(HpcCloudUser hpcCloudUser : hpcCloudUsers)
			{
				HpcGroupAlloc ga = new HpcGroupAlloc();
				ga.setHpcCloudUser(hpcCloudUser);
				ga.setHpcGroup(hpcGroup);
				this.save(HpcGroupAlloc.class, ga);
			}
		}
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public CloudGroup fetchGroup(Long groupId) throws Exception
	{
		String hql = "from HpcGroup g left join fetch g.hpcGroup where g.hpcGroupId=:groupId";
		HpcGroup hpcGroup = this.fetch(HpcGroup.class, hql, "groupId", groupId);
		if( hpcGroup == null ) {
			return null;
		}
		CloudGroup cloudGroup = copyGroupFromDb(null,hpcGroup);
		HpcGroup hpcParent = hpcGroup.getHpcGroup();
		if( hpcParent != null )
		{
			cloudGroup.setParentId(hpcParent.getHpcGroupId());
			cloudGroup.setParentName(hpcParent.getHpcGroupName());
		}
		return cloudGroup;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudUser> queryAllPayingUsers() throws Exception
	{
		String hql = "from HpcCloudUser u where u.hpcCloudUser is null and u.hpcUserName!=:root order by u.hpcUserName";
		List<HpcCloudUser> hpcUsers = this.query(HpcCloudUser.class,hql,"root","root",null,null);
		List<CloudUser> users = new ArrayList<CloudUser>();
		if( hpcUsers!=null && !hpcUsers.isEmpty() )
		{
			for( HpcCloudUser hpcUser : hpcUsers )
			{
				CloudUser user = copyUserFromDb(null,hpcUser);
				users.add(user);
			}
		}
		return users;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void setpwd(Long userId,String password) throws Exception
	{
		String hql = "update HpcCloudUser u set u.hpcUserPasswd=:password where u.hpcUserId=:userId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("password", password);
		map.put("userId", userId);
		int count = this.execute(hql, map);
		if( count == 0 )
		{
			throw new Exception("User is not found.");
		}
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countUsers(String username) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from HpcCloudUser u where u.hpcUserName != :root");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("root", "root");
		if( username != null && !username.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" u.hpcUserName like :username");
			map.put("username", String.format("%%%s%%",username));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}

	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudUser> queryUsers(Long groupId) throws Exception
	{
		String hql = "from HpcGroupAlloc ga left join fetch ga.hpcCloudUser where ga.hpcGroup.hpcGroupId=:groupId";
		List<HpcGroupAlloc> gas = this.query(HpcGroupAlloc.class, hql,"groupId",groupId,null,null);
		List<CloudUser> users = new ArrayList<CloudUser>();
		if( gas != null && !gas.isEmpty() )
		{
			for( HpcGroupAlloc ga : gas )
			{
				HpcCloudUser hpcUser = ga.getHpcCloudUser();
				if( hpcUser== null )
				{
					continue;
				}
				CloudUser user = copyUserFromDb(null,hpcUser);
				users.add(user);
			}
 		}
		return users;
	}
	/**
	 * 
	 * @param username
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<CloudUser> queryUsers(String username,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcCloudUser u left join fetch u.hpcCloudUser where u.hpcUserName != :root");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("root", "root");
		if( username != null && !username.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" u.hpcUserName like :username");
			map.put("username", String.format("%%%s%%",username));
		}
		buffer.append(" order by u.hpcUserName");
		
		String hql = buffer.toString();
		List<HpcCloudUser> hpcCloudUsers = this.query(HpcCloudUser.class, hql, map, pageNo, pageSize);
		List<CloudUser> list = new ArrayList<CloudUser>();
		for(HpcCloudUser hpcCloudUser : hpcCloudUsers)
		{
			CloudUser user = copyUserFromDb(null,hpcCloudUser);
			HpcCloudUser askTo = hpcCloudUser.getHpcCloudUser();
			if( askTo != null )
			{
				user.setAskTo(askTo.getHpcUserId());
				user.setAskToName(askTo.getHpcUserName());
			}
			else
			{
				user.setAskTo(null);
				user.setAskToName("n/a");
			}
			list.add(user);
		}
		
		return list;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public CloudUser fetchUser(String username) throws Exception
	{
		String hql = "from HpcCloudUser u left join fetch u.hpcCloudUser where u.hpcUserName=:username";
		HpcCloudUser hpcCloudUser = this.fetch(HpcCloudUser.class, hql, "username",username);
		if( hpcCloudUser == null )
		{
			throw new Exception("The user is not found.");
		}
		CloudUser cloudUser = copyUserFromDb(null,hpcCloudUser);
		if( hpcCloudUser.getHpcCloudUser() != null )
		{
			HpcCloudUser askto = hpcCloudUser.getHpcCloudUser();
			cloudUser.setAskTo(askto.getHpcUserId());
			cloudUser.setAskToName(askto.getHpcUserName());
		}
		else
		{
			cloudUser.setAskTo(null);
			cloudUser.setAskToName("n/a");
		}
		
		return cloudUser;		
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public CloudUser fetchUser(Long userId) throws Exception
	{
		String hql = "from HpcCloudUser u left join fetch u.hpcCloudUser where u.hpcUserId=:userId";
		HpcCloudUser hpcCloudUser = this.fetch(HpcCloudUser.class, hql, "userId",userId);
		if( hpcCloudUser == null )
		{
			throw new Exception("The user is not found.");
		}
		CloudUser user = copyUserFromDb(null,hpcCloudUser);
		if( hpcCloudUser.getHpcCloudUser() != null )
		{
			HpcCloudUser askto = hpcCloudUser.getHpcCloudUser();
			user.setAskTo(askto.getHpcUserId());
			user.setAskToName(askto.getHpcUserName());
		}
		else
		{
			user.setAskTo(null);
			user.setAskToName("n/a");
		}

		return user;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeUser(Long userId) throws Exception
	{
		// 
		String[] hqls = new String[] {
				// delete records in HPC_RES_LTD
				"delete HpcResLtd ltd where ltd.hpcClusterUser.hpcClstrUsrId in (select u.hpcClstrUsrId from HpcClusterUser u where u.hpcCloudUser.hpcUserId=:userId)",
				// delete records in HPC_PAYMENT
				"delete HpcPayment pay where pay.hpcClusterUser.hpcClstrUsrId in (select u.hpcClstrUsrId from HpcClusterUser u where u.hpcCloudUser.hpcUserId=:userId)",
				// delete records in HPC_JOB
				"delete HpcJob job where job.hpcClusterUser.hpcClstrUsrId in (select u.hpcClstrUsrId from HpcClusterUser u where u.hpcCloudUser.hpcUserId=:userId)",
				// clean user's payee
				"update HpcCloudUser u set u.hpcCloudUser=null where u.hpcCloudUser.hpcUserId=:userId",
				// delete invalid job records
				"delete HpcJob job where job.hpcCloudUserByHpcHobPaidUser.hpcUserId=:userId",
				"delete HpcJob job where job.hpcCloudUserByHpcJobSubmitUser.hpcUserId=:userId",
				// delete records in HPC_CLUSTER_USER
				"delete HpcClusterUser cu where cu.hpcCloudUser.hpcUserId=:userId",
				// delete records in HPC_JOB_PRICE
				"delete HpcJobPrice p where p.hpcCloudUser.hpcUserId=:userId",
				// delete records in HPC_ROLE_ALLOC
				"delete HpcRoleAlloc ra where ra.hpcCloudUser.hpcUserId=:userId",
				// delete records in HPC_GROUP_ALLOC
				"delete HpcGroupAlloc ga where ga.hpcCloudUser.hpcUserId=:userId",
				// delete the user itself
				"delete HpcCloudUser u where u.hpcUserId=:userId"
		};
		
		for(String hql : hqls)
		{
			System.out.println(hql);
			this.execute(hql,"userId",userId);
		}
	}

	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveUser(CloudUser user) throws Exception
	{
		Long userId = user.getId();
		
		// check whether the user's name is duplication
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcCloudUser u where u.hpcUserName=:name");
		map.put("name", user.getName());
		if( userId != null && userId > 0 ) {
			buffer.append(" and u.hpcUserId!=:userId");
			map.put("userId", userId);
		}
		String checkHql = buffer.toString();
		int count = this.count(checkHql, map);
		if( count > 0 ) {
			throw new Exception("The user's name is duplication.");
		}
		
		// initialize user
		HpcCloudUser hpcCloudUser = null;
		if( userId != null && userId > 0 )
		{
			hpcCloudUser = this.fetch(HpcCloudUser.class, "from HpcCloudUser u where u.hpcUserId=:userId", "userId",user.getId());
			if( hpcCloudUser == null )
			{
				throw new Exception("The user is not found.");
			}
			// remove all existing group's allocations
			String hql = "delete HpcGroupAlloc ga where ga.hpcCloudUser.hpcUserId=:userId";
			this.execute(hql, "userId", user.getId());
		}
		else
		{
			hpcCloudUser = new HpcCloudUser();
			hpcCloudUser.setHpcUserPasswd(user.getPassword());
			hpcCloudUser.setHpcUserBalance(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserCharged(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserConsumed(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserJobFee(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserJobNum(0L);
			hpcCloudUser.setHpcUserJobHosts(0L);
			hpcCloudUser.setHpcUserJobCores(0L);
			hpcCloudUser.setHpcUserJobTime(new Date());
			hpcCloudUser.setHpcUserStorageFee(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserStorageCap(BigDecimal.ZERO);
			hpcCloudUser.setHpcUserStorageUnit("B");
			hpcCloudUser.setHpcUserStorageTime(new Date());
		}
		hpcCloudUser = copyUserToDb(hpcCloudUser,user);
		
		// fetch askto's user
		Long askToId = user.getAskTo();
		HpcCloudUser askTo = null;
		if( askToId != null && askToId > 0 )
		{
			askTo = this.fetch(HpcCloudUser.class, "from HpcCloudUser u left join fetch u.hpcCloudUser where u.hpcUserId=:id","id",askToId);
			if( askTo == null )
			{
				throw new Exception("The paying user not exist.");
			}
			if( askTo.getHpcCloudUser() != null )
			{
				throw new Exception(String.format("用户[%s]不能作为当前用户的付费用户！",askTo.getHpcUserName()));
			}
		}
		hpcCloudUser.setHpcCloudUser(askTo);
		this.save(HpcCloudUser.class, hpcCloudUser);
		
		// save group allocations
		List<CloudGroup> groups = user.getGroups();
		if( groups!=null && !groups.isEmpty() )
		{
			String hql = "from HpcGroup g where g.hpcGroupId=:groupId";
			for(CloudGroup group:groups)
			{
				Long groupId = group.getId();
				HpcGroup hpcGroup = this.fetch(HpcGroup.class, hql, "groupId", groupId);
				if( hpcGroup == null ) {
					throw new Exception("Group not found for "+groupId);
				}
				HpcGroupAlloc ga = new HpcGroupAlloc();
				ga.setHpcCloudUser(hpcCloudUser);
				ga.setHpcGroup(hpcGroup);
				this.save(HpcGroupAlloc.class,ga);
			}
		}
	}

	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countRoles(String name) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcRole r");
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" r.hpcRoleName like :name");
			map.put("name", String.format("%%%s%%", name));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<Role> queryRoles(String name,Integer pageNo,Integer pageSize) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcRole r");
		if( name != null && !name.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" r.hpcRoleName like :name");
			map.put("name", String.format("%%%s%%", name));
		}
		buffer.append(" order by r.hpcRoleName ASC");
		String hql = buffer.toString();
		List<HpcRole> hpcRoles = this.query(HpcRole.class, hql, map, pageNo, pageSize);
		List<Role> roles = new ArrayList<Role>();
		for( HpcRole hpcRole : hpcRoles )
		{
			Role role = copyRoleFromDb(null,hpcRole);
			roles.add(role);
		}
		return roles;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public Role fetchRole(Long roleId) throws Exception
	{
		String hql1 = "from HpcRole r where r.hpcRoleId=:roleId";
		HpcRole hpcRole = this.fetch(HpcRole.class, hql1, "roleId", roleId);
		if( hpcRole == null )
		{
			throw new Exception("The role is not found.");
		}
		Role role = copyRoleFromDb(null,hpcRole);
		
		return role;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveRole(Role role) throws Exception
	{
		HpcRole hpcRole = null;
		if( role.getId() != null && role.getId() > 0 )
		{
			String h = "from HpcRole r where r.hpcRoleId=:roleId";
			hpcRole = this.fetch(HpcRole.class, h, "roleId",role.getId());
			if( hpcRole == null )
			{
				throw new Exception("The role is not found.");
			}
		}
		hpcRole = copyRoleToDb(hpcRole,role);
		this.save(HpcRole.class, hpcRole);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeRole(Long roleId) throws Exception
	{
		HpcRole hpcRole = this.fetch(HpcRole.class, "from HpcRole r where r.hpcRoleId=:roleId", "roleId",roleId);
		if( hpcRole == null )
		{
			throw new Exception("The role is not found");
		}
		String hql = "delete HpcRoleAlloc ra where ra.hpcRole.hpcRoleId=:roleId";
		this.execute(hql, "roleId", roleId);
		this.delete(hpcRole);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public int countClusterUsers(Long clusterId,String username)
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("select count(*) from HpcClusterUser cu");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" cu.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( username != null && !username.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" cu.hpcClstrUsrName=:username");
			map.put("username", String.format("%%%s%%", username));
		}
		String hql = buffer.toString();
		return this.count(hql, map);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<ClusterUser> queryClusterUsers(Long clusterId,String username,Integer pageNo,Integer pageSize)
	{
		StringBuffer buffer = new StringBuffer();
		Map<String,Object> map = new HashMap<String,Object>();
		buffer.append("from HpcClusterUser cu left join fetch cu.hpcCloudUser");
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" cu.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		if( username != null && !username.isEmpty() )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" cu.hpcClstrUsrName=:username");
			map.put("username", String.format("%%%s%%", username));
		}
		String hql = buffer.toString();
		List<HpcClusterUser> hpcClusterUsers = this.query(HpcClusterUser.class, hql, map, pageNo, pageSize);
		List<ClusterUser> clusterUsers = new ArrayList<ClusterUser>();
		for(HpcClusterUser hpcClusterUser:hpcClusterUsers)
		{
			HpcCloudUser hpcCloudUser = hpcClusterUser.getHpcCloudUser();
			ClusterUser clusterUser = copyClusterUserFromDb(null,hpcClusterUser);
			if( hpcCloudUser != null )
			{
				clusterUser.setUserId(hpcCloudUser.getHpcUserId());
				clusterUser.setUserName(hpcCloudUser.getHpcUserName());
			}
			clusterUsers.add(clusterUser);
		}
		return clusterUsers;
	}
	
	/**\
	 * Fetch cluster user by username in the cluster
	 * @param clusterId
	 *     Cluster primary key
	 * @param username
	 *     user's login name
	 * @return
	 *     Cluster user mapped in the cluster, if a null, no mapping cluster user was found.
	 * @throws Exception
	 */
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public ClusterUser fetchClusterUser(Long clusterId,String username) throws Exception
	{
		String hql = "from HpcClusterUser u left join fetch u.hpcCloudUser where u.hpcCluster.hpcClusterId=:clusterId and u.hpcCloudUser.hpcUserName=:username";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("username", username);
		HpcClusterUser hpcClusterUser = this.fetch(HpcClusterUser.class, hql, map);
		if( hpcClusterUser == null )
		{
			return null;
		}
		ClusterUser clusterUser = copyClusterUserFromDb(null,hpcClusterUser);
		HpcCloudUser hpcCloudUser = hpcClusterUser.getHpcCloudUser();
		if( hpcCloudUser != null )
		{
			clusterUser.setUserId(hpcCloudUser.getHpcUserId());
			clusterUser.setUserName(hpcCloudUser.getHpcUserName());
		}		

		return clusterUser;		
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public ClusterUser fetchClusterUser(Long clusterId,Long clusterUserId) throws Exception
	{
		String hql = "from HpcClusterUser u left join fetch u.hpcCloudUser where u.hpcCluster.hpcClusterId=:clusterId and u.hpcClstrUsrId=:clusterUserId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("clusterUserId", clusterUserId);
		HpcClusterUser hpcClusterUser = this.fetch(HpcClusterUser.class, hql, map);
		if( hpcClusterUser == null )
		{
			throw new Exception("The Cluster User is not found.");
		}
		ClusterUser clusterUser = copyClusterUserFromDb(null,hpcClusterUser);
		HpcCloudUser hpcCloudUser = hpcClusterUser.getHpcCloudUser();
		if( hpcCloudUser != null )
		{
			clusterUser.setUserId(hpcCloudUser.getHpcUserId());
			clusterUser.setUserName(hpcCloudUser.getHpcUserName());
		}		

		return clusterUser;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveClusterUser(Long clusterId,ClusterUser clusterUser) throws Exception
	{
		HpcCluster hpcCluster = this.fetch(HpcCluster.class, "from HpcCluster c where c.hpcClusterId=:id", "id",clusterId);
		if( hpcCluster == null )
		{
			throw new Exception(String.format("The cluster's ID is not found for %d",clusterId));
		}
		Long userId = clusterUser.getUserId();
		HpcCloudUser hpcCloudUser = this.fetch(HpcCloudUser.class, "from HpcCloudUser u where u.hpcUserId=:userId", "userId", userId);
		if( hpcCloudUser == null)
		{
			throw new Exception("The user is not found.");
		}
		HpcClusterUser hpcClusterUser = null;
		if( clusterUser.getId() != null && clusterUser.getId() > 0 )
		{
			hpcClusterUser = this.fetch(HpcClusterUser.class, "from HpcClusterUser u where u.hpcClstrUsrId=:id", "id",clusterUser.getId());
			if( hpcClusterUser == null )
			{
				throw new Exception("The clusterUser is not found.");
			}
		}
		hpcClusterUser = copyClusterUserToDb(hpcClusterUser,clusterUser);
		hpcClusterUser.setHpcCluster(hpcCluster);
		hpcClusterUser.setHpcCloudUser(hpcCloudUser);
		this.save(HpcClusterUser.class, hpcClusterUser);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeClusterUser(Long clusterId,Long clusterUserId) throws Exception
	{
		String hql = "from HpcClusterUser u where u.hpcCluster.hpcClusterId=:clusterId and u.hpcClstrUsrId=:id";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("id", clusterUserId);
		HpcClusterUser hpcClusterUser = this.fetch(HpcClusterUser.class,hql,map);
		if( hpcClusterUser == null )
		{
			throw new Exception("The clusterUser is not found");
		}
		this.delete(hpcClusterUser);
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<PropertyValue> queryProperties(Long clusterId) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcPropertyAssign ha");
		Map<String,Object> map = new HashMap<String,Object>();
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" ha.hpcNode.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId",clusterId);
		}
		buffer.append(" order by ha.hpcPropertyAssignTime asc");
		String hql = buffer.toString();
		List<HpcPropertyAssign> list = this.query(HpcPropertyAssign.class, hql, map, null, null);
		List<PropertyValue> values = new ArrayList<PropertyValue>();
		for(HpcPropertyAssign one : list)
		{
			PropertyValue value = copyPropertyValueFromDb(null,one);
			values.add(value);
		}
		return values;
	}
	
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public String queryClusterProperties(Long clusterId,String propName) throws Exception
	{
		String hql = "from HpcPropertyAssign ha where ha.hpcNode.hpcCluster.hpcClusterId=:clusterId and ha.hpcProperty.hpcPropName=:propName order by ha.hpcPropertyAssignTime asc";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("propName", propName);
		List<HpcPropertyAssign> list = this.query(HpcPropertyAssign.class, hql, map, null, null);
		if( list == null || list.isEmpty() )
		{
			return null;
		}
		HpcPropertyAssign ha = list.get(0);
		return ha.getHpcPropertyAssignValue();
	}
	
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public List<ParameterValue> queryParameters(Long clusterId) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("from HpcParameterAssign pa");
		Map<String,Object> map = new HashMap<String,Object>();
		if( clusterId != null && clusterId > 0 )
		{
			buffer.append(map.isEmpty()?" where":" and");
			buffer.append(" pa.hpcNode.hpcCluster.hpcClusterId=:clusterId");
			map.put("clusterId", clusterId);
		}
		buffer.append(" order by pa.hpcParameter.hpcParamName");
		String hql = buffer.toString();
		
		List<HpcParamAssign> list = this.query(HpcParamAssign.class, hql, map, null, null);
		List<ParameterValue> parameters = new ArrayList<ParameterValue>();
		for(HpcParamAssign one : list)
		{
			ParameterValue parameter = copyParameterValueFromDb(null,one);
			parameters.add(parameter);
		}
		
		return parameters;
	}
	
	@Transactional(value="hgMetaConfigTransactionManager",propagation=Propagation.REQUIRED,readOnly=true)
	public String queryClusterParameter(Long clusterId,String paramName) throws Exception
	{
		String hql = "from HpcParamAssign pa where pa.hpcNode.hpcCluster.hpcClusterId=:clusterId and pa.hpcParameter.hpcParamName=:paramName orcder by pa.hpcParamAssignTime desc";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("clusterId", clusterId);
		map.put("paramName", paramName);
		List<HpcPropertyAssign> list = this.query(HpcPropertyAssign.class, hql, map, null, null);
		if( list == null || list.isEmpty() )
		{
			return null;
		}
		HpcPropertyAssign ha = list.get(0);
		return ha.getHpcPropertyAssignValue();	
	}

	
}
