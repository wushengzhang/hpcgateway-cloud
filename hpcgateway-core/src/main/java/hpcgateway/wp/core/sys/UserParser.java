package hpcgateway.wp.core.sys;

import java.io.InputStream;
import java.util.List;

public interface UserParser 
{
	public List<User> parseUsers(InputStream in,String username) throws Exception;
	public List<User> parseUsers(byte[] buffer,String username) throws Exception;
}
