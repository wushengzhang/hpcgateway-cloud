package hpcgateway.wp.acl;


public class ScriptFactory 
{
	static public Script createScript(String name,String filename) throws Exception
	{
		ScriptType[] types = ScriptType.class.getEnumConstants();
		ScriptType type = null;
		for( ScriptType t : types)
		{
			if( filename.endsWith(t.getSuffix()) )
			{
				type = t;
				break;
			}
		}
		if( type == null ) {
			throw new Exception("Probing script type failed from filename "+filename);
		}
		return createScript(type,name,filename);
	}

	static public Script createScript(String typeName,String name,String filename) throws Exception
	{
		if( typeName == null || typeName.isEmpty() )
		{
			return createScript(name,filename);
		}
		
		ScriptType[] types = ScriptType.class.getEnumConstants();
		ScriptType type = null;
		for( ScriptType t : types)
		{
			if( t.name().compareToIgnoreCase(typeName) == 0 )
			{
				type = t;
				break;
			}
		}
		if( type == null ) {
			throw new Exception("Probing script type failed for type "+typeName);
		}
		return createScript(type,name,filename);
	
	}
	
	static public Script createScript(ScriptType type,String name,String filename) throws Exception
	{
		if( type == null ) {
			type = ScriptType.Groovy;
		}
		if( type == ScriptType.Groovy )
		{
			return new GroovyScript(name,filename);
		}
		
		throw new Exception("Unsupport script type "+type.name());
	}
	
	
}
