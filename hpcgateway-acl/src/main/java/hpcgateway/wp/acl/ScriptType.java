package hpcgateway.wp.acl;

public enum ScriptType
{
	Groovy(".groovy");
	private String suffix;
	private ScriptType(String suffix)
	{
		this.suffix = suffix;
	}
	
	public String getSuffix()
	{
		return this.suffix;
	}
}
