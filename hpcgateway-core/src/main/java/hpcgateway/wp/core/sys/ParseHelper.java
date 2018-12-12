package hpcgateway.wp.core.sys;

import java.util.ArrayList;
import java.util.List;

public class ParseHelper 
{
	static public List<String> split(String s,char del)
	{
		List<String> list = new ArrayList<String>();
		int prev = 0;
		int pos = 0;
		while( pos < s.length() )
		{
			while( pos < s.length() && s.charAt(pos) != del ) pos++;
			list.add(s.substring(prev, pos));
			if( pos < s.length() ) pos++;
			prev = pos;
		}
		return list;
	}
	
	static public List<String> split(String s)
	{
		return split(s,':');
	}
}
