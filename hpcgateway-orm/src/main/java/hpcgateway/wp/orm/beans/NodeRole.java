package hpcgateway.wp.orm.beans;

public enum NodeRole {
	// CoS server
	LoginServer(10001,"LoginServer","用户登录服务器"),
	WebService(10002,"WebServer","WEB服务器"),
	Bootstraper(10003,"Bootstraper","系统引导服务器"),
	ImageTemplateServer(10004,"ImageTemplateServer","系统镜像模板服务器"),
	ImageServer(10005,"ImageServer","系统镜像服务器"),
	TemplateServer(10006,"TemplateServer","系统模板服务器"),
	UploadServer(10007,"UploadServer","上传服务器"),
	DownloadServer(10008,"DownloadServer","下载服务器"),
	AppStoreServer(10009,"AppsStoreServer","应用商店服务器"),
	// Professional Server
	FTPServer(20001,"FtpServer","FTP服务器"),
	VisualizationServer(20002,"VirsualizationServer","虚拟化服务器"),
	LDAPServer(20003,"LdapServer","LDAP服务器"),
	NISServer(20004,"NisServer","NIS服务器"),
	ActiveDirectoriesServer(20005,"ActiveDirectories","Windows活动目录服务器"),
	NFSV3Sever(20006,"NfsServer","NFSv3服务器"),
	MDSServer(20007,"MdsServer","Lustre的MDS服务器"),
	OSSServer(20008,"OssServer","Lustre的OSS服务器"),
	// Resource Manager Server
	SlurmController(30001,"SlurmController","Slurm控制服务器"),
	SlurmBackupController(30002,"SlurmBackupController","Slurm备份控制服务器"),
	SlurmDbServer(30003,"SlurmDbServer","Slurm数据库服务器"),
	SlurmDbdServer(30004,"SlurmDbdServer","Slurm数据库访问服务器"),
	SlurmSubmitServer(30005,"SlurmSubmitServer","Slurm作业提交服务器"),
	PbsServer(30006,"PbsServer","PBS服务器"),
	PbsScheduleServer(30007,"PbsScheduleServer","PBS调度服务器"),
	PbsSubmitServer(30008,"PbsSubmitServer","PBS作业提交服务器")
	;

	private int code;
	private String name;
	private String desc;
	private NodeRole(int code,String name,String desc)
	{
		this.code = code;
		this.name= name;
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
