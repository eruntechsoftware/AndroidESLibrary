package com.birthstone.base.activity.parse;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.widgets.HiddenFeild;

import java.util.ArrayList;
import java.util.List;

public class FunctionProtected implements IControlSearcherHandler
{
	private Activity form;
	public void setStateControl(Object form)
	{
		try
		{
			this.form = (Activity) form;
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(form);
			Controllist.clear();
			Controllist=null;
		}
		catch(Exception ex)
		{
			Log.e("Ȩ޿", ex.getMessage());
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
					result = Activity.getFunction().contains(str.trim().toLowerCase());
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
			if(form == null)
			{
				if(obj instanceof Activity)
				{
					form = (Activity) obj;
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
						HiddenFeild hidden = null;
						IReleasable release;
						int size = form.getViews().size();
						for(int i = 0; i < size; i++)
						{
							if(form.getViews().get(i) instanceof IReleasable)
							{
								release = (IReleasable) form.getViews().get(i);
								if(release.getName().equals(aprotected.getStateHiddenId()))
								{
									hidden = (HiddenFeild) form.getViews().get(i);
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
