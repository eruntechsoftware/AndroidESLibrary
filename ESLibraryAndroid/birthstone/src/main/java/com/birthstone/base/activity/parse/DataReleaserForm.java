package com.birthstone.base.activity.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataReleaser;

import java.util.ArrayList;
import java.util.List;

public class DataReleaserForm implements IControlSearcherHandler
{
	public DataReleaserForm( )
	{
	}

	public static DataReleaserForm createDataReleaserForm()
	{
		DataReleaserForm DataReleaserForm = new DataReleaserForm();
		return DataReleaserForm;
	}

	public void release(Activity form) throws Exception
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
			if(obj instanceof IDataReleaser)
			{
				IDataReleaser DataReleaser = (IDataReleaser) obj;
				DataReleaser.release();
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataReleaser;
	}
}
