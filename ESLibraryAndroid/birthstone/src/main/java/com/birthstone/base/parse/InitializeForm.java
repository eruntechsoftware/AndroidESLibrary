package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataInitialize;

import java.util.ArrayList;
import java.util.List;


public class InitializeForm implements IControlSearcherHandler
{
	private Activity mActivity;

	public InitializeForm( Activity mActivity )
	{
		this.mActivity = mActivity;
	}

	public void initialize() throws Exception
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(mActivity);
			Controllist.clear();
			Controllist=null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	public void handle(Object obj)
	{
		try
		{
			if(obj instanceof IDataInitialize)
			{
				IDataInitialize Initidata = (IDataInitialize) obj;
				Initidata.setActivity(mActivity);
				Initidata.dataInitialize();
			}
		}
		catch(Exception ex)
		{
			/*
			 * if (ex.InnerException == null) { throw new Exception(control.Name
			 * + ""); }
			 */
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataInitialize;
	}

	public Activity getActivity()
	{
		return mActivity;
	}

	public void setActivity(Activity mActivity)
	{
		this.mActivity = mActivity;
	}

}
