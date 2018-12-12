package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

/**
 * ColNet generated by hbm2java
 */
public class ColNet implements java.io.Serializable
{

	private Long colNetId;
	private ColInfo colInfo;
	private String colNetName;
	private String colNetIdentifier;
	private String colNetIp;
	private String colNetBootproto;
	private String colNetOnboot;
	private String colNetNetmask;
	private String colNetMtu;
	private String colNetBroadcast;
	private String colNetGateway;
	private String colNetHostnamealias;

	public ColNet()
	{
	}

	public ColNet(String colNetName, String colNetIdentifier)
	{
		this.colNetName = colNetName;
		this.colNetIdentifier = colNetIdentifier;
	}

	public ColNet(ColInfo colInfo, String colNetName, String colNetIdentifier, String colNetIp, String colNetBootproto, String colNetOnboot, String colNetNetmask, String colNetMtu, String colNetBroadcast, String colNetGateway, String colNetHostnamealias)
	{
		this.colInfo = colInfo;
		this.colNetName = colNetName;
		this.colNetIdentifier = colNetIdentifier;
		this.colNetIp = colNetIp;
		this.colNetBootproto = colNetBootproto;
		this.colNetOnboot = colNetOnboot;
		this.colNetNetmask = colNetNetmask;
		this.colNetMtu = colNetMtu;
		this.colNetBroadcast = colNetBroadcast;
		this.colNetGateway = colNetGateway;
		this.colNetHostnamealias = colNetHostnamealias;
	}

	public Long getColNetId()
	{
		return this.colNetId;
	}

	public void setColNetId(Long colNetId)
	{
		this.colNetId = colNetId;
	}

	public ColInfo getColInfo()
	{
		return this.colInfo;
	}

	public void setColInfo(ColInfo colInfo)
	{
		this.colInfo = colInfo;
	}

	public String getColNetName()
	{
		return this.colNetName;
	}

	public void setColNetName(String colNetName)
	{
		this.colNetName = colNetName;
	}

	public String getColNetIdentifier()
	{
		return this.colNetIdentifier;
	}

	public void setColNetIdentifier(String colNetIdentifier)
	{
		this.colNetIdentifier = colNetIdentifier;
	}

	public String getColNetIp()
	{
		return this.colNetIp;
	}

	public void setColNetIp(String colNetIp)
	{
		this.colNetIp = colNetIp;
	}

	public String getColNetBootproto()
	{
		return this.colNetBootproto;
	}

	public void setColNetBootproto(String colNetBootproto)
	{
		this.colNetBootproto = colNetBootproto;
	}

	public String getColNetOnboot()
	{
		return this.colNetOnboot;
	}

	public void setColNetOnboot(String colNetOnboot)
	{
		this.colNetOnboot = colNetOnboot;
	}

	public String getColNetNetmask()
	{
		return this.colNetNetmask;
	}

	public void setColNetNetmask(String colNetNetmask)
	{
		this.colNetNetmask = colNetNetmask;
	}

	public String getColNetMtu()
	{
		return this.colNetMtu;
	}

	public void setColNetMtu(String colNetMtu)
	{
		this.colNetMtu = colNetMtu;
	}

	public String getColNetBroadcast()
	{
		return this.colNetBroadcast;
	}

	public void setColNetBroadcast(String colNetBroadcast)
	{
		this.colNetBroadcast = colNetBroadcast;
	}

	public String getColNetGateway()
	{
		return this.colNetGateway;
	}

	public void setColNetGateway(String colNetGateway)
	{
		this.colNetGateway = colNetGateway;
	}

	public String getColNetHostnamealias()
	{
		return this.colNetHostnamealias;
	}

	public void setColNetHostnamealias(String colNetHostnamealias)
	{
		this.colNetHostnamealias = colNetHostnamealias;
	}

}
