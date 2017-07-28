package com.birthstone.base.activity.helper;

import android.support.v4.app.Fragment;

import java.util.Stack;

public class FragmentManager {
	private static Stack<Fragment> FragmentStack;
	private static FragmentManager instance;

	public FragmentManager( )
	{

	}

	public static FragmentManager getFragmentManager()
	{
		if(instance == null)
		{
			instance = new FragmentManager();
		}
		return instance;
	}

	public static Boolean pop()
	{
		Fragment fragment = FragmentStack.lastElement();
		if(fragment != null)
		{
			FragmentStack.remove(fragment);
			fragment=null;
			return true;
		}
		return true;
	}

	public static void pop(Fragment fragment)
	{
		if(fragment != null)
		{
			if(FragmentStack == null)
			{
				FragmentStack = new Stack<Fragment>();
			}
			FragmentStack.remove(fragment);
			fragment=null;
		}
	}

	public static Fragment first()
	{
		Fragment fragment = FragmentStack.firstElement();
		return fragment;
	}

	public static Fragment last()
	{
		Fragment fragment = FragmentStack.lastElement();
		return fragment;
	}

	public static Fragment current()
	{
		Fragment fragment = FragmentStack.lastElement();
		return fragment;
	}

	public static Object getElement(int index)
	{
		return FragmentStack.get(index);
	}

	public static void push(Fragment fragment)
	{
		if(FragmentStack == null)
		{
			FragmentStack = new Stack<Fragment>();
		}
		FragmentStack.remove(fragment);
		FragmentStack.add(fragment);
	}

	// ջ
	public static void clear()
	{
		if(FragmentStack == null)
		{
			FragmentStack = new Stack<Fragment>();
		}
		FragmentStack.clear();
	}

	public static int size()
	{
		if(FragmentStack == null)
		{
			FragmentStack = new Stack<Fragment>();
		}
		return FragmentStack.size();
	}
}
