package com.birthstone.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnTextBoxChangedListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeHelper;
import com.birthstone.core.helper.DateTimeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;


public class TextBox extends EditText implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectSign;
	protected Boolean mEmpty2Null = true;
	protected Activity mActivity;
	protected String mName;
	protected String mTipText = "";
	protected String mUserTipText = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";
	private OnTextBoxChangedListener onTextBoxChangedListener;

	public TextBox(Context context)
	{
		super(context);
	}
	
	public TextBox( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			mTipText = attrs.getAttributeValue(mNameSpace, "tipText");
			mUserTipText = attrs.getAttributeValue(mNameSpace, "tipText");
			mIsRequired = attrs.getAttributeBooleanValue(mNameSpace, "isRequired", false);
			mCollectSign = attrs.getAttributeValue(mNameSpace, "collectSign");
			mEmpty2Null = attrs.getAttributeBooleanValue(mNameSpace, "empty2Null", true);
			this.addTextChangedListener(textOnchange);
			
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
				this.mDataType = DataType.String;
			}
		}
		catch(Exception ex)
		{
			Log.e("TextBox", ex.getMessage());
		}

	}
	
	public TextBox(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		String value =  this.getText().toString().trim();
		if(mTipText!=null && !"".equals(mTipText) && mIsRequired==true && value.length()==0)
		{
			Paint mPaint = new Paint();
			mPaint.setColor(Color.RED);
			mPaint.setTextSize(this.getTextSize());
			mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
			Rect rect = new Rect();
			mPaint.getTextBounds(mTipText, 0, mTipText.length(), rect);
			
			canvas.drawText(mTipText, this.getPaddingLeft()+8, this.getHeight() / 2 + rect.height()/2, mPaint);
		}
	}

	public Boolean dataValidator()
	{
		try
		{
			mTipText = ValidatorHelper.createDataTypeValidator(mDataType, getText().toString());
			if(mIsRequired)
			{
				if(mUserTipText !=null && !"".equals(mUserTipText))
				{
					mTipText = mUserTipText;
				}
				else
				{
					mTipText = ValidatorHelper.createRequiredValidator(getText().toString().trim());
				}
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

	private TextWatcher textOnchange = new TextWatcher()
	{
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			try
			{
				if(this != null)
				{
					Log.v("changeEvent", "true");
					if(mIsRequired)
					{
						dataValidator();
					}
					if(!getText().equals(null))
					{
						dataValidator();
					}
					if(onTextBoxChangedListener != null)
					{
						onTextBoxChangedListener.onTextBoxChanged(getText().toString());
					}
				}
			}
			catch(Exception ex)
			{
				Log.v("ValidatorError", ex.getMessage());
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		public void afterTextChanged(Editable s)
		{

		}
	};

	public DataType getDataType()
	{
		return mDataType;
	}

	public Boolean getIsRequired()
	{
		return mIsRequired;
	}

	public void setDataType(DataType arg0)
	{
		mDataType = arg0;
	}

	public void setIsRequired(Boolean arg0)
	{
		mIsRequired = arg0;
	}

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
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

	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	public void release(String dataName, Data data)
	{
		if(dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(data.getValue() == null)
			{
				this.setText("");
				return;
			}
			if(this.mDataType == null)
			{
				this.setText(data.getValue().toString());
			}
			else if(this.mDataType.equals(DataType.Date))
			{
				this.setText(DateTimeHelper.getDateString(data.getValue(), DateTimeHelper.getDateFormat()));
			}
			else if(this.mDataType.equals(DataType.DateTime))
			{
				this.setText(DateTimeHelper.getDateString(data.getValue(), DateTimeHelper.getDateTimeFormat()));
			}
			else
			{
				this.setText(data.getValue().toString());
			}
		}
	}

	public DataCollection collect()
	{

		DataCollection datas = new DataCollection();
		if(this.getText().equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, getText().toString(), mDataType));
		}
		return datas;
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public String getTipText()
	{
		return mTipText;
	}

	public void setTipText(String tipText)
	{
		this.mTipText = tipText;
		this.mUserTipText = tipText;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public TextWatcher getTextOnchange()
	{
		return textOnchange;
	}

	public void setTextOnchange(TextWatcher textOnchange)
	{
		this.textOnchange = textOnchange;
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName()
	{
		return mName;
	}

	public OnTextBoxChangedListener getOnTextBoxChangedListener()
	{
		return onTextBoxChangedListener;
	}

	public void setOnTextBoxChangedListener(OnTextBoxChangedListener onTextBoxChangedListener)
	{
		this.onTextBoxChangedListener = onTextBoxChangedListener;
	}
}
