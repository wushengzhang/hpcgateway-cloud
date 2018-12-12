package hpcgateway.wp.orm.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CloudUser 
{
	protected Long id;
	protected Long askTo;
	protected String askToName;
	protected String name;
	protected String password;
	protected BigDecimal balance;
	protected BigDecimal charged;
	protected BigDecimal consumed;
	protected BigDecimal jobFee;
	protected Long jobNum;
	protected Long jobHosts;
	protected Long jobCores;
	protected Date jobTime;
	protected String jobTimeStr;
	protected BigDecimal storageFee;
	protected BigDecimal storageCap;
	protected String storageUnit;
	protected Date storageTime;
	protected String storageTimeStr;
	protected List<CloudGroup> groups;

	
	public CloudUser()
	{
		this.balance = BigDecimal.ZERO;
		this.charged = BigDecimal.ZERO;
		this.consumed = BigDecimal.ZERO;
		this.jobFee = BigDecimal.ZERO;
		this.jobNum = 0L;
		this.jobHosts = 0L;
		this.jobCores = 0L;
		this.jobTime = new Date();
		this.storageFee = BigDecimal.ZERO;
		this.storageCap = BigDecimal.ZERO;
		this.storageUnit = "B";
		this.storageTime = null;
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getAskTo()
	{
		return askTo;
	}

	public void setAskTo(Long askTo)
	{
		this.askTo = askTo;
	}



	public String getAskToName()
	{
		return askToName;
	}

	public void setAskToName(String askToName)
	{
		this.askToName = askToName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}

	public BigDecimal getCharged()
	{
		return charged;
	}

	public void setCharged(BigDecimal charged)
	{
		this.charged = charged;
	}
	
	public BigDecimal getConsumed()
	{
		return consumed;
	}

	public void setConsumed(BigDecimal consumed)
	{
		this.consumed = consumed;
	}

	public BigDecimal getJobFee()
	{
		return jobFee;
	}

	public void setJobFee(BigDecimal jobFee)
	{
		this.jobFee = jobFee;
	}

	public Long getJobNum()
	{
		return jobNum;
	}

	public void setJobNum(Long jobNum)
	{
		this.jobNum = jobNum;
	}

	public Long getJobHosts()
	{
		return jobHosts;
	}

	public void setJobHosts(Long jobHosts)
	{
		this.jobHosts = jobHosts;
	}

	public Long getJobCores()
	{
		return jobCores;
	}

	public void setJobCores(Long jobCores)
	{
		this.jobCores = jobCores;
	}

	public Date getJobTime()
	{
		return jobTime;
	}

	public void setJobTime(Date jobTime)
	{
		this.jobTime = jobTime;
	}

	public BigDecimal getStorageFee()
	{
		return storageFee;
	}

	public void setStorageFee(BigDecimal storageFee)
	{
		this.storageFee = storageFee;
	}

	public BigDecimal getStorageCap()
	{
		return storageCap;
	}

	public void setStorageCap(BigDecimal storageCap)
	{
		this.storageCap = storageCap;
	}

	public String getStorageUnit()
	{
		return storageUnit;
	}

	public void setStorageUnit(String storageUnit)
	{
		this.storageUnit = storageUnit;
	}

	public Date getStorageTime()
	{
		return storageTime;
	}

	public void setStorageTime(Date storageTime)
	{
		this.storageTime = storageTime;
	}

	public String getJobTimeStr()
	{
		return jobTimeStr;
	}

	public void setJobTimeStr(String jobTimeStr)
	{
		this.jobTimeStr = jobTimeStr;
	}

	public String getStorageTimeStr()
	{
		return storageTimeStr;
	}

	public void setStorageTimeStr(String storageTimeStr)
	{
		this.storageTimeStr = storageTimeStr;
	}

	public List<CloudGroup> getGroups()
	{
		return groups;
	}

	public void setGroups(List<CloudGroup> groups)
	{
		this.groups = groups;
	}
}
