package com.birthstone.base.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataReleaser;

import java.util.ArrayList;
import java.util.List;

public class DataReleaserController implements IControlSearcherHandler
{
	public DataReleaserController( )
	{
	}

	public static DataReleaserController createDataReleaserForm()
	{
		DataReleaserController DataReleaserForm = new DataReleaserController();
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
