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

	private Activity activity;
	private Boolean result = true;

	public ValidatorController(Activity activity )
	{
		this.activity = activity;
	}

	public Boolean validator()
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
			Log.v("Validator", ex.getMessage());
		}
		return result;
	}

	public void handle(Object obj)
	{
		try
		{
			IValidatible Validatible = (IValidatible) obj;
			result = Validatible.dataValidator();
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

}
