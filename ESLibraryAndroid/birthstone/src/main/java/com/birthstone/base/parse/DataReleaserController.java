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

	public static DataReleaserController createDataReleaserController()
	{
		DataReleaserController dataReleaserController = new DataReleaserController();
		return dataReleaserController;
	}

	public void release(Activity activity) throws Exception
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(activity);
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
			Log.v("IDataReleaser", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataReleaser;
	}
}
