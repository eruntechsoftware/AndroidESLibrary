package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.birthstone.base.parse.CollectForm;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.LinkedList;


public class ESSpinner extends android.widget.Spinner implements ICollectible, IValidatible, IReleasable, IDataInitialize, ICellTitleStyleRequire, IDataQuery
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
	protected Boolean mAutoLoad = true;
	protected Object mSelectValue = "";
	protected String mSelectText = "";
	protected DataTable mDataTable;
	protected String mCharCode;
	protected String mTipText = "";
	protected String mDefaultValue = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	protected String[] displayArray = null;
	protected String[] valueArray = null;
	protected OnItemSelectIndexChangeListener mOnItemSelectIndexChangeListener;

	public ESSpinner(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.View);
			if(mCollectName == null)
			{
				mCollectName = a.getString(R.styleable.View_collectName);
			}
			mDisplayValue = a.getString(R.styleable.View_displayValue);

			mBindValue = a.getString(R.styleable.View_bindValue);
			mSql = a.getString(R.styleable.View_sql);
			mIsRequired = a.getBoolean(R.styleable.View_isRequired,false);
			mCollectSign = a.getString(R.styleable.View_collectSign);
			mSign = a.getString(R.styleable.View_sign);
			if(mSign == null)
			{
				mSign = "";
			}
			mEmpty2Null = a.getBoolean(R.styleable.View_empty2Null, true);
			mIsEmpty = a.getBoolean(R.styleable.View_isEmpty, true);
			mAutoLoad = a.getBoolean(R.styleable.View_autoLoad, true);
			mDataType = DataType.String;
			this.setOnItemSelectedListener(onItemSelected);
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

	public void query()
	{
		try
		{
			if(mAutoLoad)
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
	 * б
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
	 * 
	 * @author MinG
	 *
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
					bindAdapter();
					// setPrompt("ѡ");
					break;
				case 2:
					swap(msg.arg1, msg.arg2);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, displayArray);
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
	
	public void bindAdapter()
	{
		SQLiteDatabase sqlDb = new SQLiteDatabase(mActivity.getApplicationContext());
		mDataTable = sqlDb.executeTable(mSql, new CollectForm(mActivity, mSign).collect(), mCharCode);
		sqlDb.close();
		tableToArray();
		if(mIsRequired == true)
		{
			if(mDataTable == null || mDataTable.size() == 0)
			{
				mTipText = "";
			}
		}
		if(displayArray != null)
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, displayArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			setAdapter(adapter);
			ESSpinner.this.setVisibility(View.VISIBLE);
		}
	}

	public void bind(String[] displayArray, String[] valueArray, Activity form)
	{
		try
		{
			this.displayArray = displayArray;
			this.valueArray = valueArray;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(form, android.R.layout.simple_spinner_item, displayArray);
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
	 * 数据收集，返回DataCollection
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

	private void tableToArray()
	{
		StringBuffer displayBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		int size = 0;
		try
		{
			if(mDataTable != null)
			{
				size = mDataTable.size();
				for(int index = 0; index < size; index++)
				{
					displayBuffer.append("|!").append(mDataTable.get(index).get(mDisplayValue).getValue().toString());
					valueBuffer.append("|!").append(mDataTable.get(index).get(mBindValue).getValue().toString());
				}
				if(displayBuffer.length() > 0)
				{
					String displaystring = "";
					String valuestring = "";
					if(!mIsEmpty)
					{
						displaystring = displayBuffer.substring(2);
						valuestring = valueBuffer.substring(2);
					}
					else
					{
						displaystring = "ѡ..." + displayBuffer.toString();
						valuestring = valueBuffer.toString();
					}
					displayArray = StringToArray.stringConvertArray(displaystring);
					valueArray = StringToArray.stringConvertArray(valuestring);
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
					mSelectValue = valueArray[0];
					mSelectText = displayArray[0];
				}
			}
		}
		catch(Exception ex)
		{
			Log.v("spinnertoArray", ex.getMessage());
		}
	}
	
	OnItemClickListener onItemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Log.v("SelectText", String.valueOf(arg2));
		}
		
	};

	OnItemSelectedListener onItemSelected = new OnItemSelectedListener()
	{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			mSelectValue = valueArray[arg2];
			Log.v("SelectValue", "**" + mSelectValue.toString());
			mSelectText = displayArray[arg2];
			Log.v("SelectText", mSelectText);
			if(ESSpinner.this.mOnItemSelectIndexChangeListener != null)
			{
				ESSpinner.this.mOnItemSelectIndexChangeListener.selectIndexChange(mSelectValue.toString());
			}
		}

		public void onNothingSelected(AdapterView<?> arg0)
		{
			dataValidator();
		}

	};

	/** 
	 * 数据类型校验，并返回是否成功 
	 * @return 是否校验成功 
	 * **/
	public Boolean dataValidator()
	{
		try
		{
			mTipText = ValidatorHelper.createDataTypeValidator(mDataType, mSelectValue.toString());
			Log.v("DataTypeValidator", mTipText);
			if(mIsRequired)
			{
				Log.v("IsRequiredValidator", mTipText);
				mTipText = ValidatorHelper.createRequiredValidator(mSelectValue.toString().trim());
			}
			invalidate();
			if(mTipText.length() != 0) { return false; }
		}
		catch(Exception ex)
		{
			Log.e("Validator", ex.getMessage());
		}
		return true;
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
			Log.e("Ĭֵ߳", ex.getMessage());
		}
	}

	private boolean setDefaultText(String curoperno)
	{
		Log.i("Ĭֵ", curoperno);
		while(true)
		{
			if(valueArray != null)
			{
				int size = valueArray.length;
				for(int i = 0; i < size; i++)
				{
					String operno = valueArray[i].toLowerCase().trim();
					Log.v("operno", operno);
					if(operno.equals(curoperno.toLowerCase().trim()))
					{
						String tempValue = "";
						String tempText = "";
						tempValue = valueArray[i];
						tempText = displayArray[i];
						Log.d("tempValue", tempValue);
						valueArray[i] = valueArray[0];
						displayArray[i] = displayArray[0];
						Log.d("index0:", displayArray[i]);
						valueArray[0] = tempValue;
						displayArray[0] = tempText;
						Log.d("index2:", displayArray[0]);
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
				tempValue = valueArray[index];
				tempText = displayArray[index];
				Log.d("tempValue", tempValue);
				valueArray[index] = valueArray[index2];
				displayArray[index] = displayArray[index2];
				Log.d("index0:", displayArray[index]);
				valueArray[index2] = tempValue;
				displayArray[index2] = tempText;
				Log.d("index2:", displayArray[index2]);
				return true;
			}
		}
	}

	/**
	 * Jump directly to a specific item in the adapter data.
	 */
	public void setSelection(int position)
	{
		startSwap(0, position);
	}

	public void setDefaultValue(String value)
	{
		startSwap(value);
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.RED);

		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// ַ
		canvas.drawText(mTipText, 8, this.getHeight() / 2 + 5, mPaint);
	}

	public String getCollectName()
	{
		return mCollectName;
	}

	public void setCollectName(String collectName)
	{
		this.mCollectName = collectName;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public Boolean getIsEmpty()
	{
		return mIsEmpty;
	}

	public void setIsEmpty(Boolean isEmpty)
	{
		this.mIsEmpty = isEmpty;
	}

	public String getDisplayValue()
	{
		return mDisplayValue;
	}

	public void setDisplayValue(String displayValue)
	{
		this.mDisplayValue = displayValue;
	}

	public String getBindValue()
	{
		return mBindValue;
	}

	public void setBindValue(String bindValue)
	{
		this.mBindValue = bindValue;
	}

	public String getSql()
	{
		return mSql;
	}

	public void setSql(String sql)
	{
		this.mSql = sql;
	}

	public Boolean getAutoLoad()
	{
		return mAutoLoad;
	}

	public void setAutoLoad(Boolean autoLoad)
	{
		this.mAutoLoad = autoLoad;
	}

	public Object getSelectValue()
	{
		return mSelectValue;
	}

	public void setSelectValue(Object selectValue)
	{
		this.mSelectValue = selectValue;
	}

	public String getSelectText()
	{
		return mSelectText;
	}

	public void setSelectText(String selectText)
	{
		this.mSelectText = selectText;
	}

	public DataTable getDataTable()
	{
		return mDataTable;
	}

	public void setDataTable(DataTable dataTable)
	{
		this.mDataTable = dataTable;
	}

	public String getCharCode()
	{
		return mCharCode;
	}

	public void setCharCode(String charCode)
	{
		this.mCharCode = charCode;
	}

	public String getTipText()
	{
		return mTipText;
	}

	public void setTipText(String tipText)
	{
		this.mTipText = tipText;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public String[] getDisplayArray()
	{
		return displayArray;
	}

	public void setDisplayArray(String[] displayArray)
	{
		this.displayArray = displayArray;
	}

	public String[] getValueArray()
	{
		return valueArray;
	}

	public void setValueArray(String[] valueArray)
	{
		this.valueArray = valueArray;
	}

	public OnItemSelectedListener getSelected()
	{
		return onItemSelected;
	}

	public void setSelected(OnItemSelectedListener selected)
	{
		this.onItemSelected = selected;
	}

	public String getDefaultValue()
	{
		return mDefaultValue;
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName()
	{
		return mName;
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

	public void setName(String name)
	{
		this.mName = name;
	}

	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
	}

	public DataType getDataType()
	{
		return mDataType;
	}

	public void setIsRequired(Boolean isRequired)
	{
		this.mIsRequired = isRequired;
	}

	public Boolean getIsRequired()
	{
		return mIsRequired;
	}

	public void setOnItemSelectIndexChangeListener(OnItemSelectIndexChangeListener onItemSelectIndexChangeListener)
	{
		this.mOnItemSelectIndexChangeListener = onItemSelectIndexChangeListener;
	}
}
