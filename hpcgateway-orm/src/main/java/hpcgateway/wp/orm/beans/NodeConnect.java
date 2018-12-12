package hpcgateway.wp.orm.beans;

public class NodeConnect extends Node
{
	private Long Id;
	private String Url;
	private Integer Port;
	private String User;
	private String Pass;
	private String Crypt;
	private ConnectType type;
	
	public Long getNodeId()
	{
		return super.id;
	}
	
	public void setNodeId(Long id)
	{
		super.id = id;
	}
	
	public Long getId()
	{
		return Id;
	}
	public void setId(Long id)
	{
		Id = id;
	}
	public String getUrl()
	{
		return Url;
	}
	public void setUrl(String url)
	{
		Url = url;
	}
	public Integer getPort()
	{
		return Port;
	}
	public void setPort(Integer port)
	{
		Port = port;
	}
	public String getUser()
	{
		return User;
	}
	public void setUser(String user)
	{
		User = user;
	}
	public String getPass()
	{
		return Pass;
	}
	public void setPass(String pass)
	{
		Pass = pass;
	}
	public String getCrypt()
	{
		return Crypt;
	}
	public void setCrypt(String crypt)
	{
		Crypt = crypt;
	}
	public ConnectType getType()
	{
		return type;
	}
	
	public Integer getTypeCode()
	{
		return type==null ? null : type.getCode();
	}
	
	public void setTypeCode(Integer code) throws Exception
	{
		this.type = code==null ? null : TypeHelper.findType(ConnectType.class, code);
	}
	
	
}
