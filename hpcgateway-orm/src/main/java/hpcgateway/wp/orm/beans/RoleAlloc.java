package hpcgateway.wp.orm.beans;

import java.util.Date;

public class RoleAlloc extends Role
{
	private Long id;
	private Date time;
	private Date start;
	private Date expire;

	public Long getRoleId()
	{
		return super.getId();
	}
	public void setRoleId(Long id)
	{
		super.id = id;
	}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Date getTime()
	{
		return time;
	}
	public void setTime(Date time)
	{
		this.time = time;
	}
	public Date getStart()
	{
		return start;
	}
	public void setStart(Date start)
	{
		this.start = start;
	}
	public Date getExpire()
	{
		return expire;
	}
	public void setExpire(Date expire)
	{
		this.expire = expire;
	}	
}
