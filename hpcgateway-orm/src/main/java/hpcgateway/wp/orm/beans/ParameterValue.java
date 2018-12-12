package hpcgateway.wp.orm.beans;

import java.util.Date;

public class ParameterValue extends Parameter
{
	protected String value;
	protected Date time;
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public Date getTime()
	{
		return time;
	}
	public void setTime(Date time)
	{
		this.time = time;
	}
	
}
