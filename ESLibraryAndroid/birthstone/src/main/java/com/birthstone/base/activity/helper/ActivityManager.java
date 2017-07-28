package com.birthstone.base.activity.helper;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager
{
	private static Stack<Activity> activityStack;
	private static ActivityManager instance;

	public ActivityManager( )
	{

	}

	public static ActivityManager getActivityManager()
	{
		if(instance == null)
		{
			instance = new ActivityManager();
		}
		return instance;
	}

	public static Boolean pop()
	{
		Activity activity = activityStack.lastElement();
		if(activity != null)
		{
			activityStack.remove(activity);
			activity=null;
			return true;
		}
		return true;
	}

	public static void pop(Activity activity)
	{
		if(activity != null)
		{
			if(activityStack == null)
			{
				activityStack = new Stack<Activity>();
			}
			activityStack.remove(activity);
			activity=null;
		}
	}

	public static Activity first()
	{
		Activity activity = activityStack.firstElement();
		return activity;
	}

	public static Activity last()
	{
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public static Activity current()
	{
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public static Object getElement(int index)
	{
		return activityStack.get(index);
	}

	public static void push(Activity activity)
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.remove(activity);
		activityStack.add(activity);
//		activity.setIndex(activityStack.size() - 1);
	}

	// ջ
	public static void clear()
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.clear();
	}

	public static int size()
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		return activityStack.size();
	}
}
