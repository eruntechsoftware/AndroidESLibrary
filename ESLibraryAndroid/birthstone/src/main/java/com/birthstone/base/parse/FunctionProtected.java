package com.birthstone.base.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.widgets.ESHiddenFeild;

import java.util.ArrayList;
import java.util.List;

public class FunctionProtected implements IControlSearcherHandler
{
	private Activity activity;
	public void setStateControl(Object activity)
	{
		try
		{
			this.activity = (Activity) activity;
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(activity);
			Controllist.clear();
			Controllist=null;
		}
		catch(Exception ex)
		{
			Log.e("FunctionProtected", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{
		IFunctionProtected function = (IFunctionProtected) obj;
		Boolean visibled = stateIsMatched(function.getFuncSign());
		function.setVisible(visibled);
		if(visibled)
		{
			setControlState(obj);
		}
	}

	public Boolean isPicked(Object obj)
	{
		if(obj instanceof Activity) { return true; }
		if(obj instanceof IStateProtected || obj instanceof IFunctionProtected)
		{
			return true;
		}
		return false;
	}

	public Boolean stateIsMatched(String[] funcStr)
	{
		int size = funcStr.length;
		String str = "";
		Boolean result = false;
		if(funcStr[0].equals("Empty"))
		{
			result = true;
		}
		else
		{
			for(int i = 0; i < size; i++)
			{
				str = funcStr[i];
				if(str != null)
				{
					result = Activity.getFunctionList().contains(str.trim().toLowerCase());
					if(result)
					{
						break;
					}
				}
			}
		}
		return result;
	}
	
	public void setControlState(Object obj)
	{
		try
		{
			if(activity == null)
			{
				if(obj instanceof Activity)
				{
					activity = (Activity) obj;
				}
			}
			
			if(obj instanceof IStateProtected)
			{
				IStateProtected aprotected = (IStateProtected) obj;
				if(aprotected != null)
				{
					String getValue = aprotected.getWantedStateValue();
					if(getValue != null)
					{
						ESHiddenFeild hidden = null;
						IReleasable release;
						int size = activity.getViews().size();
						for(int i = 0; i < size; i++)
						{
							if(activity.getViews().get(i) instanceof IReleasable)
							{
								release = (IReleasable) activity.getViews().get(i);
								if(release.getName().equals(aprotected.getStateHiddenId()))
								{
									hidden = (ESHiddenFeild) activity.getViews().get(i);
								}
							}
						}
						if(hidden == null) { throw new Exception("޷ҵΪ" + aprotected.getStateHiddenId() + "State Hidden"); }
						aprotected.protectState(this.stateIsMatched(getValue, hidden.getText().toString()));
					}
				}
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean stateIsMatched(String wanted, String current)
	{
		Boolean result = false;
		if(wanted.equals(null)) { return true; }
		// String[] strs = wanted.split(SplitString.Sep1.replace("|!", "!"));
		String[] strs = wanted.replace("|!", "!").split("!");
		int size = strs.length;
		for(int i = 0; i < size; i++)
		{
			if(strs[i].equals(current))
			{
				result = true;
				break;
			}
		}
		return result;
	}

}
