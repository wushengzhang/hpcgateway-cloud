package hpcgateway.wp.metaconfig.service;

import java.util.List;
import java.util.Map;

import hpcgateway.wp.core.page.TreeNode;
import hpcgateway.wp.orm.beans.CloudGroup;
import hpcgateway.wp.orm.beans.CloudUser;
import hpcgateway.wp.orm.beans.Cluster;
import hpcgateway.wp.orm.beans.ClusterUser;
import hpcgateway.wp.orm.beans.Connect;
import hpcgateway.wp.orm.beans.ConnectType;
import hpcgateway.wp.orm.beans.Node;
import hpcgateway.wp.orm.beans.NodeConnect;
import hpcgateway.wp.orm.beans.NodeRole;
import hpcgateway.wp.orm.beans.Resource;
import hpcgateway.wp.orm.beans.Rm;
import hpcgateway.wp.orm.beans.Role;

public interface MetadataService 
{	
	public Map<String,String> getClusterUser(String username,String password) throws Exception;
		
	public List<TreeNode> queryTreeNode(String path) throws Exception;
	
	// Cluster
	public int countClusters(String name) throws Exception;
	public List<Cluster> queryClusters(String name,Integer pageNo,Integer pageSize) throws Exception;
	public Cluster fetchCluster(Long id)throws Exception;
	public void saveCluster(Cluster cluster) throws Exception;
	public void removeCluster(Long clusterId) throws Exception;
	
	// Groups
	public List<CloudGroup> queryGroupsForTree() throws Exception;
	public List<CloudGroup> queryGroups(Long userId) throws Exception;
	public int countGroups(String name) throws Exception;
	public List<CloudGroup> queryGroups(String name,Integer pageNo,Integer pageSize) throws Exception;
	public void removeGroup(Long groupId) throws Exception;
	public void saveGroup(CloudGroup group) throws Exception;
	public CloudGroup fetchGroup(Long groupId) throws Exception;
	public List<CloudGroup> querySubGroups(Long groupId) throws Exception;
	
	// User
	public void setpwd(Long id,String password) throws Exception;
	public List<CloudUser> queryAllPayingUsers() throws Exception;
	public int countUsers(String name) throws Exception;
	public List<CloudUser> queryUsers(String name,Integer pageNo,Integer pageSize) throws Exception;
	public CloudUser fetchUser(Long userId) throws Exception;
	public void saveUser(CloudUser user) throws Exception;
	public void removeUser(Long userId) throws Exception;
	
	// Role
	public int countRoles(String name) throws Exception;
	public List<Role> queryRoles(String name,Integer pageNo,Integer pageSize) throws Exception;
	public Role fetchRole(Long roleId) throws Exception;
	public void saveRole(Role role) throws Exception;
	public void removeRole(Long roleId) throws Exception;
	
	// ClusterUser
	public int countClusterUsers(Long clusterId,String name) throws Exception;
	public List<ClusterUser> queryClusterUsers(Long clusterId,String name,Integer pageNo,Integer pageSize) throws Exception;
	public ClusterUser fetchClusterUser(Long clusterId,Long userId) throws Exception;
	public void saveClusterUser(Long clusterId,ClusterUser clusterUser) throws Exception;
	public void removeClusterClusterUser(Long clusterId,Long userId) throws Exception;
	
	// RM
	public int countRms(Long clusterId,String name) throws Exception;
	public List<Rm> queryRms(Long clusterId,String name,Integer pageNo,Integer pageSize) throws Exception;
	public Rm fetchRm(Long clusterId,Long rmId) throws Exception;
	public void saveRm(Long clusterId,Rm rm) throws Exception;
	public void removeRm(Long clusterId,Long rmId) throws Exception;
	
	// Resource
	public int countResources(Long clusterId,String type) throws Exception;
	public List<Resource> queryResources(Long clusterId,String type,Integer pageNo,Integer pageSize) throws Exception;
	public Resource fetchResource(Long clusterId,Long resId) throws Exception;
	public void saveResource(Long clusterId,Resource resource) throws Exception;
	public void removeResource(Long clusterId,Long resId) throws Exception;
	
	// Node
	public int countNodes(Long clusterId, NodeRole role, String name) throws Exception;
	public List<Node> queryNodes(Long clusterId, NodeRole role, String name, Integer pageNo, Integer pageSize) throws Exception;
	public Node fetchNode(Long clusterId, Long id) throws Exception;
	public void saveNode(Long clusterId, Node metaNode) throws Exception;
	public void removeNode(Long clusterId, Long id) throws Exception;
		
	// Connections
	public List<NodeConnect> queryConnections(Long clusterId,ConnectType type,NodeRole nodeRole) throws Exception;
	
}