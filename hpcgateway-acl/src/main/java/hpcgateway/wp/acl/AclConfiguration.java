package hpcgateway.wp.acl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class AclConfiguration
{
	private String module;
	protected Map<String,Script> scripts;
	protected List<ScriptMapping> scriptMappings;

	public AclConfiguration() {
		this.module = null;
		this.scripts = new TreeMap<String,Script>();
		this.scriptMappings = new ArrayList<ScriptMapping>();
	}
	
	public String getModule() {
		return module;
	}
	
	public void setModule(String name)
	{
		this.module = name;
	}
	
	public void addScriptMapping(ScriptMapping scriptMapping)
	{
		if( this.scriptMappings == null )
		{
			this.scriptMappings = new ArrayList<ScriptMapping>();
		}
		scriptMappings.add(scriptMapping);
	}

	
	public void addScript(Script script)
	{		
		if( this.scripts == null )
		{
			this.scripts = new TreeMap<String,Script>();
		}

		if( script.getName() == null || script.getName().isEmpty() ) 
		{
			script.setName(module);
		}

		scripts.put(script.getName(),script);
	}
	
	public Collection<Script> getScriptList()
	{
		return scripts==null ? null : scripts.values();
	}


	public List<ScriptMapping> getScriptMappings()
	{
		return scriptMappings;
	}

	public void setScriptMappings(List<ScriptMapping> scriptMappings)
	{
		this.scriptMappings = scriptMappings;
	}

	public void setScripts(Map<String, Script> scripts)
	{
		this.scripts = scripts;
	}

	public Map<String, Script> getScripts()
	{
		return scripts;
	}

	public static AclConfiguration load(String topdir,String filename) throws Exception
	{
		File file = new File(topdir,filename);
		if( !file.exists() )
		{
			throw new Exception("The ACL configuration file does not exist, "+file.getAbsolutePath());
		}

		XStream xstream = new XStream(new DomDriver("UTF-8"));
		xstream.alias("acl", AclConfiguration.class);
		xstream.registerConverter(new AclConfigConverter(topdir));
		AclConfiguration aclConfiguration = (AclConfiguration)xstream.fromXML(new FileReader(file));
		return aclConfiguration;
	}
	
	public static class AclConfigConverter implements Converter
	{
		/*
		 * constant
		 */
		final public static String NODE_SCRIPTS = "scripts";
		final public static String NODE_SCRIPT = "script";
		final public static String NODE_AUTHORIZATIONS = "authorizations";
		final public static String NODE_AUTHORIZATION = "authorization";
		/*
		 * logger
		 */
		private Logger logger = Logger.getLogger(AclConfigConverter.class);
		/*
		 * topdir
		 */
		private String topdir;
		
		public AclConfigConverter(String topdir)
		{
			this.topdir = topdir;
		}

		@Override
		public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz)
		{
			return AclConfiguration.class.equals(clazz);
		}

		@Override
		public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2)
		{
			throw new java.lang.UnsupportedOperationException("The XML writer is not support");
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
		{
			AclConfiguration aclConfiguration = new AclConfiguration();
			Map<String,Script> scripts = new TreeMap<String,Script>();
			List<ScriptMapping> list = new ArrayList<ScriptMapping>();
			
			while( reader.hasMoreChildren() )
			{
				reader.moveDown();
				// under the <acl>
				String nodeName = reader.getNodeName();
				if( nodeName.compareTo(NODE_SCRIPTS) == 0 )
				{
					// process the <scripts>
					while( reader.hasMoreChildren() )
					{
						reader.moveDown();
						// under the <scripts>
						String scriptNodeName = reader.getNodeName();
						if( scriptNodeName.compareTo(NODE_SCRIPT) != 0 )
						{
							continue;
						}
						
						// process the <script>
						String scriptName = null;
						String type = null;
						String file = null;
						while( reader.hasMoreChildren() )
						{
							reader.moveDown();
							// under the <script>
							String propertyName = reader.getNodeName();
							String propertyValue = reader.getValue();
							if( propertyName.compareTo("name") == 0 )
							{
								scriptName = propertyValue;
							}
							else if( propertyName.compareTo("type") == 0 )
							{
								type = propertyValue;
							}
							else if( propertyName.compareTo("file") == 0 )
							{
								file = propertyValue;
							}
							reader.moveUp();
						}
						if( scriptName!=null && !scriptName.isEmpty() && type!=null && !type.isEmpty() && file!=null && !file.isEmpty() )
						{
							try
							{
								String filename = String.format("%s%s%s",topdir,File.separator,file);
								Script script = ScriptFactory.createScript(type, scriptName, filename);
								scripts.put(script.getName(), script);
							}
							catch(Exception e)
							{
								String m = String.format("Creating Script is failed for type:%s name:%s file:%s",type,scriptName,file);
								logger.error(m,e);
							}
						}
						reader.moveUp();
					}
				}
				else if( nodeName.compareTo(NODE_AUTHORIZATIONS) == 0 )
				{
					// process the <authorizations>
					while( reader.hasMoreChildren() )
					{
						reader.moveDown();
						// under the <authorizations>

						String authorizationNodeName = reader.getNodeName();
						if( authorizationNodeName.compareTo(NODE_AUTHORIZATION) != 0 )
						{
							continue;
						}

						String re = null;
						String scriptName = null;
						while( reader.hasMoreChildren() )
						{
							reader.moveDown();
							// under the <authorization>
							String propertyName = reader.getNodeName();
							String propertyValue = reader.getValue();
							if( propertyName.compareTo("urlpattern") == 0 ) {
								re = propertyValue;
							}
							else if( propertyName.compareTo("script") == 0 ) {
								scriptName = propertyValue;
							}
							reader.moveUp();
						}
						if( re!=null && !re.isEmpty() && scriptName!=null && !scriptName.isEmpty() )
						{
							ScriptMapping scriptMapping = new ScriptMapping();
							scriptMapping.setScriptName(scriptName);
							scriptMapping.setUrlPattern(re);
							list.add(scriptMapping);
						}
						reader.moveUp();
					}
				}
				reader.moveUp();
			}
			
			List<ScriptMapping> scriptMappings = new ArrayList<ScriptMapping>();
			for(ScriptMapping scriptMapping : list)
			{
				String scriptName = scriptMapping.getScriptName();
				if( !scripts.containsKey(scriptName) )
				{
					logger.error(String.format("Script name %s was not found in <scripts>",scriptName));
					continue;
				}
				scriptMappings.add(scriptMapping);				
			}

			
			aclConfiguration.setScripts(scripts);
			aclConfiguration.setScriptMappings(scriptMappings);
			
			return aclConfiguration;
		}
	}
}
