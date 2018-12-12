package hpcgateway.wp.orm.beans;

public enum ConnectType {
	SSH(1000,"SSH","安全Shell连接"),
	HTTP(1001,"HTTP","HTTP");
	
	private int code;
	private String name;
	private String desc;
	private ConnectType(int code,String name,String desc)
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
