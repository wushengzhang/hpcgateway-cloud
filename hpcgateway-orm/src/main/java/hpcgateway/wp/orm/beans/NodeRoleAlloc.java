package hpcgateway.wp.orm.beans;

import java.util.Date;


public class NodeRoleAlloc
{
	private Long id;
	private String name;
	private Integer priority;
	private String description;
	private Date time;
	private NodeRole role;
	
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
	public Integer getPriority()
	{
		return priority;
	}
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Date getTime()
	{
		return time;
	}
	public void setTime(Date time)
	{
		this.time = time;
	}
	public Integer getRoleCode()
	{
		if( role == null )
			return null;
		return role.getCode();
	}
	public void setRoleCode(Integer code) throws Exception
	{
		if( code == null )
		{
			this.role = null;
			return;
		}
		this.role = TypeHelper.findType(NodeRole.class, code);
	}
	public NodeRole getRole()
	{
		return role;
	}
	public void setRole(NodeRole role)
	{
		this.role = role;
	}
	
	public String getRoleName()
	{
		return role==null ? null : role.getName();
	}
	
	public String getRoleDesc()
	{
		return role==null ? null : role.getDesc();
	}
	
}
