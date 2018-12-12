package hpcgateway.wp.core.sys;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class UserParserImpl implements UserParser 
{
	final private User parseUser(String line) throws Exception
	{
		List<String> flds = ParseHelper.split(line, ':');
		if( flds.size() != 7 )
		{
			throw new Exception("Invalid user format");
		}
		User user = new User();
		user.setUsername(flds.get(0));
		user.setUid(Integer.parseInt(flds.get(2)));
		user.setGid(Integer.parseInt(flds.get(3)));
		user.setComment(flds.get(4));
		user.setHome(flds.get(5));
		user.setShell(flds.get(6));
		return user;		
	}
	
	@Override
	public List<User> parseUsers(InputStream in, String username) throws Exception 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = reader.readLine();
		List<User> list = new ArrayList<User>();
		while( line != null )
		{
			User user = parseUser(line);
			if( username==null || user.getUsername().contains(username) )
			{
				list.add(user);
			}
			line = reader.readLine();
		}
		reader.close();
		return list;
	}

	@Override
	public List<User> parseUsers(byte[] buffer,String username) throws Exception 
	{
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		return parseUsers(in,username);
	}



	
}
