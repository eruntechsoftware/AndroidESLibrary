package com.birthstone.base.helper;

import android.util.Log;

import com.birthstone.core.helper.Reflection;

import java.lang.reflect.Field;
import java.util.HashMap;

public class InitializeHelper
{
	private static Reflection Reflection = new Reflection();
	private static HashMap<Integer,String> PropertyMap = new HashMap<>();

	public static String getName(String classname, int id)
	{
		String name = "";
		try
		{
			name = getStaticProperty(classname, id);
		}
		catch(Exception ex)
		{
			Log.v("Initialize getName", ex.getMessage());
		}
		return name;
	}

//	public static Object getValue(String classname, String name)
//	{
//		Object value = null;
//		try
//		{
//			value = Reflection.getStaticPropertyValue(classname, name);
//		}
//		catch(Exception ex)
//		{
//			Log.v("Initialize getName", ex.getMessage());
//		}
//		return value;
//	}
	
	public static String getStaticProperty(String className, int value) throws Exception {
        String name = "";
        Class ownerClass = Class.forName(className);
        Field[] fields = ownerClass.getFields();
        int size = fields.length;

		if(PropertyMap.size()!=size)
		{
			for(int i = 0; i < size; i++)
			{
				try
				{
					if(!PropertyMap.containsKey(fields[i].getInt(ownerClass)))
					{
						PropertyMap.put(fields[i].getInt(ownerClass), fields[i].getName());
					}
				}
				catch (Exception ex)
				{

				}
//				int val = 0;
//				try
//				{
//					val = fields[i].getInt(ownerClass);
//				}
//				catch(Exception ex)
//				{
//					Log.e("Initialize getName", ex.getMessage());
//				}
//				if(val == value)
//				{
//					name = fields[i].getName();
//					break;
//				}

			}
		}
		if(PropertyMap.size()>0)
		{
			name = PropertyMap.get(value);
		}
        return name;
    }
}
