package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

/**
 * HpcConnect generated by hbm2java
 */
public class HpcConnect implements java.io.Serializable
{

	private Long hpcConnId;
	private HpcConnectType hpcConnectType;
	private HpcNode hpcNode;
	private String hpcConnUrl;
	private Integer hpcConnPort;
	private String hpcConnUser;
	private String hpcConnPass;
	private String hpcConnCrypt;

	public HpcConnect()
	{
	}

	public HpcConnect(HpcConnectType hpcConnectType, HpcNode hpcNode, String hpcConnUrl, Integer hpcConnPort, String hpcConnUser, String hpcConnPass, String hpcConnCrypt)
	{
		this.hpcConnectType = hpcConnectType;
		this.hpcNode = hpcNode;
		this.hpcConnUrl = hpcConnUrl;
		this.hpcConnPort = hpcConnPort;
		this.hpcConnUser = hpcConnUser;
		this.hpcConnPass = hpcConnPass;
		this.hpcConnCrypt = hpcConnCrypt;
	}

	public Long getHpcConnId()
	{
		return this.hpcConnId;
	}

	public void setHpcConnId(Long hpcConnId)
	{
		this.hpcConnId = hpcConnId;
	}

	public HpcConnectType getHpcConnectType()
	{
		return this.hpcConnectType;
	}

	public void setHpcConnectType(HpcConnectType hpcConnectType)
	{
		this.hpcConnectType = hpcConnectType;
	}

	public HpcNode getHpcNode()
	{
		return this.hpcNode;
	}

	public void setHpcNode(HpcNode hpcNode)
	{
		this.hpcNode = hpcNode;
	}

	public String getHpcConnUrl()
	{
		return this.hpcConnUrl;
	}

	public void setHpcConnUrl(String hpcConnUrl)
	{
		this.hpcConnUrl = hpcConnUrl;
	}

	public Integer getHpcConnPort()
	{
		return this.hpcConnPort;
	}

	public void setHpcConnPort(Integer hpcConnPort)
	{
		this.hpcConnPort = hpcConnPort;
	}

	public String getHpcConnUser()
	{
		return this.hpcConnUser;
	}

	public void setHpcConnUser(String hpcConnUser)
	{
		this.hpcConnUser = hpcConnUser;
	}

	public String getHpcConnPass()
	{
		return this.hpcConnPass;
	}

	public void setHpcConnPass(String hpcConnPass)
	{
		this.hpcConnPass = hpcConnPass;
	}

	public String getHpcConnCrypt()
	{
		return this.hpcConnCrypt;
	}

	public void setHpcConnCrypt(String hpcConnCrypt)
	{
		this.hpcConnCrypt = hpcConnCrypt;
	}

}