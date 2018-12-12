package hpcgateway.wp.desktop.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import hpcgateway.wp.acl.DefaultInterceptor;
import hpcgateway.wp.acl.Script;
import hpcgateway.wp.acl.ScriptFactory;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config/servlet.properties")
@ComponentScan(basePackages="hpcgateway.wp.config,hpcgateway.wp.desktop.controller,hpcgateway.wp.desktop.service")
public class DesktopConfig extends WebMvcConfigurationSupport
{
	private Logger logger = Logger.getLogger(this.getClass());

	@Value("${servlet.acl.prefix:}") String prefix;
	@Value("${servlet.acl.urls}") String exemptionUrls;
	@Value("${servlet.acl.prefixes}") String exemptionPrefixes;
	@Value("${servlet.acl.defaultScriptType}") String defaultScriptType;
	@Value("${servlet.acl.defaultScriptName}") String defaultScriptName;
	@Value("${servlet.acl.defaultScriptPath}") String defaultScriptPath;
	@Value("${servlet.acl.pagePrefixPath}") String pagePrefixPath;
	@Value("${servlet.acl.loginPageName}") String loginPageName;

	@Autowired
	private ServletContext servletContext;
	
	@Bean(name="desktopInterceptor")
	public DefaultInterceptor desktopInterceptor()
	{
		DefaultInterceptor desktopInterceptor = new DefaultInterceptor();
		
		desktopInterceptor.setPrefix(prefix);
		desktopInterceptor.setLoginPageName(loginPageName);
		desktopInterceptor.setPagePrefix(pagePrefixPath);

		/*
		 * register exemption urls
		 */
		if( exemptionUrls != null && !exemptionUrls.isEmpty() )
		{
			String[] urls = exemptionUrls.split(",");
			for(String url : urls) 
			{
				desktopInterceptor.registerExemptUrl(url);
			}
		}
		
		/*
		 * register exemption prefixes
		 */
		if( exemptionPrefixes != null && !exemptionPrefixes.isEmpty() )
		{
			String[] prefixes = exemptionPrefixes.split(",");
			for(String prefix : prefixes)
			{
				desktopInterceptor.registerExemptPrefix(prefix);
			}
		}
		
		/*
		 * register default script
		 */
		String topdir = servletContext.getRealPath(defaultScriptPath);
		File file = new File(topdir,defaultScriptName);

		try
		{
			Script defaultScript = ScriptFactory.createScript(defaultScriptType, "default", file.getAbsolutePath());
			desktopInterceptor.setDefaultScript(defaultScript);
			desktopInterceptor.afterPropertiesSet();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return desktopInterceptor;
	}
	
	@Bean
	public ViewResolver viewResolver()
	{
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public RequestMappingHandlerMapping handlerMapping()
	{
		return new RequestMappingHandlerMapping();
	}
	
	@Bean
	public RequestMappingHandlerAdapter handlerAdapter()
	{
		RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
		
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("text/html;charset=UTF-8,application/json;charset=UTF-8"));
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jackson2HttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("text/html;charset=UTF-8,application/json;charset=UTF-8"));
		
		messageConverters.add(stringHttpMessageConverter);
		messageConverters.add(jackson2HttpMessageConverter);

		handlerAdapter.setMessageConverters(messageConverters);
		
		return handlerAdapter;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{  
		registry.addInterceptor(desktopInterceptor()).addPathPatterns("/**");  
		super.addInterceptors(registry);
	} 
}
