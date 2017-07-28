package com.birthstone.base.activity.parse;

import java.util.ArrayList;
import java.util.List;


import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.interfaces.IValidator;

public class ValidatorForm implements IValidator, IControlSearcherHandler
{

	private Activity form;
	private Boolean result = true;

	public ValidatorForm( Activity form )
	{
		this.form = form;
	}

	public Boolean validator()
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
			Log.v("Validator", ex.getMessage());
		}
		return result;
	}

	public void handle(Object obj)
	{
		try
		{
			IValidatible Validatible = (IValidatible) obj;
			if(!Validatible.dataValidator())
			{
				result = false;
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{

		return obj instanceof IValidatible;
	}

	public Activity getForm()
	{
		return form;
	}

	public void setForm(Activity form)
	{
		this.form = form;
	}

	public Boolean getResult()
	{
		return result;
	}

}
