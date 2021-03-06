package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.Date;

/**
 * HpcRoleAlloc generated by hbm2java
 */
public class HpcRoleAlloc implements java.io.Serializable
{

	private Long hpcRoleAllocId;
	private HpcCloudUser hpcCloudUser;
	private HpcRole hpcRole;
	private Date hpcRoleAllocTime;
	private Date hpcRoleAllocStart;
	private Date hpcRoleAllocExpire;

	public HpcRoleAlloc()
	{
	}

	public HpcRoleAlloc(Date hpcRoleAllocTime)
	{
		this.hpcRoleAllocTime = hpcRoleAllocTime;
	}

	public HpcRoleAlloc(HpcCloudUser hpcCloudUser, HpcRole hpcRole, Date hpcRoleAllocTime, Date hpcRoleAllocStart, Date hpcRoleAllocExpire)
	{
		this.hpcCloudUser = hpcCloudUser;
		this.hpcRole = hpcRole;
		this.hpcRoleAllocTime = hpcRoleAllocTime;
		this.hpcRoleAllocStart = hpcRoleAllocStart;
		this.hpcRoleAllocExpire = hpcRoleAllocExpire;
	}

	public Long getHpcRoleAllocId()
	{
		return this.hpcRoleAllocId;
	}

	public void setHpcRoleAllocId(Long hpcRoleAllocId)
	{
		this.hpcRoleAllocId = hpcRoleAllocId;
	}

	public HpcCloudUser getHpcCloudUser()
	{
		return this.hpcCloudUser;
	}

	public void setHpcCloudUser(HpcCloudUser hpcCloudUser)
	{
		this.hpcCloudUser = hpcCloudUser;
	}

	public HpcRole getHpcRole()
	{
		return this.hpcRole;
	}

	public void setHpcRole(HpcRole hpcRole)
	{
		this.hpcRole = hpcRole;
	}

	public Date getHpcRoleAllocTime()
	{
		return this.hpcRoleAllocTime;
	}

	public void setHpcRoleAllocTime(Date hpcRoleAllocTime)
	{
		this.hpcRoleAllocTime = hpcRoleAllocTime;
	}

	public Date getHpcRoleAllocStart()
	{
		return this.hpcRoleAllocStart;
	}

	public void setHpcRoleAllocStart(Date hpcRoleAllocStart)
	{
		this.hpcRoleAllocStart = hpcRoleAllocStart;
	}

	public Date getHpcRoleAllocExpire()
	{
		return this.hpcRoleAllocExpire;
	}

	public void setHpcRoleAllocExpire(Date hpcRoleAllocExpire)
	{
		this.hpcRoleAllocExpire = hpcRoleAllocExpire;
	}

}
