package com.birthstone.base.parse;



import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.parse.DataCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @
 * @ܣռForm ICollectibleʵֶ
 */
public class CollectForm implements ICollector, IControlSearcherHandler
{
	Activity mActivity;
	DataCollection mResult;
	String mSign;

	/**
	 *
	 */
	public CollectForm( Activity mActivity )
	{
		this.mActivity = mActivity;
	}

	/**
	 *
	 * @param sign
	 */
	public CollectForm( Activity mActivity, String sign )
	{
		this.mActivity = mActivity;
		this.mSign = sign;
	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		this.mResult = new DataCollection();
		try
		{
			if(mActivity != null)
			{
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(this.mActivity);
				Controllist.clear();
				Controllist=null;
			}
		}
		catch(Exception ex)
		{
			Log.v("CollectForm", ex.getMessage());
		}
		return this.mResult;
	}

	/**
	 * ѭռ
	 * 
	 * @param obj ҪռĶ
	 */
	public void handle(Object obj)
	{
		try
		{
			if(obj instanceof ICollectible)
			{
				ICollectible collect = (ICollectible) obj;
				DataCollection dataCollection = collect.collect();
				if((dataCollection != null) && (dataCollection.size() != 0))
				{
					this.mResult.addAll(dataCollection);
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("ռ '" + obj.toString() + "' ֹ˿ãԭ", ex.getMessage());
		}
	}

	/**
	 * ȷ϶ǷICollectibleʵ
	 */
	public Boolean isPicked(Object obj)
	{
		if(obj instanceof ICollectible)
		{
			ICollectible Collectible = (ICollectible) obj;
			if(this.mSign == null)
			{
				return false;
			}
			else
			{
				return((Collectible != null) && (this.mSign.equals(null) || this.matchSign(this.mSign, Collectible.getCollectSign())));
			}
		}
		return false;
	}

	/**
	 * @:  2012-5-23
	 * @޸:
	 * @޸ʱ䣺
	 * @ܣƥǣȷǷռ
	 * @param target
	 * @param strings Ǽ
	 * @return
	 */
	private Boolean matchSign(String target, String[] strings)
	{
		int size = strings.length;
		for(int i = 0; i < size; i++)
		{
			if(strings[i] != null && strings[i].equals(target)) { return true; }
		}
		return false;
	}

	public Activity getActivity()
	{
		return mActivity;
	}

	public void setActivity(Activity mActivity)
	{
		this.mActivity = mActivity;
	}

	public DataCollection getDataCollection()
	{
		return mResult;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

}
