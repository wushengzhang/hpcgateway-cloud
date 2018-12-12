package hpcgateway.wp.orm.beans;

import java.math.BigDecimal;
import java.util.Date;


public class ClusterUser 
{
	private Long userId;
	private String userName;
	private Long id;
	private String name;
	private String password;
	private String auto;
	private String idrsa;
	private BigDecimal balance;
	private BigDecimal charged;
	private BigDecimal consumed;
	private BigDecimal jobFee;
	private Long jobNum;
	private Long jobHosts;
	private Long jobCores;
	private Date jobTime;
	private String jobTimeStr;
	private BigDecimal storageFee;
	private BigDecimal storageCap;
	private String storageUnit;
	private Date storageTime;
	private String storageTimeStr;
	
	public ClusterUser()
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
		this.storageTime = new Date();
	}


	
	public Long getUserId()
	{
		return userId;
	}



	public void setUserId(Long userId)
	{
		this.userId = userId;
	}



	public String getUserName()
	{
		return userName;
	}



	public void setUserName(String userName)
	{
		this.userName = userName;
	}



	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
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
	public String getAuto()
	{
		return auto;
	}
	public void setAuto(String auto)
	{
		this.auto = auto;
	}
	public String getIdrsa()
	{
		return idrsa;
	}
	public void setIdrsa(String idrsa)
	{
		this.idrsa = idrsa;
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
	public BigDecimal getConsumed()
	{
		return consumed;
	}
	public void setConsumed(BigDecimal consumed)
	{
		this.consumed = consumed;
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


	
	
}
