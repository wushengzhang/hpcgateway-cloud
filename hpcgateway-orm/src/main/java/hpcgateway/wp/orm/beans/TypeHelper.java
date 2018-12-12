package hpcgateway.wp.orm.beans;

import java.lang.reflect.Method;

public class TypeHelper
{
	static public <T> T findType(Class<T> clazz,int code) throws Exception
	{
		if( !clazz.isEnum() )
		{
			throw new Exception(String.format("CLass %s not enum type",clazz.getName()));
		}
		Method method = clazz.getDeclaredMethod("getCode");
		for(T t : clazz.getEnumConstants())
		{
			Integer __code = (Integer)method.invoke(t);
			if( __code != null && __code == code )
			{
				return t;
			}
		}
		return null;
		
	}
}
