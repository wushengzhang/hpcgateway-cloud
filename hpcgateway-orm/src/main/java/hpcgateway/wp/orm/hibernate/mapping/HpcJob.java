package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.math.BigDecimal;
import java.util.Date;

/**
 * HpcJob generated by hbm2java
 */
public class HpcJob implements java.io.Serializable
{

	private Long hpcJobId;
	private HpcApp hpcApp;
	private HpcCloudUser hpcCloudUserByHpcJobSubmitUser;
	private HpcCloudUser hpcCloudUserByHpcHobPaidUser;
	private HpcCluster hpcCluster;
	private HpcClusterUser hpcClusterUser;
	private HpcJobPrice hpcJobPrice;
	private String hpcJobName;
	private Date hpcJobEnd;
	private Date hpcJobStart;
	private Integer hpcJobCores;
	private Integer hpcJobHosts;
	private Long hpcJobNumber;
	private BigDecimal hpcJobPrice_1;
	private String hpcJobUnit;
	private BigDecimal hpcJobAmount;
	private String hpcJobState;

	public HpcJob()
	{
	}

	public HpcJob(HpcCloudUser hpcCloudUserByHpcJobSubmitUser, HpcCloudUser hpcCloudUserByHpcHobPaidUser, String hpcJobName, BigDecimal hpcJobAmount, String hpcJobState)
	{
		this.hpcCloudUserByHpcJobSubmitUser = hpcCloudUserByHpcJobSubmitUser;
		this.hpcCloudUserByHpcHobPaidUser = hpcCloudUserByHpcHobPaidUser;
		this.hpcJobName = hpcJobName;
		this.hpcJobAmount = hpcJobAmount;
		this.hpcJobState = hpcJobState;
	}

	public HpcJob(HpcApp hpcApp, HpcCloudUser hpcCloudUserByHpcJobSubmitUser, HpcCloudUser hpcCloudUserByHpcHobPaidUser, HpcCluster hpcCluster, HpcClusterUser hpcClusterUser, HpcJobPrice hpcJobPrice, String hpcJobName, Date hpcJobEnd, Date hpcJobStart, Integer hpcJobCores,
			Integer hpcJobHosts, Long hpcJobNumber, BigDecimal hpcJobPrice_1, String hpcJobUnit, BigDecimal hpcJobAmount, String hpcJobState)
	{
		this.hpcApp = hpcApp;
		this.hpcCloudUserByHpcJobSubmitUser = hpcCloudUserByHpcJobSubmitUser;
		this.hpcCloudUserByHpcHobPaidUser = hpcCloudUserByHpcHobPaidUser;
		this.hpcCluster = hpcCluster;
		this.hpcClusterUser = hpcClusterUser;
		this.hpcJobPrice = hpcJobPrice;
		this.hpcJobName = hpcJobName;
		this.hpcJobEnd = hpcJobEnd;
		this.hpcJobStart = hpcJobStart;
		this.hpcJobCores = hpcJobCores;
		this.hpcJobHosts = hpcJobHosts;
		this.hpcJobNumber = hpcJobNumber;
		this.hpcJobPrice_1 = hpcJobPrice_1;
		this.hpcJobUnit = hpcJobUnit;
		this.hpcJobAmount = hpcJobAmount;
		this.hpcJobState = hpcJobState;
	}

	public Long getHpcJobId()
	{
		return this.hpcJobId;
	}

	public void setHpcJobId(Long hpcJobId)
	{
		this.hpcJobId = hpcJobId;
	}

	public HpcApp getHpcApp()
	{
		return this.hpcApp;
	}

	public void setHpcApp(HpcApp hpcApp)
	{
		this.hpcApp = hpcApp;
	}

	public HpcCloudUser getHpcCloudUserByHpcJobSubmitUser()
	{
		return this.hpcCloudUserByHpcJobSubmitUser;
	}

	public void setHpcCloudUserByHpcJobSubmitUser(HpcCloudUser hpcCloudUserByHpcJobSubmitUser)
	{
		this.hpcCloudUserByHpcJobSubmitUser = hpcCloudUserByHpcJobSubmitUser;
	}

	public HpcCloudUser getHpcCloudUserByHpcHobPaidUser()
	{
		return this.hpcCloudUserByHpcHobPaidUser;
	}

	public void setHpcCloudUserByHpcHobPaidUser(HpcCloudUser hpcCloudUserByHpcHobPaidUser)
	{
		this.hpcCloudUserByHpcHobPaidUser = hpcCloudUserByHpcHobPaidUser;
	}

	public HpcCluster getHpcCluster()
	{
		return this.hpcCluster;
	}

	public void setHpcCluster(HpcCluster hpcCluster)
	{
		this.hpcCluster = hpcCluster;
	}

	public HpcClusterUser getHpcClusterUser()
	{
		return this.hpcClusterUser;
	}

	public void setHpcClusterUser(HpcClusterUser hpcClusterUser)
	{
		this.hpcClusterUser = hpcClusterUser;
	}

	public HpcJobPrice getHpcJobPrice()
	{
		return this.hpcJobPrice;
	}

	public void setHpcJobPrice(HpcJobPrice hpcJobPrice)
	{
		this.hpcJobPrice = hpcJobPrice;
	}

	public String getHpcJobName()
	{
		return this.hpcJobName;
	}

	public void setHpcJobName(String hpcJobName)
	{
		this.hpcJobName = hpcJobName;
	}

	public Date getHpcJobEnd()
	{
		return this.hpcJobEnd;
	}

	public void setHpcJobEnd(Date hpcJobEnd)
	{
		this.hpcJobEnd = hpcJobEnd;
	}

	public Date getHpcJobStart()
	{
		return this.hpcJobStart;
	}

	public void setHpcJobStart(Date hpcJobStart)
	{
		this.hpcJobStart = hpcJobStart;
	}

	public Integer getHpcJobCores()
	{
		return this.hpcJobCores;
	}

	public void setHpcJobCores(Integer hpcJobCores)
	{
		this.hpcJobCores = hpcJobCores;
	}

	public Integer getHpcJobHosts()
	{
		return this.hpcJobHosts;
	}

	public void setHpcJobHosts(Integer hpcJobHosts)
	{
		this.hpcJobHosts = hpcJobHosts;
	}

	public Long getHpcJobNumber()
	{
		return this.hpcJobNumber;
	}

	public void setHpcJobNumber(Long hpcJobNumber)
	{
		this.hpcJobNumber = hpcJobNumber;
	}

	public BigDecimal getHpcJobPrice_1()
	{
		return this.hpcJobPrice_1;
	}

	public void setHpcJobPrice_1(BigDecimal hpcJobPrice_1)
	{
		this.hpcJobPrice_1 = hpcJobPrice_1;
	}

	public String getHpcJobUnit()
	{
		return this.hpcJobUnit;
	}

	public void setHpcJobUnit(String hpcJobUnit)
	{
		this.hpcJobUnit = hpcJobUnit;
	}

	public BigDecimal getHpcJobAmount()
	{
		return this.hpcJobAmount;
	}

	public void setHpcJobAmount(BigDecimal hpcJobAmount)
	{
		this.hpcJobAmount = hpcJobAmount;
	}

	public String getHpcJobState()
	{
		return this.hpcJobState;
	}

	public void setHpcJobState(String hpcJobState)
	{
		this.hpcJobState = hpcJobState;
	}

}
