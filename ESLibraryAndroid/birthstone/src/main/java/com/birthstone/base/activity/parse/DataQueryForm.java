package com.birthstone.base.activity.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataQuery;

import java.util.ArrayList;
import java.util.List;

public class DataQueryForm implements IControlSearcherHandler
{
	private Activity form;

	public DataQueryForm( Activity form )
	{
		this.form = form;
	}

	public void query()
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
			Log.v("QueryForm", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{

		try
		{
			if(obj instanceof IDataQuery)
			{
				IDataQuery Initidata = (IDataQuery) obj;
				Initidata.query();
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

		return obj instanceof IDataQuery;
	}

	public Activity getForm()
	{
		return form;
	}

	public void setForm(Activity form)
	{
		this.form = form;
	}

}
