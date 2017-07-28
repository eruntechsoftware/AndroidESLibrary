package com.birthstone.widgets.actionbutton;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.event.OnClickedListener;
import com.birthstone.base.activity.event.OnClickingListener;
import com.birthstone.base.activity.helper.MessageBox;
import com.birthstone.base.activity.parse.CollectForm;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.core.parse.DataTable;


public class ButtonQueryLocalForm extends android.widget.Button implements IFunctionProtected, IStateProtected, IDataInitialize, IDataQuery
{
	protected SQLiteDatabase mSqlDb;
	protected Boolean mAutoLoad = false;
	protected String mFuncSign;
	protected String mSign;
	protected String mSql;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected Activity mActivity;
	protected OnClickingListener onClickingListener;
	protected OnClickedListener onClickedListener;
	protected String mNameSpace = "http://schemas.android.com/res/eruntech.birthStone.widgets";
	protected DataTable mDataSource = null;
	protected CollectForm mCollForm;

	public ButtonQueryLocalForm( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			setOnClickListener(clickListener);
			mSign = attrs.getAttributeValue(mNameSpace, "sign");
			mStateHiddenId = attrs.getAttributeValue(mNameSpace, "stateHiddenId");
			mWantedStateValue = attrs.getAttributeValue(mNameSpace, "wantedStateValue");
			mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
			mSql = attrs.getAttributeValue(mNameSpace, "sql");
			mAutoLoad = attrs.getAttributeBooleanValue(mNameSpace, "autoLoad", false);
		}
		catch(Exception ex)
		{
			Log.v("ButtonQueryLocalForm", ex.getMessage());
		}
	}

	public void click()
	{
		try
		{
			executeTable();
		}
		catch(Exception ex)
		{
			Log.v("ButtonQueryLocal", ex.getMessage());
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

	public DataTable executeTable()
	{
		try
		{
			collect();
			mDataSource = mSqlDb.executeTable();
		}
		catch(Exception ex)
		{
			MessageBox.showMessage(mActivity, "Ϣ", ex.getMessage());
		}
		return mDataSource;
	}
	
	public Cursor executeCursor()
	{
		Cursor cursor = null;
		try
		{
			collect();
			cursor = mSqlDb.executeCursor();
		}
		catch(Exception ex)
		{
			MessageBox.showMessage(mActivity, "Ϣ", ex.getMessage());
		}
		return cursor;
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
		if(mSql != null && mSql.trim().length() > 0)
		{
			Log.v("CheckSql", "false");
			return false;
		}
		Log.v("CheckSql", "true");
		return true;
	}

	public void dataInitialize()
	{

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

	public void protectState(Boolean arg0)
	{

		if(arg0)
		{
			setVisibility(View.VISIBLE);
		}
		else
		{
			setVisibility(View.GONE);
		}
	}

	public void query()
	{
		try
		{
			if(mAutoLoad)
			{
				executeTable();
			}
		}
		catch(Exception ex)
		{
			Log.v("ButtonQueryLocal", ex.getMessage());
		}

	}

	/*
	 *
	 */
	public String[] getFuncSigns()
	{
		return StringToArray.stringConvertArray(mFuncSign);
	}

	/*
	 *
	 */
	public void setVisible(Boolean arg0)
	{
		if(arg0)
		{
			setVisibility(View.INVISIBLE);
		}
		else
		{
			setVisibility(View.GONE);
		}
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
		return StringToArray.stringConvertArray(mFuncSign);
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

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
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

	public void setStateHiddenId(String stateHiddenId)
	{
		this.mStateHiddenId = stateHiddenId;
	}

	public void setWantedStateValue(String wantedStateValue)
	{
		this.mWantedStateValue = wantedStateValue;
	}

}
