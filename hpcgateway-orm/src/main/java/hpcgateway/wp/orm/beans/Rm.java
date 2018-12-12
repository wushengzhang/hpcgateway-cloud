package hpcgateway.wp.orm.beans;


public class Rm
{
	private Long clusterId;
	private String clusterName;
	private Long id;
	private Node installer;

	private Node controller;
	private Node account;
	private Node storage;
	private Node schedule;
	private String name;
	private String version;
	private String topdir;
	private String bin;
	private String sbin;
	private String lib;
	private String etc;
	
	public Long getInstallerId()
	{
		return installer==null ? null : installer.getId();
	}
	public String getInstallerName()
	{
		return installer==null ? null : installer.getName();
	}
	public Node getInstaller()
	{
		return installer;
	}
	public void setInstaller(Node installer)
	{
		this.installer = installer;
	}	
	public Long getClusterId()
	{
		return clusterId;
	}
	public void setClusterId(Long clusterId)
	{
		this.clusterId = clusterId;
	}	
	public String getClusterName()
	{
		return clusterName;
	}
	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Long getControllerId()
	{
		return controller==null ? null : controller.getId();
	}
	public String getControllerName()
	{
		return controller==null ? null : controller.getName();
	}
	public Node getController()
	{
		return controller;
	}
	public void setController(Node controller)
	{
		this.controller = controller;
	}
	
	public Long getAccountId()
	{
		return account==null ? null : account.getId();
	}
	public String getAccountName()
	{
		return account==null ? null : account.getName();
	}
	public Node getAccount()
	{
		return account;
	}
	public void setAccount(Node account)
	{
		this.account = account;
	}
	
	public Long getStorageId()
	{
		return storage==null ? null : storage.getId();
	}
	public String getStorageName()
	{
		return storage==null ? null : storage.getName();
	}
	public Node getStorage()
	{
		return storage;
	}
	public void setStorage(Node storage)
	{
		this.storage = storage;
	}
	
	public Long getScheduleId()
	{
		return schedule==null ? null : schedule.getId();
	}
	public String getScheduleName()
	{
		return schedule==null ? null : schedule.getName();
	}
	public Node getSchedule()
	{
		return schedule;
	}
	public void setSchedule(Node schedule)
	{
		this.schedule = schedule;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getTopdir()
	{
		return topdir;
	}
	public void setTopdir(String topdir)
	{
		this.topdir = topdir;
	}
	public String getBin()
	{
		return bin;
	}
	public void setBin(String bin)
	{
		this.bin = bin;
	}
	public String getSbin()
	{
		return sbin;
	}
	public void setSbin(String sbin)
	{
		this.sbin = sbin;
	}
	public String getLib()
	{
		return lib;
	}
	public void setLib(String lib)
	{
		this.lib = lib;
	}
	public String getEtc()
	{
		return etc;
	}
	public void setEtc(String etc)
	{
		this.etc = etc;
	}

	

	
	
}
