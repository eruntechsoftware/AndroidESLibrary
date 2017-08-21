package com.birthstone.base.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataBindble;
import com.birthstone.core.interfaces.IDataBinder;

import java.util.ArrayList;
import java.util.List;

public class DataBindController implements IDataBinder, IControlSearcherHandler
{

	private Activity activity;
	private Object source;
	private String id;

	public DataBindController(Activity activity, Object source, String id )
	{
		this.activity = activity;
		this.source = source;
		this.id = id;
	}

	public void bind()
	{
		try
		{
			if(activity != null)
			{
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(this.activity);
				Controllist.clear();
				Controllist=null;
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{
		try
		{
			IDataBindble dataBind = (IDataBindble) obj;
			dataBind.dataBind(id, source);
		}
		catch(Exception ex)
		{
			Log.v("DataBindController", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataBindble;
	}

}
