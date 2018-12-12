package hpcgateway.wp.core.sys;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupParserImpl implements GroupParser 
{
	private Pattern groupPattern;
	public GroupParserImpl()
	{
		this.groupPattern = Pattern.compile("^([^:]+):x:(\\d+):(.*)$");
	}
	
	public Map<String, Group> readGroupsMap(InputStream in,Pattern filter,Integer gid,String username,int pageNo,int pageSize) throws Exception
	{
		List<Group> list = readGroupsList(null,in,filter,gid,username,pageNo,pageSize);
		Map<String,Group> map = new TreeMap<String,Group>();
		for(Group group : list)
		{
			map.put(group.getGroupname(), group);
		}
		return map;
	}

	public Map<String, Group> readGroupsMap(byte[] buffer,Pattern filter,Integer gid,String username,int pageNo,int pageSize) throws Exception
	{
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		return readGroupsMap(in,filter,gid,username,pageNo,pageSize);
	}
	

	@Override
	public List<Group> parseGroups(byte[] buffer,String groupname) throws Exception 
	{
		InputStream in = new ByteArrayInputStream(buffer);
		return parseGroups(in,groupname);
	}

	@Override
	public List<Group> parseGroups(InputStream in,String groupname) throws Exception 
	{
		List<Group> groups = new ArrayList<Group>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String line = reader.readLine();
		while( line != null )
		{
			line = line.trim();
			if( line.isEmpty() || line.startsWith("#") )
			{
				line = reader.readLine();
				continue;
			}
			
			Matcher matcher = groupPattern.matcher(line);
			if( !matcher.find() )
			{
				line = reader.readLine();
				continue;
			}
			
			String _groupname = matcher.group(1);
			if( groupname !=null && !_groupname.contains(groupname) )
			{
				line = reader.readLine();
				continue;
			}
			String _groupId = matcher.group(2);
			String _users = matcher.group(3);
			List<String> userSet = new ArrayList<String>();
			if( _users!=null && !_users.isEmpty() )
			{
				String[] users = _users.split(",");
				for(String user : users)
				{
					userSet.add(user);
				}
			}

			int groupId = 65534;
			try
			{
				groupId = Integer.parseInt(_groupId);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			Group group = new Group();
			group.setGroupname(_groupname);
			group.setGid(groupId);
			group.setUsers(userSet);

			groups.add(group);
			line = reader.readLine();
		}
		reader.close();
		return groups;
	}
	
//	public Group parserGroup(byte[] buffer) throws Exception 
//	{
//		String line = new String(buffer,"UTF-8");
//		String[]  fields = line.split(":");
//		String groupname = fields[0];
//		String _gid = fields[2];
//		String _users = fields[3];
//		List<String> userSet = new ArrayList<String>();
//		if( _users!=null && !_users.isEmpty() )
//		{
//			String[] users = _users.split(",");
//			for(String user : users)
//			{
//				userSet.add(user);
//			}
//		}
//		int groupId = 65534;
//		try
//		{
//			groupId = Integer.parseInt(_gid);
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		Group group = new Group();
//		group.setGroupname(groupname);
//		group.setGid(groupId);
//		group.setUsers(userSet);
//		return group;
//	}

	public List<Group> readGroupsList(List<Group> groups,InputStream in,Pattern filter,Integer gid,String username,int pageNo,int pageSize) throws Exception
	{
		if( groups == null )
		{
			groups = new ArrayList<Group>();
		}
		BufferedReader reader = null;
		int first = -1;
		int range = -1;
		if( pageNo > 0 && pageSize > 0 )
		{
			first = ( pageNo - 1 ) * pageSize;
			range = pageSize;
		}
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(in));

			int i = 0;
			int pos = 0;
			String line = reader.readLine();
			while( line != null && ( range < 0 || i < range ) )
			{
				line = line.trim();
				if( line.isEmpty() || line.startsWith("#") )
				{
					line = reader.readLine();
					continue;
				}
				
				Matcher matcher = groupPattern.matcher(line);
				if( !matcher.find() )
				{
					line = reader.readLine();
					continue;
				}
				
				String groupname = matcher.group(1);
				String _groupId = matcher.group(2);
				String _users = matcher.group(3);
				List<String> userSet = new ArrayList<String>();
				if( _users!=null && !_users.isEmpty() )
				{
					String[] users = _users.split(",");
					for(String user : users)
					{
						userSet.add(user);
					}
				}

				int groupId = 65534;
				try
				{
					groupId = Integer.parseInt(_groupId);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				if( (filter!=null&&!filter.matcher(groupname).find()) && (gid!=null&&gid!=groupId) && (username!=null&&!userSet.contains(username) ) )
				{
					line = reader.readLine();
					continue;
				}
				
				pos++;
				
				if( first >= 0 && pos < first )
				{
					line = reader.readLine();
					continue;
				}

				Group group = new Group();
				group.setGroupname(groupname);
				group.setGid(groupId);
				group.setUsers(userSet);

				groups.add(group);
				i++;
				line = reader.readLine();
			}
			
			return groups;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally 
		{
			if( reader != null )
			{
				try { reader.close(); } catch(IOException e) { e.printStackTrace();}
			}
		}
	}

	public List<Group> readGroupsList(List<Group> groups,byte[] buffer,Pattern filter,Integer gid,String username,int pageNo,int pageSize) throws Exception 
	{
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		return readGroupsList(groups,in,filter,gid,username,pageNo,pageSize);
	}

}
