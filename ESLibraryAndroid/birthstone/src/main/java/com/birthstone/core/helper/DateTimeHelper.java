package com.birthstone.core.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/// <summary>
/// </summary>
public class DateTimeHelper
{
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat ft = null;
	private static Date date = null;
	private static Calendar calendar = Calendar.getInstance();

	static int YEAR;
	static int MONTH;
	static int DAY;
	static int HOUR;
	static int MINUTE;

	/**
	 * ǰʱ
	 * @return
	 */
	public static String getNow()
	{
		calendar.setTime(new Date());
		date = calendar.getTime();
		ft = new SimpleDateFormat(DATE_TIME_FORMAT);
		String dateTime = ft.format(date);
		return dateTime;
	}

	/**
	 * ǰ
	 * @return
	 */
	public static String getToday()
	{
		calendar.setTime(new Date());
		date = calendar.getTime();
		ft = new SimpleDateFormat(DATE_FORMAT);
		String dateTime = ft.format(date);
		return dateTime;
	}

	/***
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compar(String date1, String date2)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try
		{
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		if(d1.before(d2)) 
		{ 
			return true; 
		}
		return false;
	}

	/**
	 *
	 * @param value
	 * @param format
	 * @return
	 */
	public static String getDateString(Object value, String format)
	{
		Date date = (Date) value;
		SimpleDateFormat ft = new SimpleDateFormat(format);
		return ft.format(date);
	}
	
	/***
	 *
	 * @param value ʱַ
	 * @return
	 */
	public static String getLocalDateString(Object value)
	{
		String[] date = value.toString().split(" ")[0].split("-");
		return date[0]+""+date[1]+""+date[2]+"";
	}
	
	/**
	 * ʱ䣨λ죩
	 * @param beginDate
	 * @param endDate
	 * @return ʱ
	 */
	public static long getDiffDays(String beginDate, String endDate)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
		    Date d1 = df.parse(beginDate);
		    Date d2 = df.parse(endDate);
		    long diff = d1.getTime() - d2.getTime();
		    long days = diff / (1000 * 60 * 60 * 24);
		    return days;
		}
		catch (Exception e)
		{
		}
		return -1;
	}
	
	/**
	 * @param date
	 * @param desc
	 * @return
	 */
	public static String getDateOfWeek(String date, String desc)
	{
		String weekStr="";
		try
		{
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal.setTime(df.parse(date));
			int week = cal.get(Calendar.DAY_OF_WEEK);  
			switch(week)
			{
			case 1:
				weekStr="";
				break;
			case 2:
				weekStr="һ";
				break;
			case 3:
				weekStr="";
				break;
			case 4:
				weekStr="";
				break;
			case 5:
				weekStr="";
				break;
			case 6:
				weekStr="";
				break;
			case 7:
				weekStr="";
				break;
			}
		}
		catch(Exception ex)
		{
			
		}
		return desc+weekStr;
	}

	public static String getDateFormat()
	{
		return DATE_FORMAT;
	}

	public static String getDateTimeFormat()
	{
		return DATE_TIME_FORMAT;
	}
	
	/**
	 * ǰ
	 * @return
	 */
	public static int getYear()
	{
		YEAR = calendar.get(Calendar.YEAR);
		return YEAR;
	}
	
	/**
	 *
	 * @return
	 */
	public static int getMonth()
	{
		MONTH = calendar.get(Calendar.MONTH);
		return MONTH;
	}
	
	/**
	 *
	 * @return
	 */
	public static int getDay()
	{
		DAY = calendar.get(Calendar.DAY_OF_MONTH);
		return DAY;
	}
	
	/**
	 * @return
	 */
	public static int getHour()
	{
		HOUR = calendar.get(Calendar.HOUR_OF_DAY);
		return HOUR;
	}
	
	/**
	 * @return
	 */
	public static int getMinute()
	{
		MINUTE = calendar.get(Calendar.MINUTE);
		return MINUTE;
	}

}
