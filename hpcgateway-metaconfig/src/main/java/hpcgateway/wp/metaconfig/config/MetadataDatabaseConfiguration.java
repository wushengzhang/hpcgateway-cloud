package hpcgateway.wp.metaconfig.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("metadataDatabaseConfiguration")
public class MetadataDatabaseConfiguration
{
	@Value("${metaconfig.db.url}")
	private String url;
	@Value("${metaconfig.db.driver}")
	private String driver;
	@Value("${metaconfig.db.username}")
	private String username;
	@Value("${metaconfig.db.password}")
	private String password;
	@Value("${metaconfig.db.poolinitialsize}")
	private Integer poolInitialSize;
	@Value("${metaconfig.db.poolmaxactive}")
	private Integer poolMaxActive;
	@Value("${metaconfig.db.poolmaxidle}")
	private Integer poolMaxIdle;
	@Value("${metaconfig.db.poolminidle}")
	private Integer poolMinIdel;
	@Value("${metaconfig.db.poolmaxwait}")
	private Integer poolMaxWait;
	@Value("${metaconfig.db.poolremoveabandoned}")
	private Boolean poolRemoveAbandoned;
	@Value("${metaconfig.db.poolremoveabandonedtimeout}")
	private Integer poolRemoveAbandonedTimeout;
	
	public MetadataDatabaseConfiguration()
	{
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getDriver()
	{
		return driver;
	}
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public Integer getPoolInitialSize()
	{
		return poolInitialSize;
	}
	public void setPoolInitialSize(Integer poolInitialSize)
	{
		this.poolInitialSize = poolInitialSize;
	}
	public Integer getPoolMaxActive()
	{
		return poolMaxActive;
	}
	public void setPoolMaxActive(Integer poolMaxActive)
	{
		this.poolMaxActive = poolMaxActive;
	}
	public Integer getPoolMaxIdle()
	{
		return poolMaxIdle;
	}
	public void setPoolMaxIdle(Integer poolMaxIdle)
	{
		this.poolMaxIdle = poolMaxIdle;
	}
	public Integer getPoolMinIdel()
	{
		return poolMinIdel;
	}
	public void setPoolMinIdel(Integer poolMinIdel)
	{
		this.poolMinIdel = poolMinIdel;
	}
	public Integer getPoolMaxWait()
	{
		return poolMaxWait;
	}
	public void setPoolMaxWait(Integer poolMaxWait)
	{
		this.poolMaxWait = poolMaxWait;
	}
	public Boolean getPoolRemoveAbandoned()
	{
		return poolRemoveAbandoned;
	}
	public void setPoolRemoveAbandoned(Boolean poolRemoveAbandoned)
	{
		this.poolRemoveAbandoned = poolRemoveAbandoned;
	}
	public Integer getPoolRemoveAbandonedTimeout()
	{
		return poolRemoveAbandonedTimeout;
	}
	public void setPoolRemoveAbandonedTimeout(Integer poolRemoveAbandonedTimeout)
	{
		this.poolRemoveAbandonedTimeout = poolRemoveAbandonedTimeout;
	}
	



}
