package hpcgateway.wp.core.context;

import java.io.File;
import java.util.TreeMap;

//import java.util.concurrent.locks.ReentrantLock;

//This class takes a piece of text and substitutes all the variables within it. The default definition of a variable is ${variableName}.
import org.apache.commons.lang3.text.StrSubstitutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;


public class HPCGatewayContext
{
	private  Log log = LogFactory.getLog(HPCGatewayContext.class);

	private WebApplicationContext ctx;

	public static String HPCGATEWAY_LOGIN_URL  = "/login/LoginDialog.html";
	public static String HPCGATEWAY_LOGIN_USER = "HG.USER";

	public static String ACL_DIRNAME = "acl";
	public static String PROFILE_DIRNAME = "profile";
	
//	public static String PROFILE_FILE_NAME = "profile.json";
//	public static String PROFILE_TOP = "WEB-INF/profile";
//	public static String PROFILE_DEFAULT_DIR = PROFILE_TOP + "/default";
//	public static String PROFILE_DEFAULT_FILE = PROFILE_TOP + "/default/" + PROFILE_FILE_NAME;
//	public static String PROFILE_GROUP_DIR = PROFILE_TOP + "/group";
//	public static String PROFILE_GROUP_COMMON_FILE = PROFILE_GROUP_DIR + "/" + PROFILE_FILE_NAME;
//	public static String PROFILE_USER_DIR = PROFILE_TOP + "/user";
//	public static String PROFILE_USER_COMMON_FILE = PROFILE_USER_DIR + "/" + PROFILE_FILE_NAME;
	
	public static String CONFIG_FILE_NAME = "config.json";
	public static String CONFIG_TOP = "WEB-INF/serviceconfig";
	public static String CONFIG_DEFAULT_DIR = CONFIG_TOP + "/default";
	public static String CONFIG_DEFAULT_FILE = CONFIG_TOP + "/default/" + CONFIG_FILE_NAME;
	public static String CONFIG_GROUP_DIR = CONFIG_TOP + "/group";
	public static String CONFIG_GROUP_COMMON_FILE = CONFIG_GROUP_DIR + "/" + CONFIG_FILE_NAME;
	public static String CONFIG_USER_DIR = CONFIG_TOP + "/user";
	public static String CONFIG_USER_COMMON_FILE = CONFIG_USER_DIR + "/" + CONFIG_FILE_NAME;
	public static String HPCGATEWAY_NAME = "hpcgateway";
	public static String HPCGATEWAY_PATH_NAME = File.separator + HPCGATEWAY_NAME  + File.separator;
	public static String COS_NAME = "CoS";
	public static String COS_CONF_NAME = "conf";
	public static String COS_PATCHES_NAME = "patches";
	public static String RESOURCE_MANAGER_NAME = "rm";
	public static String RESOURCE_MANAGER_CONFIG_FILE_NAME = "rm.xml";
	public static String COLLECT_CONFIG_FILE_NAME = "collect.xml";
	public static String ACTIVITY_CONFIG_FILE_NAME = "activity.config.xml";
	public static String USER_SESSION_ACTIVITY_CONFIG_FILE_NAME = "activity.config.json";
	public static String CLUSTER_DESCRIPTION_FILENAME = "ClusterDescription.default.xml";
	public static String CLUSTER_TFTP_BASEDIR = "t";
	public static String ACL_CONFIG_FILE_NAME = "acl.xml";
	
	private  String cosHome = null;
	private  String cosPatchesHome = null;
	private  String clusterDescriptionFile = null;
	private  String hpcgatewayHome = null;
	private  String hpcgatewayParentDir = null;
	private  String cosResourceManagerHome = null;
	private  String activityConfiguratorFile = null;
	private  String defaultTftpBaseDir = null;
	private  String aclTopDir = null;
	private  String rmDescFile = null;
	private  String collectConfigFile = null;
	private  String aclConfigFile = null;
	
//	private  java.util.TreeMap<String, java.util.concurrent.ThreadPoolExecutor> threadpools = null;
//	private  ReentrantLock threadpoolsLock = new java.util.concurrent.locks.ReentrantLock();
	public  static int DEFAULT_THREAD_POOL_CORE_SIZE = 5;
	public  static int DEFAULT_THREAD_POOL_MAX_SIZE = 10;
	public  static long DEFAULT_THREAD_POOL_KEEPALIVE_TIME = 90;
	public  static java.util.concurrent.TimeUnit  DEFAULT_THREAD_POOL_KEEPALIVE_TIME_UNIT = java.util.concurrent.TimeUnit.SECONDS;
	public  static String HPCGATEWAY_GENERAL_THREADPOOL_NAME = "hpcgateway.general.threadpool";

	public  String HG_PARENTDIR = "HPCGatewayParentDir";
	public  String HG_HOME = "HPCGatewayHome";
	public  String HG_COSHOME = "CoSHome";
	public  String HG_PATCHESHOME = "PatchesHome";
	public  String HG_RESOURCEMANAGERHOME = "ResourceManagerHome";

	private  java.util.TreeMap<String, String> variables = new TreeMap<String, String>();
	private  StrSubstitutor substitutor = new StrSubstitutor(variables);
	

	public HPCGatewayContext() {
		ctx = null;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
	{ 
		this.ctx = (WebApplicationContext)applicationContext;
	} 
	
	public  ApplicationContext getApplicationContext() { 
		return ctx; 
	}

	public void initialize()
	{
		this.hpcgatewayParentDir = createHPCGatewayParentDir(ctx);
		this.hpcgatewayHome = this.createHPCGatewayHome(hpcgatewayParentDir);
		this.cosHome = this.createCoSHome(hpcgatewayParentDir);
		this.cosPatchesHome = this.createCoSPatchesHome(hpcgatewayParentDir);
		this.cosResourceManagerHome = this.createCoSResourceManagerHome(hpcgatewayParentDir);
		this.activityConfiguratorFile = this.createActivityCofiguratorFileName(hpcgatewayParentDir);
		this.clusterDescriptionFile = this.createClusterDescriptionFileName(hpcgatewayParentDir);
		this.defaultTftpBaseDir = this.createDefaultTftpBaseDir(cosHome);
		this.aclTopDir = this.createAclTopDir(hpcgatewayParentDir);
		this.rmDescFile = this.createRmDescriptionFileName(hpcgatewayParentDir);
		this.collectConfigFile = this.createCollectConfigFileName(hpcgatewayParentDir);
		this.aclConfigFile = this.createAclConfigFileName(hpcgatewayParentDir);
		
		log.info("this.hpcgatewayParentDir=" + this.hpcgatewayParentDir);
		log.info("this.hpcgatewayHome=" + this.hpcgatewayHome);
		log.info("this.cosHome=" + this.cosHome);
		log.info("this.cosPatchesHome=" + this.cosPatchesHome);
		log.info("this.cosResourceManagerHome=" + this.cosResourceManagerHome);
		log.info("this.activityConfiguratorFile=" + this.activityConfiguratorFile);
		log.info("this.clusterDescriptionFile=" + this.clusterDescriptionFile);
		log.info("this.defaultTftpBaseDir=" + this.defaultTftpBaseDir);
		log.info("this.aclTopDir=" + this.aclTopDir);
	}

	public  String substitute(String str) {
		if(!variables.containsKey(HG_PARENTDIR)) {
			variables.put(HG_PARENTDIR, this.getHpcgatewayParentDir());
		}
		if(!variables.containsKey(HG_HOME)) {
			variables.put(HG_HOME, this.getHpcgatewayHome());
		}
		if(!variables.containsKey(HG_COSHOME)) {
			variables.put(HG_COSHOME, this.getCosHome());
		}
		if(!variables.containsKey(HG_PATCHESHOME)) {
			variables.put(HG_PATCHESHOME, this.getCosPatchesHome());
		}
		if(!variables.containsKey(HG_RESOURCEMANAGERHOME)) {
			variables.put(HG_RESOURCEMANAGERHOME, this.getCosResourceManagerHome());
		}
		return substitutor.replace(str);
	}

	public String createHPCGatewayParentDir(WebApplicationContext applicationContext)
	{
		String rootctx = applicationContext.getServletContext().getRealPath(File.separator);
		String[] paths = StringUtils.split(rootctx, HPCGATEWAY_PATH_NAME);
		if(paths == null || paths.length == 0 ) 
		{
			return null;
		} 
		else 
		{
			rootctx = paths[0];
		}
		
		log.info("rootctx=" + rootctx);
		
		return rootctx;
	}

	
	public String createHPCGatewayHome(String hpcGatewayParentDir)
	{
		return String.format("%s/%s",hpcGatewayParentDir,HPCGATEWAY_NAME);
	}

	public  String createCoSHome(String hpcGatewayParentDir) {
		return String.format("%s/%s",createHPCGatewayHome(hpcGatewayParentDir),COS_NAME);
	}

	public  String createCoSPatchesHome(String hpcGatewayParentDir) 
	{
		return String.format("%s/%s", createCoSHome(hpcGatewayParentDir), COS_PATCHES_NAME);
	}

	public String createCoSResourceManagerHome(String hpcGatewayParentDir) 
	{
		return String.format("%s/%s", createCoSHome(hpcGatewayParentDir),RESOURCE_MANAGER_NAME);
	}

	public  String createActivityCofiguratorFileName(String hpcGatewayParentDir) 
	{
		return String.format("%s/%s/%s",createCoSHome(hpcGatewayParentDir),COS_CONF_NAME,ACTIVITY_CONFIG_FILE_NAME);
	}

	public  String createResourceManagerConfigFileName() 
	{
		return getCosHome() + File.separator + COS_CONF_NAME + File.separator + RESOURCE_MANAGER_CONFIG_FILE_NAME;
	}
	
	public String createClusterDescriptionFileName(String hpcGatewayParentDir)
	{
		return String.format("%s/%s/%s", createCoSHome(hpcGatewayParentDir),COS_CONF_NAME,CLUSTER_DESCRIPTION_FILENAME);
	}
	
	public String createRmDescriptionFileName(String hpcGatewayParentDir)
	{
		return String.format("%s/%s/%s", createCoSHome(hpcGatewayParentDir),COS_CONF_NAME,RESOURCE_MANAGER_CONFIG_FILE_NAME);
	}

	public String createCollectConfigFileName(String hpcGatewayParentDir)
	{
		return String.format("%s/%s/%s", createCoSHome(hpcGatewayParentDir),COS_CONF_NAME,COLLECT_CONFIG_FILE_NAME);
	}
	
	public String createAclConfigFileName(String hpcGatewayParentDir)
	{
		return String.format("%s/%s/%s", createCoSHome(hpcGatewayParentDir),COS_CONF_NAME,ACL_CONFIG_FILE_NAME);
	}

	public String createDefaultTftpBaseDir(String hpcGatewayParentDir)
	{
		return String.format("%s/%s/%s", this.cosHome, COS_CONF_NAME, CLUSTER_TFTP_BASEDIR);
	}
	
	public String createAclTopDir(String hpcGatewayParentDir)
	{
		return String.format("%s/%s", createCoSHome(hpcGatewayParentDir),ACL_DIRNAME);
	}
	
	
	public String getUserAclDir()
	{
		return String.format("%s/users", this.aclTopDir);
	}
	
	public File getUserAclFile(String filename)
	{
		return new File(String.format("%s%susers",aclTopDir,File.separator),filename);
	}
	
	public String getUserAclFilename(String username)
	{
		return String.format("%s/users/%s", this.aclTopDir,username);
	}
	public String getGroupAclDir()
	{
		return String.format("%s/groups", this.aclTopDir);
	}
	public File getGroupAclFile(String filename)
	{
		return new File(String.format("%s%sgroups",aclTopDir,File.separator),filename);
	}
	public String getGroupAclFilename(String groupname)
	{
		return String.format("%s/groups/%s", this.aclTopDir,groupname);
	}
	public File getDefaultAclFile(String filename)
	{
		return new File(aclTopDir,filename);
	}
	
	public String getRoleDir()
	{
		return String.format("%s/roles", this.aclTopDir);
	}
	public String getRoleFilename(String name)
	{
		return String.format("%s/roles/%s", this.aclTopDir,name);
	}
	
	
	
	public  String getUserSessionActivityCofiguratorFileName(String realPath, String serviceName, String userHome, String userName, java.util.ArrayList<String> groups) {
		String activityUserSessionConfiguratorFile = null;
		java.io.File f = null;

		if(userHome != null && !userHome.endsWith(File.separator)) {
			userHome += File.separator;
		}

		//assume config file at user's home are hidden
		activityUserSessionConfiguratorFile = userHome + "." + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
		log.debug("get user session activity configure file, trying " + activityUserSessionConfiguratorFile);
		f = new java.io.File(activityUserSessionConfiguratorFile);
		if(f.exists()) {
			//(1) if the user's home contains an .activity.config.json then return it.
			return activityUserSessionConfiguratorFile;
		}

		if(realPath != null && !realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}

		String top = realPath + HPCGatewayContext.CONFIG_TOP;
		activityUserSessionConfiguratorFile = null;
		if(userName != null) {
			activityUserSessionConfiguratorFile = top + "/" + serviceName + "/" + userName + "/" + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
			log.debug("get user session activity configure file, trying " + activityUserSessionConfiguratorFile);
			f = new java.io.File(activityUserSessionConfiguratorFile);
			if(f.exists()) {
				//(2) if the user's profile contains an activity.config.json then return it.
				return activityUserSessionConfiguratorFile;
			}
		}

		top = realPath + HPCGatewayContext.CONFIG_GROUP_DIR;
		activityUserSessionConfiguratorFile = null;
		for(String g: groups) {
			activityUserSessionConfiguratorFile = top + "/" + serviceName + "/" + g + "/" + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
			log.debug("trying " + activityUserSessionConfiguratorFile);
			if(f.exists()) {
				//(3) if the user's group profile contains an activity.config.json then return it.
				return activityUserSessionConfiguratorFile;
			}
		}

		return activityUserSessionConfiguratorFile;
	}

	public  String getUserSessionActivityCofiguratorFileFullPath(String realPath, String serviceName, String userHome, String userName, java.util.ArrayList<String> groups) {
		String activityUserSessionConfiguratorFile = null;
		java.io.File f = null;

		if(userHome != null && !userHome.endsWith(File.separator)) {
			userHome += File.separator;
		}

		//assume config file at user's home are hidden
		activityUserSessionConfiguratorFile = userHome;
		log.debug("get user session activity configure file, trying " + activityUserSessionConfiguratorFile);
		f = new java.io.File(activityUserSessionConfiguratorFile);
		if(f.isDirectory()) {
			//(1) if the user's home exists then return it.
			activityUserSessionConfiguratorFile += "." + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
			return activityUserSessionConfiguratorFile;
		}

		if(realPath != null && !realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}

		String top = realPath + HPCGatewayContext.CONFIG_TOP;
		activityUserSessionConfiguratorFile = null;
		if(userName != null) {
			activityUserSessionConfiguratorFile = top + "/" + serviceName + "/" + userName;
			log.debug("get user session activity configure file, trying " + activityUserSessionConfiguratorFile);
			f = new java.io.File(activityUserSessionConfiguratorFile);
			if(f.isDirectory()) {
				//(2) if the user's profile contains an activity.config.json then return it.
				activityUserSessionConfiguratorFile += "/" + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
				return activityUserSessionConfiguratorFile;
			}
		}

		top = realPath + HPCGatewayContext.CONFIG_GROUP_DIR;
		activityUserSessionConfiguratorFile = null;
		for(String g: groups) {
			activityUserSessionConfiguratorFile = top + "/" + serviceName + "/" + g;
			log.debug("trying " + activityUserSessionConfiguratorFile);
			if(f.isDirectory()) {
				//(3) if the user's group profile contains an activity.config.json then return it.
				activityUserSessionConfiguratorFile += "/" + USER_SESSION_ACTIVITY_CONFIG_FILE_NAME;
				return activityUserSessionConfiguratorFile;
			}
		}

		return activityUserSessionConfiguratorFile;
	}


//	public  String getUserProfile(String realPath, String userName, java.util.ArrayList<String> groups) {
//		java.io.File f = null;
//		String fn = null;
//		
//		if(!realPath.endsWith(File.separator)) {
//			realPath += "/";
//		}
//		String top = realPath + HPCGatewayContext.CONFIG_USER_DIR;
//		if(userName != null) {
//			fn = top + "/" + userName + "/" + HPCGatewayContext.PROFILE_FILE_NAME;
//			log.debug("trying " + fn);
//			f = new java.io.File(fn);
//			if(f.exists()) {
//				return fn;
//			}
//		}
//		top = realPath + HPCGatewayContext.CONFIG_GROUP_DIR;
//		for(String g: groups) {
//			fn = top + "/" + g + "/" + HPCGatewayContext.PROFILE_FILE_NAME;
//			log.debug("trying " + fn);
//			if(f.exists()) {
//				return fn;
//			}
//		}
//		fn = realPath + PROFILE_DEFAULT_FILE;
//		log.info("using profile " + fn);
//		return fn;
//	}

	public  String getUserConfig(String realPath, String serviceName, String configFile, String userName, java.util.ArrayList<String> groups) {
		//if(Utils.isEmpty(configFile)) {
		if( configFile == null || configFile.isEmpty() ) {
			configFile = HPCGatewayContext.CONFIG_FILE_NAME;
		}
		java.io.File f = null;
		String fn = null;
		if(!realPath.endsWith(File.separator)) {
			realPath += "/";
		}
		String top = realPath + HPCGatewayContext.CONFIG_TOP;
		if(userName != null) {
			fn = top + "/" + serviceName + "/user/" + userName + "/" + configFile;
			log.debug("getUserConfig...trying " + fn);
			f = new java.io.File(fn);
			if(f.exists()) {
				return fn;
			}
		} 

		for(String groupName: groups) {
			fn = top + "/" + serviceName + "/group/" + groupName + "/" + configFile;
			log.debug("trying " + fn);
			f = new java.io.File(fn);
			if(f.exists()) {
				return fn;
			}
		}
		fn = top + "/" + serviceName + "/default/" + configFile;
		log.debug("trying " + fn);
		f = new java.io.File(fn);
		if(f.exists()) {
			return fn;
		}

		fn = realPath + CONFIG_DEFAULT_FILE;
		log.info("using config " + fn);
		return fn;
	}

//	public  java.util.concurrent.ThreadPoolExecutor getThreadPool(String name) {
//		java.util.concurrent.ThreadPoolExecutor rtn = null;
//		threadpoolsLock.lock();
//		try {
//			if(threadpools == null) {
//				threadpools = new java.util.TreeMap<String, java.util.concurrent.ThreadPoolExecutor>();
//			}
//			rtn = threadpools.get(name);
//			if(rtn == null) {
//				rtn = new java.util.concurrent.ThreadPoolExecutor(DEFAULT_THREAD_POOL_CORE_SIZE, 
//																		 DEFAULT_THREAD_POOL_MAX_SIZE, 
//																		 DEFAULT_THREAD_POOL_KEEPALIVE_TIME, 
//																		 DEFAULT_THREAD_POOL_KEEPALIVE_TIME_UNIT, 
//																		 new java.util.concurrent.LinkedBlockingQueue<Runnable>()
//																		 );
//				threadpools.put(name, rtn);
//			}
//		} finally {
//			threadpoolsLock.unlock();
//		}
//		//threadpoolsLock.unlock();
//
//		return rtn;
//	}


	public String getCosHome() {
		return cosHome;
	}


	public void setCosHome(String cosHome) {
		this.cosHome = cosHome;
	}


	public String getCosPatchesHome() {
		return cosPatchesHome;
	}


	public void setCosPatchesHome(String cosPatchesHome) {
		this.cosPatchesHome = cosPatchesHome;
	}


	public String getHpcgatewayHome() {
		return hpcgatewayHome;
	}


	public void setHpcgatewayHome(String hpcgatewayHome) {
		this.hpcgatewayHome = hpcgatewayHome;
	}


	public String getHpcgatewayParentDir() {
		return hpcgatewayParentDir;
	}


	public void setHpcgatewayParentDir(String hpcgatewayParentDir) {
		this.hpcgatewayParentDir = hpcgatewayParentDir;
	}


	public String getCosResourceManagerHome() {
		return cosResourceManagerHome;
	}


	public void setCosResourceManagerHome(String cosResourceManagerHome) {
		this.cosResourceManagerHome = cosResourceManagerHome;
	}


	public String getActivityConfiguratorFile() {
		return activityConfiguratorFile;
	}


	public void setActivityConfiguratorFile(String activityConfiguratorFile) {
		this.activityConfiguratorFile = activityConfiguratorFile;
	}


	public String getClusterDescriptionFile() {
		return clusterDescriptionFile;
	}


	public void setClusterDescriptionFile(String clusterDescriptionFile) {
		this.clusterDescriptionFile = clusterDescriptionFile;
	}

	public String getDefaultTftpBaseDir() {
		return defaultTftpBaseDir;
	}

	public void setDefaultTftpBaseDir(String defaultTftpBaseDir) {
		this.defaultTftpBaseDir = defaultTftpBaseDir;
	}

	public String getRmDescFile() {
		return rmDescFile;
	}

	public void setRmDescFile(String rmDescFile) {
		this.rmDescFile = rmDescFile;
	}

	public String getCollectConfigFile() {
		return collectConfigFile;
	}

	public String getAclConfigFile() {
		return aclConfigFile;
	}

	public void setAclConfigFile(String aclConfigFile) {
		this.aclConfigFile = aclConfigFile;
	}

	public static String getAclConfigFilename()
	{
		return "CoS/conf/acl.xml";
	}

	public static String getClusterDescriptionFilename()
	{
		return "CoS/conf/ClusterDescription.default.xml";
	}
	
	public static String getCollectConfigFilename()
	{
		return "CoS/conf/collect.xml";
	}
	
	public static String getTftpBaseDir()
	{
		return "CoS/conf/t";
	}

}
