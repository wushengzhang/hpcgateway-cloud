package hpcgateway.wp.orm.beans;

public class Role 
{
	protected Long id;
	protected String name;
	protected int code;
	protected String desc;
	protected String rights;
	

	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public String getRights()
	{
		return rights;
	}
	public void setRights(String rights)
	{
		this.rights = rights;
	}
	

}
