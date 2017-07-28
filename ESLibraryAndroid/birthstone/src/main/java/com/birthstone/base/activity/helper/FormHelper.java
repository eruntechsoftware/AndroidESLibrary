package com.birthstone.base.activity.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.birthstone.core.helper.Reflection;
import com.birthstone.core.parse.DataCollection;

public class FormHelper
{
	//Activityתƿ
	/**
	 * ActivityתActivity
	 * 
	 * @param parentActivity ǰ
	 * @param targetViewController ָռ
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController)
	{
		open(parentActivity, targetViewController, new DataCollection());
	}
	
	/**
	 * ActivityתActivity
	 * @param parentActivity ǰ
	 * @param targetViewController ָռ
	 * @param params
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController, DataCollection params)
	{
		open(parentActivity, targetViewController, params, false, false);
	}

	/**
	 * ActivityתActivityǷص
	 * 
	 * @param parentActivity ǰ
	 * @param targetViewController ָռ
	 * @param params
	 * @param showBtnBack
	 * @param navigationbar ǷĬ
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
					ActivityManager.push(parentActivity);
					if(params == null)
					{
						params = new DataCollection();
					}
					Intent intent = new Intent();
					intent.putExtra("Parameter", (DataCollection)params.clone());
					intent.putExtra("ActivityType", "Activity");
					intent.putExtra("Navigationbar", navigationbar);
					intent.putExtra("ShowBtnBack", showBtnBack);
					intent.setClass(parentActivity.getApplicationContext(), obj.getClass());
					parentActivity.startActivityForResult(intent, 0);
				}
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}

	
	//Fragmentתƿ
	/**
	 * FragmentתActivityǷص
	 * 
	 * @param parentFragment ǰ
	 * @param targetViewController ָռ
	 * @param params
	 */
	@Deprecated
	public void open(Fragment parentFragment, String targetViewController, DataCollection params)
	{
		open(parentFragment, targetViewController, params, false, false);
	}
	
	/**
	 * FragmentתǷص
	 * 
	 * @param parentFragment ǰ
	 * @param targetViewController ָռ
	 * @param params
	 * @param showBtnBack
	 * @param navigationbar ǷĬ
	 */
	@Deprecated
	public void open(Fragment parentFragment, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
				if(params==null)
				{
					params = new DataCollection();
				}
				FragmentManager.push(parentFragment);
				Intent intent = new Intent();
				intent.putExtra("Parameter", params);
				intent.putExtra("ActivityType", "Fragment");
				intent.putExtra("Navigationbar", navigationbar);
				intent.putExtra("ShowBtnBack", showBtnBack);
				intent.setClass(parentFragment.getActivity(), obj.getClass());
				parentFragment.startActivityForResult(intent, 0);
			}
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}
	
	//FragmentActivityתƿ
	/**
	 * FragmentActivityת
	 * 
	 * @param parentFragment ǰFragment
	 * @param targetFragment ָռ
	 * @param params
	 */
	@SuppressLint("NewApi")
	public void open(FragmentActivity parentFragment, String targetFragment, DataCollection params, Boolean showBtnBack, Boolean navigationbar )
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetFragment);
			if(obj!=null)
			{
				if(params==null)
				{
					params = new DataCollection();
				}
				FragmentActivityManager.push(parentFragment);

				Intent intent = new Intent();
				intent.putExtra("Parameter", (DataCollection)params.clone());
				intent.putExtra("ActivityType", "FragmentActivity");
				intent.putExtra("Navigationbar", navigationbar);
				intent.putExtra("ShowBtnBack", showBtnBack);
	
				intent.setClass(parentFragment.getBaseContext(), obj.getClass());
				parentFragment.startActivityForResult(intent, 0);
			}
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}
	
	/**Context**/
	/**
	 * ContextתǷص
	 * 
	 * @param parent ǰcontext
	 * @param targetViewController ָռ
	 * @param params
	 * @param showBtnBack
	 * @param navigationbar ǷĬ
	 */
	@SuppressLint("NewApi")
	public void open(Context parent, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
					if(params == null)
					{
						params = new DataCollection();
					}
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("Parameter", (DataCollection)params.clone());
					intent.putExtra("ActivityType", "Context");
					intent.putExtra("Navigationbar", navigationbar);
					intent.putExtra("ShowBtnBack", showBtnBack);
					intent.setClass(parent, obj.getClass());
					parent.startActivity(intent);
				}
			
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}
	
}
