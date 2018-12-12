package hpcgateway.wp.utils.enumeration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Helper
{
	static public <T> T findValue(Class<T> ref,String name)
	{
		if( !ref.isEnum() || name == null || (name=name.trim()).isEmpty() )
		{
			return null;
		}
	
		T[] ts = ref.getEnumConstants();
		for(T t : ts)
		{
			if( t instanceof Descriptable )
			{
				String n = ((Descriptable)t).getValue();
				if( name.compareTo(n) == 0 )
				{
					return t;
				}
			}
		}

		return null;
	}
	
	static public <T> T findValue(Class<T> ref,int index)
	{
		if( !ref.isEnum() )
		{
			return null;
		}
	
		T[] ts = ref.getEnumConstants();
		if( index < 0 || index >= ts.length )
		{
			return null;
		}

		return ts[index];		
	}
	
	static public Descriptable[] getList(Class<Descriptable> ref)
	{
		if( !ref.isEnum() )
		{
			return null;
		}
		return ref.getEnumConstants();
	}
	
	static public List<Item> getItems(Class<?> ref) throws ClassNotFoundException
	{
		if( !ref.isEnum())
		{
			throw new ClassNotFoundException(String.format("class %s is not enumerate and Descriptable",ref.getName()));
		}
		List<Item> items = new ArrayList<Item>();
		for(Descriptable d : (Descriptable[])ref.getEnumConstants() )
		{
			items.add(new Item(d));
		}
		return items;
	}
	
	static public String getListString(List<?> list)
	{
		if( list == null )
		{
			return null;
		}
		else if( list.isEmpty() )
		{
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<list.size();i++)
		{
			Object object = list.get(i);
			if( object != null )
			{
				buffer.append(String.format("%s%s", i>0?",":"",object.toString().trim()));
			}
		}
		return buffer.toString();
	}
	
	static public <T> List<T> parseStringList(Class<T> clazz,String s) throws Exception
	{
		if( s == null )
		{
			return null;
		}
		List<T> list = new ArrayList<T>();
		if( s.isEmpty() )
		{
			return list;
		}
		StringTokenizer tokenizer = new StringTokenizer(s,",");
		while( tokenizer.hasMoreTokens() )
		{
			String name = tokenizer.nextToken();
			if( clazz.isEnum() )
			{
				T t = Helper.findValue(clazz, name);
				if( t == null )
				{
					throw new ClassNotFoundException(String.format("invalid value %s for %s",name,clazz.getName()));
				}
				list.add(Helper.findValue(clazz, name));
			}
			else 
			{
				Constructor<T> constructor = clazz.getConstructor(String.class);
				T t = constructor.newInstance(name);
				list.add(t);
			}
		}
		return list;
	}
}
