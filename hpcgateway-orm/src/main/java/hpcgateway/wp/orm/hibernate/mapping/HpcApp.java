package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.HashSet;
import java.util.Set;

/**
 * HpcApp generated by hbm2java
 */
public class HpcApp implements java.io.Serializable
{

	private Long hpcAppId;
	private String hpcAppName;
	private String hpcAppCode;
	private String hpcAppDesc;
	private Set hpcJobPrices = new HashSet(0);
	private Set hpcJobs = new HashSet(0);

	public HpcApp()
	{
	}

	public HpcApp(String hpcAppName)
	{
		this.hpcAppName = hpcAppName;
	}

	public HpcApp(String hpcAppName, String hpcAppCode, String hpcAppDesc, Set hpcJobPrices, Set hpcJobs)
	{
		this.hpcAppName = hpcAppName;
		this.hpcAppCode = hpcAppCode;
		this.hpcAppDesc = hpcAppDesc;
		this.hpcJobPrices = hpcJobPrices;
		this.hpcJobs = hpcJobs;
	}

	public Long getHpcAppId()
	{
		return this.hpcAppId;
	}

	public void setHpcAppId(Long hpcAppId)
	{
		this.hpcAppId = hpcAppId;
	}

	public String getHpcAppName()
	{
		return this.hpcAppName;
	}

	public void setHpcAppName(String hpcAppName)
	{
		this.hpcAppName = hpcAppName;
	}

	public String getHpcAppCode()
	{
		return this.hpcAppCode;
	}

	public void setHpcAppCode(String hpcAppCode)
	{
		this.hpcAppCode = hpcAppCode;
	}

	public String getHpcAppDesc()
	{
		return this.hpcAppDesc;
	}

	public void setHpcAppDesc(String hpcAppDesc)
	{
		this.hpcAppDesc = hpcAppDesc;
	}

	public Set getHpcJobPrices()
	{
		return this.hpcJobPrices;
	}

	public void setHpcJobPrices(Set hpcJobPrices)
	{
		this.hpcJobPrices = hpcJobPrices;
	}

	public Set getHpcJobs()
	{
		return this.hpcJobs;
	}

	public void setHpcJobs(Set hpcJobs)
	{
		this.hpcJobs = hpcJobs;
	}

}
