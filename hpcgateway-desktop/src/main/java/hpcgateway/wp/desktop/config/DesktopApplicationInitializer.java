package hpcgateway.wp.desktop.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.WebAppRootListener;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.ClassPathResource;

import hpcgateway.wp.orm.h2.H2dbListener;

@Order(1)
public class DesktopApplicationInitializer implements WebApplicationInitializer
{
	final public static String KEY_H2_STARTUP = "h2.startup";
	final public static String KEY_H2_PORT = "h2.port";
	
	final public static String DEFAULT_H2_PORT = "8043";
	final public static String DEFAULT_H2_STARTUP = "yes";
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException
	{		
		// listener 
		servletContext.addListener(WebAppRootListener.class);
		
		// filter
		FilterRegistration.Dynamic encoding = servletContext.addFilter("encodingFilter",CharacterEncodingFilter.class);
		encoding.setInitParameter("encoding", "UTF-8");
		encoding.setInitParameter("forceEncoding", "true");
		encoding.addMappingForUrlPatterns(null, true, "/wp/*");
		
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(DesktopConfig.class);
		
		// Startup spring servlet
	    DispatcherServlet desktop = new DispatcherServlet(appContext);
	    ServletRegistration.Dynamic desktopDynamic = servletContext.addServlet("desktop",desktop);
	    desktopDynamic.setLoadOnStartup(1);
	    desktopDynamic.addMapping("/wp/*");    
	    
		Properties props = null;		
		try 
		{
			ClassPathResource cr = new ClassPathResource("/WEB-INF/classes/config/h2.properties");
			props = PropertiesLoaderUtils.loadProperties(cr);		
		} 
		catch (Exception ex)
		{
			System.out.println("DesktopApplicationInitializer: An error occured when metadb.properties is reading, "+ex.getMessage());
		}

		String dbPort = props==null ? DEFAULT_H2_PORT : props.getProperty(KEY_H2_PORT,DEFAULT_H2_PORT);
		String dbStartup = props==null ? DEFAULT_H2_STARTUP : props.getProperty(KEY_H2_STARTUP, DEFAULT_H2_STARTUP);
		
		if(  dbStartup.compareToIgnoreCase("yes") == 0 )
		{
			servletContext.setInitParameter("h2.tcpPort", dbPort);
			servletContext.addListener(H2dbListener.class);  
		    // startup servlet for H2 console 
		    org.h2.server.web.WebServlet h2console = new org.h2.server.web.WebServlet();
		    ServletRegistration.Dynamic h2consoleDynamic = servletContext.addServlet("h2console",h2console);
		    h2consoleDynamic.setLoadOnStartup(2);
		    h2consoleDynamic.addMapping("/h2/*");
		}

	}
}
