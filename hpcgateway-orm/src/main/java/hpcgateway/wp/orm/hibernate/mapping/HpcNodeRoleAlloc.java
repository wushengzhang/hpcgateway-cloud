package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.Date;

/**
 * HpcNodeRoleAlloc generated by hbm2java
 */
public class HpcNodeRoleAlloc implements java.io.Serializable
{

	private Long hpcNodeRoleAllocId;
	private HpcNode hpcNode;
	private HpcNodeRole hpcNodeRole;
	private String hpcNodeRoleAllocName;
	private int hpcNodeRoleAllocPriority;
	private String hpcNodeRoleAllocDescription;
	private Date hpcNodeRoleAllocTime;

	public HpcNodeRoleAlloc()
	{
	}

	public HpcNodeRoleAlloc(String hpcNodeRoleAllocName, int hpcNodeRoleAllocPriority, Date hpcNodeRoleAllocTime)
	{
		this.hpcNodeRoleAllocName = hpcNodeRoleAllocName;
		this.hpcNodeRoleAllocPriority = hpcNodeRoleAllocPriority;
		this.hpcNodeRoleAllocTime = hpcNodeRoleAllocTime;
	}

	public HpcNodeRoleAlloc(HpcNode hpcNode, HpcNodeRole hpcNodeRole, String hpcNodeRoleAllocName, int hpcNodeRoleAllocPriority, String hpcNodeRoleAllocDescription, Date hpcNodeRoleAllocTime)
	{
		this.hpcNode = hpcNode;
		this.hpcNodeRole = hpcNodeRole;
		this.hpcNodeRoleAllocName = hpcNodeRoleAllocName;
		this.hpcNodeRoleAllocPriority = hpcNodeRoleAllocPriority;
		this.hpcNodeRoleAllocDescription = hpcNodeRoleAllocDescription;
		this.hpcNodeRoleAllocTime = hpcNodeRoleAllocTime;
	}

	public Long getHpcNodeRoleAllocId()
	{
		return this.hpcNodeRoleAllocId;
	}

	public void setHpcNodeRoleAllocId(Long hpcNodeRoleAllocId)
	{
		this.hpcNodeRoleAllocId = hpcNodeRoleAllocId;
	}

	public HpcNode getHpcNode()
	{
		return this.hpcNode;
	}

	public void setHpcNode(HpcNode hpcNode)
	{
		this.hpcNode = hpcNode;
	}

	public HpcNodeRole getHpcNodeRole()
	{
		return this.hpcNodeRole;
	}

	public void setHpcNodeRole(HpcNodeRole hpcNodeRole)
	{
		this.hpcNodeRole = hpcNodeRole;
	}

	public String getHpcNodeRoleAllocName()
	{
		return this.hpcNodeRoleAllocName;
	}

	public void setHpcNodeRoleAllocName(String hpcNodeRoleAllocName)
	{
		this.hpcNodeRoleAllocName = hpcNodeRoleAllocName;
	}

	public int getHpcNodeRoleAllocPriority()
	{
		return this.hpcNodeRoleAllocPriority;
	}

	public void setHpcNodeRoleAllocPriority(int hpcNodeRoleAllocPriority)
	{
		this.hpcNodeRoleAllocPriority = hpcNodeRoleAllocPriority;
	}

	public String getHpcNodeRoleAllocDescription()
	{
		return this.hpcNodeRoleAllocDescription;
	}

	public void setHpcNodeRoleAllocDescription(String hpcNodeRoleAllocDescription)
	{
		this.hpcNodeRoleAllocDescription = hpcNodeRoleAllocDescription;
	}

	public Date getHpcNodeRoleAllocTime()
	{
		return this.hpcNodeRoleAllocTime;
	}

	public void setHpcNodeRoleAllocTime(Date hpcNodeRoleAllocTime)
	{
		this.hpcNodeRoleAllocTime = hpcNodeRoleAllocTime;
	}

}