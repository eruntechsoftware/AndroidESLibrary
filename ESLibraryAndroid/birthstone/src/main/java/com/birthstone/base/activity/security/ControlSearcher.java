package com.birthstone.base.activity.security;

import com.birthstone.base.activity.Activity;
import com.birthstone.core.interfaces.IControlSearcherHandler;

import java.util.ArrayList;
import java.util.List;


public class ControlSearcher
{
	private ArrayList<IControlSearcherHandler> handlers;

	public ControlSearcher( )
	{
		this.handlers = new ArrayList<IControlSearcherHandler>();
	}

	public ControlSearcher( List<IControlSearcherHandler> handlers )
	{
		this.handlers = new ArrayList<IControlSearcherHandler>(handlers);
	}

	public void search(Object control) throws Exception
	{
		try
		{
			int size = this.handlers.size();
			// Log.v("Controls",String.valueOf(size));
			if(control instanceof Activity)
			{
				if(((Activity) control).getViews().size() > 0)
				{
					int len = ((Activity) control).getViews().size();
					for(int i = 0; i < len; i++)
					{
						search(((Activity) control).getViews().get(i));
					}
				}
			}
			else
			{
				for(int i = 0; i < size; i++)
				{
					// Log.v("ControlSearcher",
					// String.valueOf(handlers.get(i).IsPicked(control)));
					if(handlers.get(i).isPicked(control))
					{
						handlers.get(i).handle(control);
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}
}
