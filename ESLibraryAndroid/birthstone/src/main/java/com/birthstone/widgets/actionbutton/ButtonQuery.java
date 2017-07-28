package com.birthstone.widgets.actionbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.event.OnClickedListener;
import com.birthstone.base.activity.event.OnClickingListener;
import com.birthstone.base.activity.helper.FormHelper;
import com.birthstone.base.activity.helper.InitializeHelper;
import com.birthstone.base.activity.helper.MessageBox;
import com.birthstone.base.activity.parse.CollectForm;
import com.birthstone.base.activity.parse.DataBindForm;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataTable;

import java.util.LinkedList;


public class ButtonQuery extends android.widget.Button implements IFunctionProtected, IStateProtected, IDataInitialize, IDataQuery, IReleasable
{
	protected String mNameSpace = "http://schemas.android.com/res/eruntech.birthStone.widgets";
	protected SQLiteDatabase mSqlDb;
	protected Boolean mAutoLoad = false;
	protected String mFuncSign;
	protected String mSign;
	protected String mName;
	protected String mSql;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected String mOpenForm = "";
	protected Activity mActivity;
	protected OnClickingListener onClickingListener;
	protected OnClickedListener onClickedListener;
	protected DataTable mDataSource = null;
	protected CollectForm mCollForm;

	public ButtonQuery( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			this.setOnClickListener(clickListener);
			this.mOpenForm = attrs.getAttributeValue(mNameSpace, "openForm");
			this.mSign = attrs.getAttributeValue(mNameSpace, "sign");
			this.mStateHiddenId = attrs.getAttributeValue(mNameSpace, "stateHiddenId");
			this.mWantedStateValue = attrs.getAttributeValue(mNameSpace, "wantedStateValue");
			this.mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
			this.mSql = attrs.getAttributeValue(mNameSpace, "sql");
			this.mAutoLoad = attrs.getAttributeBooleanValue(mNameSpace, "autoLoad", false);
		}
		catch(Exception ex)
		{
			Log.e("ButtonQuery", ex.getMessage());
		}
	}

	public ButtonQuery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void click()
	{
		try
		{
			openForm();
		}
		catch(Exception ex)
		{
			Log.v("ButtonOpen", ex.getMessage());
		}
	}

	protected OnClickListener clickListener = new OnClickListener()
	{
		public Boolean onClicking()
		{
			if(onClickingListener != null)
			{
				return onClickingListener.onClicking();
			}
			return false;
		}

		public void onClick(View v)
		{
			if(onClicking()) 
			{ 
				return; 
			}
			click();
			onClicked();
		}

		public void onClicked()
		{
			if(onClickedListener != null)
			{
				onClickedListener.onClicked();
			}
		}
	};

	public void execute() throws Exception
	{
		try
		{
			if(mThread.getState().equals(Thread.State.WAITING))
			{
				mThread.run();
			}
			else if(mThread.getState().equals(Thread.State.NEW))
			{
				mThread.start();
			}
			else if(mThread.getState().equals(Thread.State.TERMINATED))
			{
				mThread.run();
			}
		}
		catch(Exception ex)
		{
			MessageBox.showMessage(mActivity, "Ϣ", ex.getMessage());
		}
	}

	private Boolean collect()
	{
		try
		{
			if(checkSqlIsNull())
			{
				MessageBox.showMessage(mActivity, "", "ִеSql");
				return false;
			}
			mSqlDb = new SQLiteDatabase(mActivity.getApplicationContext());
			mSqlDb.setSql(mSql);
			mCollForm = new CollectForm(mActivity, mSign);
			mSqlDb.getCollectors().add((ICollector) mCollForm);
			return true;
		}
		catch(Exception ex)
		{
			Log.e("collect", ex.getMessage());
		}
		return false;
	}

	private Boolean checkSqlIsNull()
	{
		if(mSql != null && mSql.trim().length() > 0) { return false; }
		return true;
	}

	Thread mThread = new Thread(new Runnable()
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(collect())
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

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			try
			{
				// ͨжmsg.whatжĸ""Ҫд
				switch(msg.what)
				{
				case 1:
					mDataSource = mSqlDb.executeTable();
					break;
				}
			}
			catch(Exception ex)
			{
				Log.v("ExecuteHandleMessage", ex.getMessage());
			}
		}
	};

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
	}

	public void protectState(Boolean arg0)
	{

		if(arg0)
		{
			this.setVisibility(View.INVISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}

	public void query()
	{
		try
		{
			if(mAutoLoad)
			{
				execute();
				DataBindForm dataBindForm = new DataBindForm(mActivity, mDataSource, mName);
				dataBindForm.bind();
			}
		}
		catch(Exception ex)
		{
			Log.v("ButtonQuery", ex.getMessage());
		}

	}

	private Boolean openForm()
	{
		try
		{
			FormHelper formHelper = new FormHelper();
			if(mOpenForm.contains("."))
			{
				formHelper.open(mActivity, mOpenForm);
			}
			else
			{
				formHelper.open(mActivity, mActivity.getClass().getPackage().getName() + "." + mOpenForm);
			}

		}
		catch(Exception ex)
		{
			Log.v("ButtonOpen", ex.getMessage());
		}
		return true;
	}

	public LinkedList<String> getRequest()
	{
		return null;
	}

	/**
	 * 发布数据到UIView 
	 *@param dataName 数据名称 
	 *@param data 数据对象 
	 **/
	public void release(String dataName, Data data)
	{

		if(mAutoLoad && dataName != null)
		{
			if(dataName.equals("BtnQuery"))
			{
				try
				{
					execute();
					DataBindForm dataBindForm = new DataBindForm(mActivity, mDataSource, mName);
					dataBindForm.bind();
				}
				catch(Exception ex)
				{
					Log.v("ButtonQuery", ex.getMessage());
				}
			}
		}

	}

	public String getName()
	{
		return mName;
	}

	/*
	 *
	 */
	public String[] getFuncSigns()
	{

		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	public Object getActivity()
	{
		return mActivity;
	}

	public void setActivity(Object arg0)
	{
		if(arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
		}
	}

	public String getStateHiddenId()
	{
		return mStateHiddenId;
	}

	public String getWantedStateValue()
	{
		return mWantedStateValue;
	}

	/*
	 *
	 */
	public void setVisible(Boolean arg0)
	{
		if(arg0)
		{
			this.setVisibility(View.INVISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public SQLiteDatabase getSqlDb()
	{
		return mSqlDb;
	}

	public void setSqlDb(SQLiteDatabase sqlDb)
	{
		this.mSqlDb = sqlDb;
	}

	public Boolean getAutoLoad()
	{
		return mAutoLoad;
	}

	public void setAutoLoad(Boolean autoLoad)
	{
		this.mAutoLoad = autoLoad;
	}

	public String[] getFuncSign()
	{
		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	public void setFuncSign(String funcSign)
	{
		this.mFuncSign = funcSign;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public String getSql()
	{
		return mSql;
	}

	public void setSql(String sql)
	{
		this.mSql = sql;
	}

	public String getOpenForm()
	{
		return mOpenForm;
	}

	public void setOpenForm(String openForm)
	{
		this.mOpenForm = openForm;
	}

	public OnClickingListener getOnClickingListener()
	{
		return onClickingListener;
	}

	public void setOnClickingListener(OnClickingListener onClickingListener)
	{
		this.onClickingListener = onClickingListener;
	}

	public OnClickedListener getOnClickedListener()
	{
		return onClickedListener;
	}

	public void setOnClickedListener(OnClickedListener onClickedListener)
	{
		this.onClickedListener = onClickedListener;
	}

	public DataTable getDataSource()
	{
		return mDataSource;
	}

	public void setDataSource(DataTable dataSource)
	{
		this.mDataSource = dataSource;
	}

	public CollectForm getCollForm()
	{
		return mCollForm;
	}

	public void setCollForm(CollectForm collForm)
	{
		this.mCollForm = collForm;
	}

	public OnClickListener getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(OnClickListener clickListener)
	{
		this.clickListener = clickListener;
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public void setStateHiddenId(String stateHiddenId)
	{
		this.mStateHiddenId = stateHiddenId;
	}

	public void setWantedStateValue(String wantedStateValue)
	{
		this.mWantedStateValue = wantedStateValue;
	}

}
