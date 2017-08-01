package com.birthstone.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.parse.CollectForm;
import com.birthstone.base.parse.ReleaseForm;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeHelper;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IReleaser;
import com.birthstone.core.parse.DataTable;


public class ActionLoadPage extends TextView implements IDataInitialize, IDataQuery, IReleaser
{
	private SQLiteDatabase mSqlDb;
	protected Activity mActivity;
	protected Boolean mAutoLoad = false;
	protected String mSql;
	protected String mSign = "ForQuery";
	protected DataType mDataType;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";

	public ActionLoadPage( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			this.setVisibility(View.GONE);
			try
			{
				String dataType = attrs.getAttributeValue(mNameSpace, "dataType");
				if(dataType != null && dataType.length() > 0)
				{
					this.mDataType = DataTypeHelper.valueOf(dataType);
				}
			}
			catch(Exception ex)
			{
				Log.e("ʹ", ex.getMessage());
				this.mDataType = DataType.String;
			}
			this.mSign = attrs.getAttributeValue(mNameSpace, "sign");
			this.mSql = attrs.getAttributeValue(mNameSpace, "sql");
			this.mAutoLoad = attrs.getAttributeBooleanValue(mNameSpace, "autoLoad", false);
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	public ActionLoadPage(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣFormϡForm
	 * @return
	 */
	private Boolean onInit()
	{
		mSqlDb = new SQLiteDatabase(mActivity.getApplicationContext());
		this.mSqlDb.getCollectors().add(new CollectForm(mActivity, mSign));
		this.mSqlDb.getReleasers().add(new ReleaseForm(mActivity));
		mSqlDb.setSql(mSql);
		return true;
	}

	/**
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣִз
	 */
	public void execute()
	{
		try
		{
			onInit();
			mSqlDb.executeTable();
		}
		catch(Exception ex)
		{
			Log.e("", ex.getMessage());
		}
	}

	/**
	 * ̶߳
	 */
	Thread mThread = new Thread(new Runnable()
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(onInit())
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			else
			{
				msg.what = 0;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	});

	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			try
			{
				// ͨжmsg.whatжĸ""Ҫд
				switch(msg.what)
				{
				case 1:
					mSqlDb.executeTable();
					mSqlDb.close();
					break;
				}
			}
			catch(Exception ex)
			{
				Log.v("ExecuteHandleMessage", ex.getMessage());
			}
		}
	};

	/**
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣִз
	 * @param ds Դ
	 * @param form ǰ
	 */
	public void execute(DataTable ds, Activity form)
	{
		try
		{
			mSqlDb = new SQLiteDatabase(form);
			mSqlDb.getReleasers().add(new ReleaseForm(form));
			mSqlDb.execute(ds);
			mSqlDb.close();
		}
		catch(Exception ex)
		{
			Log.e("", ex.getMessage());
		}
	}

	/**
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣִֶвѯ
	 * @param Collector
	 * @param form ǰ
	 */
	public void collector(ICollector Collector, Activity form)
	{
		try
		{
			mSqlDb.getCollectors().add(Collector);
			mSqlDb.getReleasers().add(new ReleaseForm(form));
			mSqlDb.setSql(mSql);
			mSqlDb.executeTable();
			mSqlDb.close();
		}
		catch(Exception ex)
		{
			Log.e("ռ", ex.getMessage());
		}
	}

	/**
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣִݷ
	 */
	public void release()
	{
		try
		{
			execute();
		}
		catch(Exception ex)
		{
			Log.v("ݴ", ex.getMessage());
		}
	}

	/**
	 *@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣ
	 */
	public void dataInitialize()
	{
		String classnameString = mActivity.getPackageName() + ".R$id";
		this.setText(InitializeHelper.getName(classnameString, getId()));
		// Release();
	}

	/**
	 **@:   2012-5-22
	 *@޸:
	 *@޸ʱ䣺
	 *@ܣִֶݷ
	 */
	public void query()
	{
		try
		{
			if(mAutoLoad)
			{
				execute();
			}
		}
		catch(Exception ex)
		{
			Log.v("ݷ", ex.getMessage());
		}
	}
	
	@Override
	public void release(Object arg0)
	{
		query();
	}

	public Object getActivity()
	{
		return mActivity;
	}

	public void setActivity(Object obj)
	{
		if(obj instanceof Activity)
		{
			mActivity = (Activity) obj;
		}
	}

	public Boolean getAutoLoad()
	{
		return mAutoLoad;
	}

	public void setAutoLoad(Boolean autoLoad)
	{
		this.mAutoLoad = autoLoad;
	}

	public String getSql()
	{
		return mSql;
	}

	public void setSql(String sql)
	{
		this.mSql = sql;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public DataType getDataType()
	{
		return mDataType;
	}

	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}
}
