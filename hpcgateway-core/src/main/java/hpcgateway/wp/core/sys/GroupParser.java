package hpcgateway.wp.core.sys;

import java.io.InputStream;
import java.util.List;

public interface GroupParser 
{
	public List<Group> parseGroups(InputStream in,String groupname) throws Exception;
	public List<Group> parseGroups(byte[] buffer,String groupname) throws Exception;
}
