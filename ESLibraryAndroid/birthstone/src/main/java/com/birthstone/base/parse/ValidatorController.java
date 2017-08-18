package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.interfaces.IValidator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorController implements IValidator, IControlSearcherHandler
{

	private Activity form;
	private Boolean result = true;

	public ValidatorController(Activity form )
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

		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		result = false;
		if (obj instanceof IValidatible)
		{
			IValidatible Validatible = (IValidatible) obj;
			result = Validatible.dataValidator();
		}
		return result;
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
