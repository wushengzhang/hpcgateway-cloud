package hpcgateway.wp.acl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GroovyScript implements Script
{
	private String name;
	private String filename;
	
	public GroovyScript(String name,String filename)
	{
		this.name = name;
		this.filename = filename;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public ScriptType scriptType()
	{
		return ScriptType.Groovy;
	}

	@Override
	public Map<String,Object> execute(Authentication authentication,RequestInfo request) throws Exception 
	{
		File file = new File(filename);
		if( !file.exists() ) 
		{
			throw new Exception(String.format("Script %s is not exist.",file.getAbsolutePath()));
		}
		else if( !file.isFile() ) 
		{
			throw new Exception(String.format("Script %s is not a normal file",file.getAbsolutePath()));
		}
		else if( !file.getName().endsWith(ScriptType.Groovy.getSuffix()) )
		{
			throw new Exception(String.format("Script %s is not a groovy program", file.getAbsolutePath()));
		}
		
		System.out.println("$$$$$$$ setup script => "+file.getAbsolutePath());

				
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("groovy");
		Bindings bindings = engine.createBindings();
		bindings.put("authentication", authentication);
		bindings.put("request", request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		try
		{
			Object r = engine.eval(reader, bindings);
			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>)r;
			return map;
		}
		finally
		{
			reader.close();
		}
	}

}
