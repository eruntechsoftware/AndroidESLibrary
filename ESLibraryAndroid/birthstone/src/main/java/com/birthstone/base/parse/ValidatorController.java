package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.interfaces.IValidator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorController implements IValidator, IControlSearcherHandler
{

	private IChildView childView;
	private Boolean result = true;

	public ValidatorController(IChildView childView)
	{
		this.childView = childView;
	}

	public Boolean validator()
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(childView);
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
			if(obj instanceof IValidatible)
			{
				IValidatible Validatible = (IValidatible) obj;
				result = Validatible.dataValidator();
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

}
