package hpcgateway.wp.orm.beans;

public class Cluster
{
	private Long id;
	private ClusterType type;
	private String name;
	private String desc;
	private String username;
	private String password;
	private String identifier;
	private String realname;
	private String email;
	private String phone;
	private String wechat;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getTypeCode()
	{
		return type==null ? null : type.getCode();
	}

	public void setTypeCode(Integer code) throws Exception
	{
		this.type = code==null ? null : TypeHelper.findType(ClusterType.class, code);
	}
	
	public String getTypeName()
	{
		return type==null ? null : type.getName();
	}
	
	public ClusterType getType()
	{
		return this.type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = realname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getWechat()
	{
		return wechat;
	}

	public void setWechat(String wechat)
	{
		this.wechat = wechat;
	}

}
