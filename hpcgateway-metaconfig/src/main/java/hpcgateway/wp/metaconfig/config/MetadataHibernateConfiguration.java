package hpcgateway.wp.metaconfig.config;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("metadataHibernateConfiguration")
//@ConfigurationProperties(prefix="metaconfig.hibernate")
public class MetadataHibernateConfiguration
{
	@Value("${metaconfig.hibernate.dialect}")
	private String dialect;
	@Value("${metaconfig.hibernate.autocommit}")
	private String autoCommit;
	@Value("${metaconfig.hibernate.showsql}")
	private String showSql;
	@Value("${metaconfig.hibernate.releasemode}")
	private String releaseMode;
	@Value("${metaconfig.hibernate.hbm2ddl}")
	private String hbm2ddl;
	@Value("${metaconfig.hibernate.mappinglocations}")
	private String mappingLocations;
	

	public String getDialect()
	{
		return dialect;
	}
	public void setDialect(String dialect)
	{
		this.dialect = dialect;
	}
	public String getAutoCommit()
	{
		return autoCommit;
	}
	public void setAutoCommit(String autoCommit)
	{
		this.autoCommit = autoCommit;
	}
	public String getShowSql()
	{
		return showSql;
	}
	public void setShowSql(String showSql)
	{
		this.showSql = showSql;
	}
	public String getReleaseMode()
	{
		return releaseMode;
	}
	public void setReleaseMode(String releaseMode)
	{
		this.releaseMode = releaseMode;
	}
	public String getHbm2ddl()
	{
		return hbm2ddl;
	}
	public void setHbm2ddl(String hbm2ddl)
	{
		this.hbm2ddl = hbm2ddl;
	}
	public String getMappingLocations()
	{
		return mappingLocations;
	}
	public void setMappingLocations(String mappingLocations)
	{
		this.mappingLocations = mappingLocations;
	}
	


}
