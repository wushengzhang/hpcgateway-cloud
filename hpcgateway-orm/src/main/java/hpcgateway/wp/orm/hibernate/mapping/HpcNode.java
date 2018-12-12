package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.HashSet;
import java.util.Set;

/**
 * HpcNode generated by hbm2java
 */
public class HpcNode implements java.io.Serializable
{

	private Long hpcNodeId;
	private HpcCluster hpcCluster;
	private String hpcNodeName;
	private String hpcNodeAliasnames;
	private Set hpcRmsForHpcRmControll = new HashSet(0);
	private Set hpcInterfaces = new HashSet(0);
	private Set hpcParamAssigns = new HashSet(0);
	private Set hpcConnects = new HashSet(0);
	private Set hpcNodeRoleAllocs = new HashSet(0);
	private Set hpcPropertyAssigns = new HashSet(0);
	private Set hpcRmsForHpcRmAccount = new HashSet(0);
	private Set hpcRmsForHcRmInstall = new HashSet(0);
	private Set hpcRmsForHpcRmStorage = new HashSet(0);
	private Set hpcRmsForHpcRmSchedule = new HashSet(0);

	public HpcNode()
	{
	}

	public HpcNode(String hpcNodeName, String hpcNodeAliasnames)
	{
		this.hpcNodeName = hpcNodeName;
		this.hpcNodeAliasnames = hpcNodeAliasnames;
	}

	public HpcNode(HpcCluster hpcCluster, String hpcNodeName, String hpcNodeAliasnames, Set hpcRmsForHpcRmControll, Set hpcInterfaces, Set hpcParamAssigns, Set hpcConnects, Set hpcNodeRoleAllocs, Set hpcPropertyAssigns, Set hpcRmsForHpcRmAccount, Set hpcRmsForHcRmInstall,
			Set hpcRmsForHpcRmStorage, Set hpcRmsForHpcRmSchedule)
	{
		this.hpcCluster = hpcCluster;
		this.hpcNodeName = hpcNodeName;
		this.hpcNodeAliasnames = hpcNodeAliasnames;
		this.hpcRmsForHpcRmControll = hpcRmsForHpcRmControll;
		this.hpcInterfaces = hpcInterfaces;
		this.hpcParamAssigns = hpcParamAssigns;
		this.hpcConnects = hpcConnects;
		this.hpcNodeRoleAllocs = hpcNodeRoleAllocs;
		this.hpcPropertyAssigns = hpcPropertyAssigns;
		this.hpcRmsForHpcRmAccount = hpcRmsForHpcRmAccount;
		this.hpcRmsForHcRmInstall = hpcRmsForHcRmInstall;
		this.hpcRmsForHpcRmStorage = hpcRmsForHpcRmStorage;
		this.hpcRmsForHpcRmSchedule = hpcRmsForHpcRmSchedule;
	}

	public Long getHpcNodeId()
	{
		return this.hpcNodeId;
	}

	public void setHpcNodeId(Long hpcNodeId)
	{
		this.hpcNodeId = hpcNodeId;
	}

	public HpcCluster getHpcCluster()
	{
		return this.hpcCluster;
	}

	public void setHpcCluster(HpcCluster hpcCluster)
	{
		this.hpcCluster = hpcCluster;
	}

	public String getHpcNodeName()
	{
		return this.hpcNodeName;
	}

	public void setHpcNodeName(String hpcNodeName)
	{
		this.hpcNodeName = hpcNodeName;
	}

	public String getHpcNodeAliasnames()
	{
		return this.hpcNodeAliasnames;
	}

	public void setHpcNodeAliasnames(String hpcNodeAliasnames)
	{
		this.hpcNodeAliasnames = hpcNodeAliasnames;
	}

	public Set getHpcRmsForHpcRmControll()
	{
		return this.hpcRmsForHpcRmControll;
	}

	public void setHpcRmsForHpcRmControll(Set hpcRmsForHpcRmControll)
	{
		this.hpcRmsForHpcRmControll = hpcRmsForHpcRmControll;
	}

	public Set getHpcInterfaces()
	{
		return this.hpcInterfaces;
	}

	public void setHpcInterfaces(Set hpcInterfaces)
	{
		this.hpcInterfaces = hpcInterfaces;
	}

	public Set getHpcParamAssigns()
	{
		return this.hpcParamAssigns;
	}

	public void setHpcParamAssigns(Set hpcParamAssigns)
	{
		this.hpcParamAssigns = hpcParamAssigns;
	}

	public Set getHpcConnects()
	{
		return this.hpcConnects;
	}

	public void setHpcConnects(Set hpcConnects)
	{
		this.hpcConnects = hpcConnects;
	}

	public Set getHpcNodeRoleAllocs()
	{
		return this.hpcNodeRoleAllocs;
	}

	public void setHpcNodeRoleAllocs(Set hpcNodeRoleAllocs)
	{
		this.hpcNodeRoleAllocs = hpcNodeRoleAllocs;
	}

	public Set getHpcPropertyAssigns()
	{
		return this.hpcPropertyAssigns;
	}

	public void setHpcPropertyAssigns(Set hpcPropertyAssigns)
	{
		this.hpcPropertyAssigns = hpcPropertyAssigns;
	}

	public Set getHpcRmsForHpcRmAccount()
	{
		return this.hpcRmsForHpcRmAccount;
	}

	public void setHpcRmsForHpcRmAccount(Set hpcRmsForHpcRmAccount)
	{
		this.hpcRmsForHpcRmAccount = hpcRmsForHpcRmAccount;
	}

	public Set getHpcRmsForHcRmInstall()
	{
		return this.hpcRmsForHcRmInstall;
	}

	public void setHpcRmsForHcRmInstall(Set hpcRmsForHcRmInstall)
	{
		this.hpcRmsForHcRmInstall = hpcRmsForHcRmInstall;
	}

	public Set getHpcRmsForHpcRmStorage()
	{
		return this.hpcRmsForHpcRmStorage;
	}

	public void setHpcRmsForHpcRmStorage(Set hpcRmsForHpcRmStorage)
	{
		this.hpcRmsForHpcRmStorage = hpcRmsForHpcRmStorage;
	}

	public Set getHpcRmsForHpcRmSchedule()
	{
		return this.hpcRmsForHpcRmSchedule;
	}

	public void setHpcRmsForHpcRmSchedule(Set hpcRmsForHpcRmSchedule)
	{
		this.hpcRmsForHpcRmSchedule = hpcRmsForHpcRmSchedule;
	}

}