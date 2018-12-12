package hpcgateway.wp.acl;

import java.util.Map;
import java.util.Properties;

public class RequestInfo 
{
	private String url;
	private String method;
	private String port;
	private String protocol;
	private Map<String,String[]> parameters;
	private Properties properties;
	
	
	public String getPort()
	{
		return port;
	}
	public void setPort(String port)
	{
		this.port = port;
	}
	public String getProtocol()
	{
		return protocol;
	}
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}

	public Map<String, String[]> getParameters()
	{
		return parameters;
	}
	public void setParameters(Map<String, String[]> parameters)
	{
		this.parameters = parameters;
	}
	public Properties getProperties()
	{
		return properties;
	}
	public String getProperty(String key)
	{
		if( properties!=null && properties.containsKey(key) )
		{
			return properties.getProperty(key);
		}
		return null;
	}
	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
	public void addProperty(String key,String value)
	{
		if( this.properties == null )
		{
			this.properties = new Properties();
		}
		this.properties.setProperty(key, value);
	}
	
	
	
}
