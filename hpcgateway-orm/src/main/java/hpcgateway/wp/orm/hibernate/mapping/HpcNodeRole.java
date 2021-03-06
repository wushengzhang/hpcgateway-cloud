package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.HashSet;
import java.util.Set;

/**
 * HpcNodeRole generated by hbm2java
 */
public class HpcNodeRole implements java.io.Serializable
{

	private Long hpcNodeRoleId;
	private String hpcNodeRoleName;
	private int hpcNodeRoleCode;
	private String hpcNodeRoleDesc;
	private Set hpcNodeRoleAllocs = new HashSet(0);

	public HpcNodeRole()
	{
	}

	public HpcNodeRole(String hpcNodeRoleName, int hpcNodeRoleCode)
	{
		this.hpcNodeRoleName = hpcNodeRoleName;
		this.hpcNodeRoleCode = hpcNodeRoleCode;
	}

	public HpcNodeRole(String hpcNodeRoleName, int hpcNodeRoleCode, String hpcNodeRoleDesc, Set hpcNodeRoleAllocs)
	{
		this.hpcNodeRoleName = hpcNodeRoleName;
		this.hpcNodeRoleCode = hpcNodeRoleCode;
		this.hpcNodeRoleDesc = hpcNodeRoleDesc;
		this.hpcNodeRoleAllocs = hpcNodeRoleAllocs;
	}

	public Long getHpcNodeRoleId()
	{
		return this.hpcNodeRoleId;
	}

	public void setHpcNodeRoleId(Long hpcNodeRoleId)
	{
		this.hpcNodeRoleId = hpcNodeRoleId;
	}

	public String getHpcNodeRoleName()
	{
		return this.hpcNodeRoleName;
	}

	public void setHpcNodeRoleName(String hpcNodeRoleName)
	{
		this.hpcNodeRoleName = hpcNodeRoleName;
	}

	public int getHpcNodeRoleCode()
	{
		return this.hpcNodeRoleCode;
	}

	public void setHpcNodeRoleCode(int hpcNodeRoleCode)
	{
		this.hpcNodeRoleCode = hpcNodeRoleCode;
	}

	public String getHpcNodeRoleDesc()
	{
		return this.hpcNodeRoleDesc;
	}

	public void setHpcNodeRoleDesc(String hpcNodeRoleDesc)
	{
		this.hpcNodeRoleDesc = hpcNodeRoleDesc;
	}

	public Set getHpcNodeRoleAllocs()
	{
		return this.hpcNodeRoleAllocs;
	}

	public void setHpcNodeRoleAllocs(Set hpcNodeRoleAllocs)
	{
		this.hpcNodeRoleAllocs = hpcNodeRoleAllocs;
	}

}
