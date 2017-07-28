package com.birthstone.core.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class ValidatorHelper
{

	static String expression = "";
	static String err = "";

	public static ValidatorHelper createValidatorHelper()
	{
		return new ValidatorHelper();
	}

	public static String createDataTypeValidator(DataType datatype, String value) throws Exception
	{
		expression = "";
		if(datatype == null)
		{
			err = "";
		}
		if(datatype == DataType.Integer)
		{
			err = "";
			expression = "^\\s*-?\\s*\\d+\\s*$";
		}
		if(datatype == DataType.Numeric)
		{
			err = "֣";
			expression = "^\\s*-?\\s*\\d+(.\\d+)?\\s*$";
		}
		if(datatype == DataType.Money)
		{
			err = "";
			expression = "^\\s*-?\\s*\\d+(.\\d+)?\\s*$";
		}
		if(datatype == DataType.Date)
		{
			err = "ڣʽΪYYYY-MM-DD";
			expression = "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
		}
		if(isMached(expression, value))
		{
			err = "";
		}
		return err;
	}

	public static String createRequiredValidator(String value) throws Exception
	{
		try
		{
			if(value == null || value.equals(""))
			{
				err = "";
				// DisplayError(target);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return err;
	}

	public static Boolean isMached(String Expression, String value)
	{
		try
		{
			if(Expression == "") { return true; }
			Pattern p = Pattern.compile(Expression);
			Matcher m = p.matcher(value);
			if(value != null)
			{
				if(!m.matches()) { return false; }
			}
			else
			{
				return true;
			}
			return true;
		}
		catch(Exception ex)
		{
			Log.e("У", ex.getMessage());
		}
		return true;
	}

	/*
	 * УǷΪ
	 */
	public static Boolean isNumber(String value)
	{
		return isMached("^[A-Za-z0-9]+$", value);
	}

	/*
	 * УǷΪ
	 */
	public static Boolean isChinese(String value)
	{
		return isMached("^[\u4E00-\u9FFF]+$", value);
	}

	public static String getExpression()
	{
		return expression;
	}

	public static void setExpression(String expression)
	{
		ValidatorHelper.expression = expression;
	}

	public static String getErr()
	{
		return err;
	}

	public static void setErr(String err)
	{
		ValidatorHelper.err = err;
	}

}
