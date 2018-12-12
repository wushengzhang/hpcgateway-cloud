package hpcgateway.wp.orm.beans;

public class Connect
{
	private Long id;
	private String url;
	private Integer port;
	private String user;
	private String pass;
	private String crypt;
	private ConnectType type;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public String getCrypt()
	{
		return crypt;
	}

	public void setCrypt(String crypt)
	{
		this.crypt = crypt;
	}

	public void setType(ConnectType type)
	{
		this.type = type;
	}

	public Integer getTypeCode()
	{
		return type==null ? null : type.getCode();
	}

	public ConnectType getType()
	{
		return type;
	}
	
	public void setTypeCode(Integer type) throws Exception
	{
		this.type = TypeHelper.findType(ConnectType.class, type);
	}

	public String getTypeName()
	{
		return type==null ? null : type.getName();
	}

}
