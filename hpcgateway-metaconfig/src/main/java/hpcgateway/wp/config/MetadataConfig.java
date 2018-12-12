package hpcgateway.wp.config;


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import hpcgateway.wp.acl.AclConfiguration;
import hpcgateway.wp.acl.DefaultInterceptor;
import hpcgateway.wp.acl.Script;
import hpcgateway.wp.metaconfig.config.MetadataDatabaseConfiguration;
import hpcgateway.wp.metaconfig.config.MetadataHibernateConfiguration;
import hpcgateway.wp.metaconfig.controller.MetadataController;
import hpcgateway.wp.metaconfig.config.MetadataAclConfiguration;

import org.apache.commons.lang.text.StrSubstitutor;

@Configuration
@PropertySource(value="classpath:config/hpcgateway/wp/metaconfig/metadata.properties")
@ComponentScan(basePackages="hpcgateway.wp.metaconfig.config,hpcgateway.wp.metaconfig.dao.hibernate,hpcgateway.wp.metaconfig.controller,hpcgateway.wp.metaconfig.service")
public class MetadataConfig
{
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MetadataDatabaseConfiguration metadataDatabaseConfiguration;
	@Autowired
	private MetadataHibernateConfiguration metadataHibernateConfiguration;
	
	@Autowired
	public MetadataConfig(DefaultInterceptor desktopInterceptor,MetadataAclConfiguration metadataAclConfiguration) throws Exception
	{
		AclConfiguration aclConfiguration = metadataAclConfiguration.getAclConfiguration();
		desktopInterceptor.configAcl(MetadataController.class, aclConfiguration);
	}

	
	@Bean(name="hgMetaConfigDataSource",destroyMethod="close")
	public DataSource dataSource() throws Exception
	{
		
		//MetadataDatabaseConfiguration metadataDatabaseConfiguration = metadataConfiguration.getDb();
		PoolProperties poolProperties = new PoolProperties();
				
		//System.out.print
		//String webroot = System.getProperty(webRootKey);
		String dbUrl = metadataDatabaseConfiguration.getUrl();
		String dbDriver = metadataDatabaseConfiguration.getDriver();
		String dbUsername = metadataDatabaseConfiguration.getUsername();
		String dbPassword = metadataDatabaseConfiguration.getPassword();
		Integer dbPoolInitialSize = metadataDatabaseConfiguration.getPoolInitialSize();
		Integer dbPoolMaxActive = metadataDatabaseConfiguration.getPoolMaxActive();
		Integer dbPoolMaxIdle = metadataDatabaseConfiguration.getPoolMaxIdle();
		Integer dbPoolMinIdle = metadataDatabaseConfiguration.getPoolMinIdel();
		Integer dbPoolMaxWait = metadataDatabaseConfiguration.getPoolMaxWait();
		Boolean dbPoolRemoveAbandoned = metadataDatabaseConfiguration.getPoolRemoveAbandoned();
		Integer dbPoolRemoveAbandonedTimeout = metadataDatabaseConfiguration.getPoolRemoveAbandonedTimeout();
		
		String url = new StrSubstitutor(System.getProperties()).replace(dbUrl);
		
		poolProperties.setUrl(url);
		poolProperties.setDriverClassName(dbDriver);
		poolProperties.setUsername(dbUsername);
		poolProperties.setPassword(dbPassword);
		
		if( dbPoolInitialSize != null )
		{
			poolProperties.setInitialSize(dbPoolInitialSize);
		}
		if( dbPoolMaxActive != null )
		{
			poolProperties.setMaxActive(dbPoolMaxActive);
		}
		if( dbPoolMaxIdle != null )
		{
			poolProperties.setMaxIdle(dbPoolMaxIdle);
		}
		if( dbPoolMinIdle != null )
		{
			poolProperties.setMinIdle(dbPoolMinIdle);
		}
		if( dbPoolMaxWait != null )
		{
			poolProperties.setMaxWait(dbPoolMaxWait);
		}
		if( dbPoolRemoveAbandoned != null )
		{
			poolProperties.setRemoveAbandoned(dbPoolRemoveAbandoned);
		}
		if( dbPoolRemoveAbandonedTimeout != null )
		{
			poolProperties.setRemoveAbandonedTimeout(dbPoolRemoveAbandonedTimeout);
		}

		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
	    
		dataSource.setPoolProperties(poolProperties);  
		
		return dataSource;
	}

	@Bean(name="hgMetaConfigSessionFactory")
	public SessionFactory sessionFactory() throws Exception
	{
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		//MetadataHibernateConfiguration metadataHibernateConfiguration = metadataConfiguration.getHibernate();
		/*
		 * setup the datasource
		 */
		localSessionFactoryBean.setDataSource(this.dataSource());
		
		/*
		 * setup hibernate's properties
		 */
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", metadataHibernateConfiguration.getDialect());
		hibernateProperties.setProperty("hibernate.connection.autocommit", metadataHibernateConfiguration.getAutoCommit());
		hibernateProperties.setProperty("hibernate.show_sql",metadataHibernateConfiguration.getShowSql());
		hibernateProperties.setProperty("hibernate.connection.release_mode",metadataHibernateConfiguration.getReleaseMode());
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto",metadataHibernateConfiguration.getHbm2ddl());		
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		/*
		 * setup hibernate's class mapping 
		 */
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(defaultResourceLoader);
		org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(metadataHibernateConfiguration.getMappingLocations());
		localSessionFactoryBean.setMappingLocations(resources);
		
		/*
		 * Create real sessionFactory
		 */
		localSessionFactoryBean.afterPropertiesSet();
		/*
		 * return the sessionFactory
		 */
		return localSessionFactoryBean.getObject();
	}
	
	@Bean(name="hgMetaConfigTransactionManager")
	public HibernateTransactionManager transactionManager() throws Exception
	{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(this.sessionFactory());
		return transactionManager;
	}
}
