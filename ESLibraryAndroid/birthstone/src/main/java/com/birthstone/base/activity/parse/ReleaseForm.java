package com.birthstone.base.activity.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IReleaser;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataTable;

public class ReleaseForm implements IReleaser, IControlSearcherHandler
{
	private DataTable table;
	private Activity form;

	public ReleaseForm( Activity form )
	{
		this.form = form;
	}

	public void release(Object data)
	{
		try
		{
			if((data != null) && (this.form != null))
			{
				table = (DataTable) data;
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(form);
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
			if(obj instanceof IReleasable)
			{
				IReleasable Releasa = (IReleasable) obj;
				LinkedList<String> strs = Releasa.getRequest();
				int size = strs.size();
				String name = "";
				for(int i = 0; i < size; i++)
				{
					name = strs.get(i);
					Data data = table.getFirst().get(name);
					if(data != null)
					{
						Releasa.release(name, data);
					}
				}
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IReleasable;
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
