package hpcgateway.wp.orm.beans;

import java.util.List;

public class CloudGroup
{
	private Long id;
	private Long parentId;
	private String parentName;
	private String name;
	private String code;
	private String desc;
	private List<CloudUser> users;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getParentId()
	{
		return parentId;
	}
	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}
	public String getParentName()
	{
		return parentName;
	}
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
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
	public List<CloudUser> getUsers()
	{
		return users;
	}
	public void setUsers(List<CloudUser> users)
	{
		this.users = users;
	}
	
	
}
