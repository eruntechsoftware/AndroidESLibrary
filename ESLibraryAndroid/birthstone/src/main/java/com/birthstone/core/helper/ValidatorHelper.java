package com.birthstone.core.helper;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorHelper
{

	static String expression = "";
	static String err = "";

	/**
	 * 输入值的数据类型校验
	 * @param datatype 数据类型
	 * @param value 输入值
	 * @return 校验不通过的错误描述
	 * */
	public static String dataTypeValidator(DataType datatype, String value) throws Exception
	{
		expression = "";
		if(datatype == null)
		{
			err = "";
		}
		if(datatype == DataType.Integer)
		{
			err = "请输入整数类型";
			expression = "^\\s*-?\\s*\\d+\\s*$";
		}
		if(datatype == DataType.Numeric)
		{
			err = "请输入小数或整数类型";
			expression = "^(([0-9]+\\\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
		}
		if(datatype == DataType.Date)
		{
			err = "请输入yyyy-MM-dd格式的日期";
			expression = "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
		}
		if(datatype == DataType.DateTime)
		{
			err = "请输入yyyy-MM-dd HH:mm:ss格式的时间";
			expression = "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
		}
		if(datatype == DataType.DateTime)
		{
			err = "请输入yyyy-MM-dd HH:mm:ss格式的时间";
			expression = "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
		}
		if(datatype == DataType.EMail)
		{
			err = "请输入正确的电子邮箱格式";
			expression = "^[0-9A-Za-z]{1,50}";
		}
		if(datatype == DataType.EMail)
		{
			err = "请输入正确的电子邮箱格式";
			expression = "^[0-9A-Za-z]{1,50}";
		}
		if(datatype == DataType.URL)
		{
			err = "请输入正确的URL地址";
			expression = "^[0-9A-Za-z]{1,50}";
		}
		if(isMached(expression, value))
		{
			err = "";
		}
		return err;
	}

	/**
	 * 校验输入的正则表达式和输入值
	 * @param expression 正则表达式
	 * @param value 输入值
	 * @return 是否合法
	 */
	public static Boolean isMached(String expression, String value)
	{
		try
		{
			if(TextUtils.isEmpty(expression)) { return true; }
			Pattern p = Pattern.compile(expression);
			Matcher m = p.matcher(value);
			boolean b = m.matches();
			return b;
		}
		catch(Exception ex)
		{
			Log.e("У", ex.getMessage());
		}
		return true;
	}

	/**
	 * 是否number类型
	 */
	public static Boolean isNumber(String value)
	{
		return isMached("^[A-Za-z0-9]+$", value);
	}

	/**
	 * 是否汉字类型
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
