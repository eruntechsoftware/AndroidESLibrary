package com.birthstone.base.activity;


import com.birthstone.base.activity.helper.FormHelper;
import com.birthstone.core.parse.DataCollection;

public abstract class Context extends android.content.Context
{

	/**
	 * ͼ
	 * 
	 * @param targetViewController ͼ
	 * @param params ͼĲ
	 * @param navigationbar ǷĬ
	 * **/
	public static void pushViewController(android.content.Context context, String targetViewController, DataCollection params, Boolean navigationbar)
	{
		try
		{
			FormHelper open = new FormHelper();
			open.open(context, targetViewController, params, true, navigationbar);
		}
		catch(Exception ex)
		{

		}
	}
}
