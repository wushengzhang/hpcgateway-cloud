package hpcgateway.wp.core.page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SelectItemHelper
{
	static private String convertToGetMethod(String field)
	{
		return String.format("get%c%s",Character.toUpperCase(field.charAt(0)),field.substring(1));
	}
	static public <T> List<SelectItem> getSelectItems(Class<T> clazz,String name,String value,String desc) throws ClassCastException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if( !clazz.isEnum() )
		{
			throw new ClassCastException(String.format("Class %s is not enum type",clazz.getName()));
		}
		Method getName = clazz.getDeclaredMethod(convertToGetMethod(name));
		Method getValue = clazz.getDeclaredMethod(convertToGetMethod(value));
		Method getDescription = clazz.getDeclaredMethod(convertToGetMethod(desc));
		T[] list = clazz.getEnumConstants();
		List<SelectItem> items = new ArrayList<SelectItem>();
		for(T t : list)
		{
			String _name = (String)getName.invoke(t);
			Object _value = getValue.invoke(t);
			String _description = (String)getDescription.invoke(t);
			if( _name == null || _value == null )
			{
				continue;
			}
			if( _description == null )
			{
				_description = "";
			}
			SelectItem selectItem = new SelectItem();
			selectItem.setName(_name);
			selectItem.setValue(_value.toString());
			selectItem.setDescription(_description);
			items.add(selectItem);
		}
		return items;
	}
}
