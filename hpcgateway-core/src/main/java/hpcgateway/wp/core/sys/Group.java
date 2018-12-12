package hpcgateway.wp.core.sys;

import java.util.ArrayList;
import java.util.List;

public class Group 
{
	private String groupname;
	private Integer gid;
	private List<String> users;
//	private List<String> roles;
	
	public void addUser(String username)
	{
		if( users == null )
		{
			users = new ArrayList<String>();
		}
		if( !users.contains(username) )
		{
			users.add(username);
		}
	}
	

	public void addAllUsers(List<String> list) 
	{
		if( list != null && !list.isEmpty() )
		{
			for(String user : list)
			{
				addUser(user);
			}
		}
	}
	
	public void removeUser(String username)
	{
		if( users == null || users.isEmpty() || !users.contains(username) )
		{
			return;
		}
		users.remove(username);
	}
	
	public boolean hasUsers()
	{
		return users!=null&&!users.isEmpty();
	}
	
	public boolean containUser(String username)
	{
		return users!=null && users.contains(username);
	}
	
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}


//	public List<String> getRoles() {
//		return roles;
//	}
//
//
//	public void setRoles(List<String> roles) {
//		this.roles = roles;
//	}




	
	
}
