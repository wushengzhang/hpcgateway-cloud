package hpcgateway.wp.core.sys;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import hpcgateway.wp.utils.enumeration.Helper;


public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	final public static String noneAvailable = "n/a";
	
	protected String username;
	protected String password;
	protected Integer uid;
	protected Integer gid;
	protected String gname;
	protected String comment;
	protected String home;
	protected Boolean homeFlag;
	protected String shell;
	protected Integer lstchg;
	protected Integer min;
	protected Integer max;
	protected Integer warning;
	protected Integer inactive;
	protected Date expire;
	protected List<String> groups;

	public User()
	{
		this(null);
	}
	
	public User(User user)
	{
		if( user != null )
		{
			BeanUtils.copyProperties(user, this);
		}
	}
	
	public void addGroup(String group)
	{
		if( groups == null )
		{
			groups = new ArrayList<String>();
		}
		if( !groups.contains(group) )
		{
			groups.add(group);
		}
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getShell() {
		return shell;
	}
	public void setShell(String shell) {
		this.shell = shell;
	}

	public List<String> getGroups() {
		return groups;
	}
	
	public String getGroupsList()
	{
		return Helper.getListString(groups);
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
	public Date getExpire()
	{
		return expire;
	}
	
	public String getExpireStr()
	{
		if( expire == null )
		{
			return noneAvailable;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(expire);
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getHomeFlag() {
		return homeFlag;
	}

	public void setHomeFlag(Boolean homeFlag) {
		this.homeFlag = homeFlag;
	}

	public Integer getInactive() {
		return inactive;
	}

	public void setInactive(Integer inactive) {
		this.inactive = inactive;
	}
	
	public String getInactiveStr()
	{
		if( this.inactive == null )
		{
			return noneAvailable;
		}
		return String.valueOf(this.inactive);
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Integer getLstchg() {
		return lstchg;
	}

	public void setLstchg(Integer lstchg) {
		this.lstchg = lstchg;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getWarning() {
		return warning;
	}

	public void setWarning(Integer warning) {
		this.warning = warning;
	}	
	
}
