package hpcgateway.wp.metaconfig.config;


import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hpcgateway.wp.acl.AclConfiguration;


@Component("metadataAclConfiguration")
public class MetadataAclConfiguration implements InitializingBean
{
	final public static String DEFAULT_MODULE_NAME = "metadata";
	final public static String DEFAULT_CONFIG_PATH = "/WEB-INF/acl/hpcgateway/wp/metaconfig/metadata.xml";

	@Value("${metaconfig.modname:}") String modname;
	@Value("${metaconfig.acl.config:}") String configFilename;

	@Autowired
	private ServletContext servletContext;
	
	private AclConfiguration aclConfiguration;
		
	public MetadataAclConfiguration()
	{
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if( modname == null || modname.isEmpty() )
		{
			modname = DEFAULT_MODULE_NAME;
		}
		
		/*
		 * setup config filename
		 */
		if( configFilename == null || configFilename.isEmpty() )
		{
			configFilename = DEFAULT_CONFIG_PATH;
		}

		String topdir = servletContext.getRealPath("/");
		
		/*
		 * load configuration
		 */
		this.aclConfiguration = AclConfiguration.load(topdir,configFilename);	
		this.aclConfiguration.setModule(modname);
	}

	public AclConfiguration getAclConfiguration()
	{
		return aclConfiguration;
	}

	public void setAclConfiguration(AclConfiguration aclConfiguration)
	{
		this.aclConfiguration = aclConfiguration;
	}
	
	
}
