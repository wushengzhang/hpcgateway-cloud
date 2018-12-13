package hpcgateway.wp.config.apps.materials;

import java.util.Properties;
import javax.sql.DataSource;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.commons.lang.text.StrSubstitutor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
//import org.prcode.utility.util.exception.DESException;



import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

//@EnableAspectJAutoProxy
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages="hpcgateway.wp.apps.materials.dao.mybatis,hpcgateway.wp.apps.materials.controller,hpcgateway.wp.apps.materials.service,hpcgateway.wp.apps.materials.mappers,hpcgateway.wp.apps.materials.beans")
@PropertySource("classpath:config/hpcgateway/wp/apps/materials/app.properties")
public class MaterialsConfig
{
	private Logger logger = Logger.getLogger(this.getClass());

	@Value("${apps.materials.db.url}") String dbUrl;
	@Value("${apps.materials.db.username}") String dbUsername;
	@Value("${apps.materials.db.password}") String dbPassword;
	@Value("${apps.materials.db.cacheprepstmts") String cachePrepStmts;
	@Value("${apps.materials.db.prepstmtcachesize") String prepStmtCacheSize;
	@Value("${apps.materials.db.prepstmtcachesqllimit") String prepStmtCacheSqlLimit;
	//@Value("${hpcgateway.db.driver}") String dbDriver;

	//jdbcUrl=jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8
	//username=root
	//password=admin

	public MaterialsConfig()
	{
	}

	@Bean(name="hgAppsMaterilsDataSource",destroyMethod="close")
	public DataSource dataSource() throws Exception
	{
		DataSource ds = null;
		HikariConfig hc = new HikariConfig();

		this.dbUrl = new StrSubstitutor(System.getProperties()).replace(this.dbUrl);
		hc.setJdbcUrl( this.dbUrl );
		hc.setUsername( this.dbUsername );
		hc.setPassword( this.dbPassword );
		hc.addDataSourceProperty("cachePrepStmts" , cachePrepStmts);
		hc.addDataSourceProperty("prepStmtCacheSize" , prepStmtCacheSize);
		hc.addDataSourceProperty("prepStmtCacheSqlLimit" , prepStmtCacheSqlLimit);

		ds = new HikariDataSource(hc);

		return ds;
	}

	@Bean(name="hgAppsMaterialsTransactionManager")
	public DataSourceTransactionManager transactionManager() throws Exception
	{
		System.out.println("DataSourceTransactionManager");
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(name="hgAppsMaterialsSessionFactory")
	public SqlSessionFactory sessionFactory() throws Exception
	{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource());
		PageInterceptor pageHelper = new PageInterceptor();
		Properties props = new Properties();
		//props.setProperty("dialect", "h2");
		props.setProperty("reasonable", "true");
		props.setProperty("supportMethodsArguments", "true");
		props.setProperty("returnPageInfo", "check");
		props.setProperty("params", "count=countSql");
		pageHelper.setProperties(props);

		sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
		//PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		//sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPERXML_LOCATION));

		System.out.println("session factory");
		return sqlSessionFactoryBean.getObject();
	}

/*
	@Bean(name="hgAppsMaterialsTransactionManager")
	public HibernateTransactionManager transactionManager() throws Exception
	{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(this.sessionFactory());
		return transactionManager;
	}
*/
}
