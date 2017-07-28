package com.birthstone.base.activity.helper;

import java.lang.reflect.Field;

import android.util.Log;

import com.birthstone.core.helper.Reflection;

public class InitializeHelper
{
	static Reflection reflection = new Reflection();

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

	public static Object getValue(String classname, String name)
	{
		Object value = null;
		try
		{
			value = reflection.getStaticPropertyValue(classname, name);
		}
		catch(Exception ex)
		{
			Log.v("Initialize getName", ex.getMessage());
		}
		return value;
	}
	
	public static String getStaticProperty(String className, Object value) throws Exception {
        String name = "";
        Class ownerClass = Class.forName(className);
        Field[] fields = ownerClass.getFields();
        int size = fields.length;

        for(int i = 0; i < size; i++) 
        {
        	int val = 0;
        	try
        	{
	            val = fields[i].getInt(ownerClass);
        	}
        	catch(Exception ex)
        	{
        		Log.e("Initialize getName", ex.getMessage());
        	}
        	if(val == (int)value) 
            {
                name = fields[i].getName();
                break;
            }
        }
        return name;
    }
}
