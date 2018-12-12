package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.Date;

/**
 * HpcParamAssign generated by hbm2java
 */
public class HpcParamAssign implements java.io.Serializable
{

	private Long hpcParamAssignId;
	private HpcNode hpcNode;
	private HpcParameter hpcParameter;
	private String hpcParamAssignValue;
	private Date hpcParamAssignTime;

	public HpcParamAssign()
	{
	}

	public HpcParamAssign(String hpcParamAssignValue, Date hpcParamAssignTime)
	{
		this.hpcParamAssignValue = hpcParamAssignValue;
		this.hpcParamAssignTime = hpcParamAssignTime;
	}

	public HpcParamAssign(HpcNode hpcNode, HpcParameter hpcParameter, String hpcParamAssignValue, Date hpcParamAssignTime)
	{
		this.hpcNode = hpcNode;
		this.hpcParameter = hpcParameter;
		this.hpcParamAssignValue = hpcParamAssignValue;
		this.hpcParamAssignTime = hpcParamAssignTime;
	}

	public Long getHpcParamAssignId()
	{
		return this.hpcParamAssignId;
	}

	public void setHpcParamAssignId(Long hpcParamAssignId)
	{
		this.hpcParamAssignId = hpcParamAssignId;
	}

	public HpcNode getHpcNode()
	{
		return this.hpcNode;
	}

	public void setHpcNode(HpcNode hpcNode)
	{
		this.hpcNode = hpcNode;
	}

	public HpcParameter getHpcParameter()
	{
		return this.hpcParameter;
	}

	public void setHpcParameter(HpcParameter hpcParameter)
	{
		this.hpcParameter = hpcParameter;
	}

	public String getHpcParamAssignValue()
	{
		return this.hpcParamAssignValue;
	}

	public void setHpcParamAssignValue(String hpcParamAssignValue)
	{
		this.hpcParamAssignValue = hpcParamAssignValue;
	}

	public Date getHpcParamAssignTime()
	{
		return this.hpcParamAssignTime;
	}

	public void setHpcParamAssignTime(Date hpcParamAssignTime)
	{
		this.hpcParamAssignTime = hpcParamAssignTime;
	}

}