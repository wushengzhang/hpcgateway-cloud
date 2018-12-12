package hpcgateway.wp.orm.beans;

public class Resource
{
	private Long id;
	private String type;
	private String unit;
	private String limit;
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String tupe)
	{
		this.type = tupe;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getLimit()
	{
		return limit;
	}
	public void setLimit(String limit)
	{
		this.limit = limit;
	}
	

}
