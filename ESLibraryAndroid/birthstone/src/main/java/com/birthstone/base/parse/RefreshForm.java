package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IRefresh;

import java.util.ArrayList;
import java.util.List;


public class RefreshForm implements IControlSearcherHandler
{
	public RefreshForm( )
	{
	}

	public static RefreshForm createRefreshForm()
	{
		RefreshForm Ref = new RefreshForm();
		return Ref;
	}

	public void refresh(Activity form) throws Exception
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(form);
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
			if(obj instanceof IRefresh)
			{
				IRefresh Refresh = (IRefresh) obj;
				Refresh.refreshData();
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());// new
												// ApplicationException(control.Name
												// + " ִRefreshData()ԭ"
												// + ex.Message);
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IRefresh;
	}

}
