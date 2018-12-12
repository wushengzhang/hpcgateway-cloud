package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.HashSet;
import java.util.Set;

/**
 * HpcConnectType generated by hbm2java
 */
public class HpcConnectType implements java.io.Serializable
{

	private Long hpcConnTypeId;
	private String hpcConnTypeName;
	private int hpcConnTypeCode;
	private String hpcConnTypeDesc;
	private Set hpcConnects = new HashSet(0);

	public HpcConnectType()
	{
	}

	public HpcConnectType(String hpcConnTypeName, int hpcConnTypeCode)
	{
		this.hpcConnTypeName = hpcConnTypeName;
		this.hpcConnTypeCode = hpcConnTypeCode;
	}

	public HpcConnectType(String hpcConnTypeName, int hpcConnTypeCode, String hpcConnTypeDesc, Set hpcConnects)
	{
		this.hpcConnTypeName = hpcConnTypeName;
		this.hpcConnTypeCode = hpcConnTypeCode;
		this.hpcConnTypeDesc = hpcConnTypeDesc;
		this.hpcConnects = hpcConnects;
	}

	public Long getHpcConnTypeId()
	{
		return this.hpcConnTypeId;
	}

	public void setHpcConnTypeId(Long hpcConnTypeId)
	{
		this.hpcConnTypeId = hpcConnTypeId;
	}

	public String getHpcConnTypeName()
	{
		return this.hpcConnTypeName;
	}

	public void setHpcConnTypeName(String hpcConnTypeName)
	{
		this.hpcConnTypeName = hpcConnTypeName;
	}

	public int getHpcConnTypeCode()
	{
		return this.hpcConnTypeCode;
	}

	public void setHpcConnTypeCode(int hpcConnTypeCode)
	{
		this.hpcConnTypeCode = hpcConnTypeCode;
	}

	public String getHpcConnTypeDesc()
	{
		return this.hpcConnTypeDesc;
	}

	public void setHpcConnTypeDesc(String hpcConnTypeDesc)
	{
		this.hpcConnTypeDesc = hpcConnTypeDesc;
	}

	public Set getHpcConnects()
	{
		return this.hpcConnects;
	}

	public void setHpcConnects(Set hpcConnects)
	{
		this.hpcConnects = hpcConnects;
	}

}