package com.birthstone.base.activity.helper;

import android.support.v4.app.FragmentActivity;

import java.util.Stack;

public class FragmentActivityManager {
	private static Stack<FragmentActivity> fragmentStack;
	private static FragmentActivityManager instance;

	public FragmentActivityManager( )
	{

	}

	public static FragmentActivityManager getFragmentActivityManager()
	{
		if(instance == null)
		{
			instance = new FragmentActivityManager();
		}
		return instance;
	}

	public static Boolean pop()
	{
		FragmentActivity fragmentActivity = fragmentStack.lastElement();
		if(fragmentActivity != null)
		{
			fragmentStack.remove(fragmentActivity);
			return true;
		}
		fragmentActivity=null;
		return true;
	}

	public static void pop(FragmentActivity fragmentActivity)
	{
		if(fragmentActivity != null)
		{
			if(fragmentStack == null)
			{
				fragmentStack = new Stack<FragmentActivity>();
			}
			fragmentStack.remove(fragmentActivity);
			fragmentActivity=null;
		}
	}

	public static FragmentActivity first()
	{
		FragmentActivity fragmentActivity = fragmentStack.firstElement();
		return fragmentActivity;
	}

	public static FragmentActivity last()
	{
		FragmentActivity fragmentActivity = fragmentStack.lastElement();
		return fragmentActivity;
	}

	public static FragmentActivity current()
	{
		FragmentActivity fragmentActivity = fragmentStack.lastElement();
		return fragmentActivity;
	}

	public static Object getElement(int index)
	{
		return fragmentStack.get(index);
	}

	public static void push(FragmentActivity fragmentActivity)
	{
		if(fragmentStack == null)
		{
			fragmentStack = new Stack<FragmentActivity>();
		}
		fragmentStack.remove(fragmentActivity);
		fragmentStack.add(fragmentActivity);
	}

	//
	public static void clear()
	{
		if(fragmentStack == null)
		{
			fragmentStack = new Stack<FragmentActivity>();
		}
		fragmentStack.clear();
	}

	public static int size()
	{
		if(fragmentStack == null)
		{
			fragmentStack = new Stack<FragmentActivity>();
		}
		return fragmentStack.size();
	}
}
