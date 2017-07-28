package com.birthstone.base.activity.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataBindble;
import com.birthstone.core.interfaces.IDataBinder;

import java.util.ArrayList;
import java.util.List;

public class DataBindForm implements IDataBinder, IControlSearcherHandler
{

	private Activity form;
	private Object source;
	private String id;

	public DataBindForm(Activity form, Object source, String id )
	{
		this.form = form;
		this.source = source;
		this.id = id;
	}

	public void bind()
	{
		try
		{
			if(form != null)
			{
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(this.form);
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
			Log.v("DataBindForm", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataBindble;
	}

	public Activity getForm()
	{
		return form;
	}

	@SuppressWarnings("unused")
	private Object getSource()
	{
		return source;
	}

	@SuppressWarnings("unused")
	private String getID()
	{
		return id;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setForm(Activity form)
	{
		this.form = form;
	}

	public void setSource(Object source)
	{
		this.source = source;
	}

}
