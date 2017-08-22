package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnItemSelectIndexChangeListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.parse.CollectController;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.LinkedList;
import java.util.List;


public class ESSpinner extends android.widget.Spinner implements ICollectible, IReleasable, IDataInitialize, ICellTitleStyleRequire, IDataQuery,AdapterView.OnItemSelectedListener
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectName;
	protected String mCollectSign;
	protected String mSign = "ForQuery";
	protected Boolean mEmpty2Null = true;
	protected Boolean mIsEmpty = false;
	protected Activity mActivity;
	protected String mName;
	protected String mDisplayValue;
	protected String mBindValue;
	protected String mSql;
	protected Boolean mAutomatic = true;
	protected Object mSelectValue = "";
	protected String mSelectText = "";
	protected DataTable mDataTable;
	protected String mCharCode;

	protected String mDefaultValue = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	protected Object[] displayArray = null;
	protected Object[] valueArray = null;
	protected OnItemSelectIndexChangeListener mOnItemSelectIndexChangeListener;

	public ESSpinner(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESSpinner);
			if(mCollectName == null)
			{
				mCollectName = a.getString(R.styleable.ESSpinner_collectName);
			}
			mDisplayValue = a.getString(R.styleable.ESSpinner_displayValue);

			mBindValue = a.getString(R.styleable.ESSpinner_bindValue);
			mSql = a.getString(R.styleable.ESSpinner_sql);
			mIsRequired = a.getBoolean(R.styleable.ESSpinner_isRequired,false);
			mCollectSign = a.getString(R.styleable.ESSpinner_collectSign);
			mSign = a.getString(R.styleable.ESSpinner_sign);
			if(mSign == null)
			{
				mSign = "";
			}
			mEmpty2Null = a.getBoolean(R.styleable.ESSpinner_empty2Null, true);
			mIsEmpty = a.getBoolean(R.styleable.ESSpinner_isEmpty, true);
			mAutomatic = a.getBoolean(R.styleable.ESSpinner_automatic, true);
			mDataType = DataType.String;
			this.setOnItemSelectedListener(this);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("Spinner", ex.getMessage());
		}
	}

	public ESSpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
			if(mCollectName == null || mCollectName.equals(""))
			{
				mCollectName = mName;
			}
		}
	}

	/**
	 * 执行sql查询并绑定到适配器
	 * */
	public void query()
	{
		try
		{
			if(mAutomatic)
			{
				new BindDataThread().start();
			}
		}
		catch(Exception ex)
		{
			Log.v("DropDownList", ex.getMessage());
		}
	}

	/**
	 * 绑定数据源
	 * **/
	public void bind()
	{
		try
		{
			new BindDataThread().start();
		}
		catch(Exception ex)
		{

			Log.v("DropDownList", ex.getMessage());
		}

	}

	/**
	 *数据源绑定处理线程
	 */
	class BindDataThread  extends Thread
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(true)
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	}

	/**
	 * 接收数据，并将数据绑定到适配器
	 * **/
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			try
			{
				switch(msg.what)
				{
				case 1:
					bindDataToAdapter();
					break;
				case 2:
					swap(msg.arg1, msg.arg2);
					ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(mActivity, android.R.layout.simple_spinner_item, displayArray);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					setAdapter(adapter);
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
	 * 绑定数据到适配器
	 * */
	private void bindDataToAdapter()
	{
		SQLiteDatabase sqlDb = new SQLiteDatabase(mActivity.getApplicationContext());
		mDataTable = sqlDb.executeTable(mSql, new CollectController(mActivity, mSign).collect(), mCharCode);
		sqlDb.close();
		dataTableToArray();
		if(displayArray != null)
		{
			ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(mActivity, android.R.layout.simple_spinner_item, displayArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			setAdapter(adapter);
			ESSpinner.this.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 绑定指定数据到适配器
	 * @param displayArray 显示数据数组
	 * @param valueArray 绑定数据数组
	 * @param activity 屏幕对象
	 * **/
	public void bind(String[] displayArray, String[] valueArray, Activity activity)
	{
		try
		{
			this.displayArray = displayArray;
			this.valueArray = valueArray;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, displayArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.setAdapter(adapter);
			if(valueArray != null)
			{
				mSelectValue = valueArray[0];
				mSelectText = displayArray[0];
			}
			// this.setPrompt("ѡ");
		}
		catch(Exception ex)
		{
			Log.v("DropDownList", ex.getMessage());
		}
	}

	/**
	 * 获取采集标签名称
	 * @return 标签名称
	 * */
	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	/**
	 * 发布数据到UIView 
	 *@param dataName 数据名称 
	 *@param data 数据对象 
	 **/
	public void release(String dataName, Data data)
	{
		if(dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(data.getValue() != null)
			{
				setDefaultValue(data.getValue().toString());
			}
		}
	}

	/**
	 * 数据收集
	 * @return 数据集合对象
	 * **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		if(mSelectText.equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(mCollectName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mCollectName, mSelectValue.toString().trim(), mDataType));
		}
		return datas;
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	/**
	 * 将DataTable数据源转换为Array对象
	 * */
	private void dataTableToArray()
	{
		List<String>displayList = new LinkedList<>();
		List<String>valueList = new LinkedList<>();
		int size = 0;
		try
		{
			if(mDataTable != null)
			{
				size = mDataTable.size();
				for(int index = 0; index < size; index++)
				{
					displayList.add(mDataTable.get(index).get(mDisplayValue).getValue().toString());
					valueList.add(mDataTable.get(index).get(mBindValue).getValue().toString());
				}
				if(displayList.size() > 0)
				{
					displayArray = displayList.toArray();
					valueArray = valueList.toArray();
					if(!mDefaultValue.equals(""))
					{
						setDefaultText(mDefaultValue);
					}
				}
				if(mIsEmpty)
				{
					mSelectValue = "";
				}
				else if(valueArray != null && !mIsEmpty)
				{
					mSelectValue = valueArray[0].toString();
					mSelectText = displayArray[0].toString();
				}
			}
		}
		catch(Exception ex)
		{
			Log.v("spinnertoArray", ex.getMessage());
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		mSelectValue = valueArray[arg2];
		mSelectText = displayArray[arg2].toString();
		if(ESSpinner.this.mOnItemSelectIndexChangeListener != null)
		{
			ESSpinner.this.mOnItemSelectIndexChangeListener.selectIndexChange(mSelectValue.toString());
		}
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	private void startSwap(final int index, final int index2)
	{
		new Thread()
		{
			public void run()
			{
				Message msg = new Message();
				if(Looper.myLooper() == null)
				{
					Looper.prepare();
				}
				if(true)
				{
					msg.arg1 = index;
					msg.arg2 = index2;
					msg.what = 2;
					handler.sendMessage(msg);
				}
				Looper.loop();
			}
		}.start();
	}

	private void startSwap(final String operno)
	{
		try
		{
			new Thread()
			{
				public void run()
				{
					Message msg = new Message();
					if(Looper.myLooper() == null)
					{
						Looper.prepare();
					}
					if(setDefaultText(operno))
					{
						msg.what = 2;
						handler.sendMessage(msg);
					}
					Looper.loop();
				}
			}.start();
		}
		catch(Exception ex)
		{
			Log.e("startSwap", ex.getMessage());
		}
	}

	private boolean setDefaultText(String curoperno)
	{
		while(true)
		{
			if(valueArray != null)
			{
				int size = valueArray.length;
				for(int i = 0; i < size; i++)
				{
					String operno = valueArray[i].toString().toLowerCase().trim();
					Log.v("operno", operno);
					if(operno.equals(curoperno.toLowerCase().trim()))
					{
						String tempValue = "";
						String tempText = "";
						tempValue = valueArray[i].toString();
						tempText = displayArray[i].toString();
						valueArray[i] = valueArray[0];
						displayArray[i] = displayArray[0];
						valueArray[0] = tempValue;
						displayArray[0] = tempText;
						return true;
					}
					if(i + 1 == size) { return false; }
				}
			}
		}
	}

	private boolean swap(int index, int index2)
	{
		while(true)
		{
			if(displayArray != null)
			{
				String tempValue = "";
				String tempText = "";
				tempValue = valueArray[index].toString();
				tempText = displayArray[index].toString();
				valueArray[index] = valueArray[index2];
				displayArray[index] = displayArray[index2];
				valueArray[index2] = tempValue;
				displayArray[index2] = tempText;
				return true;
			}
		}
	}

	public void setSelection(int position)
	{
		startSwap(0, position);
	}

	public void setDefaultValue(String value)
	{
		startSwap(value);
	}

	public String getDisplayValue()
	{
		return mDisplayValue;
	}

	public String getBindValue()
	{
		return mBindValue;
	}


	public Object[] getDisplayArray()
	{
		return displayArray;
	}

	public void setDisplayArray(String[] displayArray)
	{
		this.displayArray = displayArray;
	}

	public Object[] getValueArray()
	{
		return valueArray;
	}

	public void setValueArray(String[] valueArray)
	{
		this.valueArray = valueArray;
	}

	public String getDefaultValue()
	{
		return mDefaultValue;
	}


	@Override
	public void setDataType(DataType dataType) {
		this.mDataType = dataType;
	}

	@Override
	public DataType getDataType() {
		return mDataType;
	}

	@Override
	public void setIsRequired(Boolean isRequired) {
		this.mIsRequired = isRequired;
	}

	@Override
	public Boolean getIsRequired() {
		return mIsRequired;
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName()
	{
		return mName;
	}

	@Override
	public Object getActivity() {
		return mActivity;
	}

	public void setActivity(Object arg0)
	{
		if(arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
		}
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public void setOnItemSelectIndexChangeListener(OnItemSelectIndexChangeListener onItemSelectIndexChangeListener)
	{
		this.mOnItemSelectIndexChangeListener = onItemSelectIndexChangeListener;
	}
}
