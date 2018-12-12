package hpcgateway.wp.core.page;

public class TreeNodeDefine
{
	private String name;
	private String identifier;
	private String pathFormat;
	private boolean isParent;
	
	public TreeNodeDefine(String identifier,String name,String pathFormat,boolean isParent)
	{
		this.name = name;
		this.identifier = identifier;
		this.pathFormat = pathFormat;
		this.isParent = isParent;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	public String getPath(Object ...args)
	{
		return String.format(pathFormat, args);
	}
	public boolean isParent()
	{
		return isParent;
	}
	public void setParent(boolean isParent)
	{
		this.isParent = isParent;
	}

	public String getPathFormat()
	{
		return pathFormat;
	}

	public void setPathFormat(String pathFormat)
	{
		this.pathFormat = pathFormat;
	}
	
	
}
