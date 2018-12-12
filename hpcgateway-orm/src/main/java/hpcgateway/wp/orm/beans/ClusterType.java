package hpcgateway.wp.orm.beans;

public enum ClusterType 
{
	CoS(1000,"CoS","CoS cluster system")
	;
	private int code;
	private String name;
	private String desc;
	private ClusterType(int code,String name,String desc)
	{
		this.code = code;
		this.name = name;
		this.desc = desc;
	}
	
	public int getCode()
	{
		return code;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
}
