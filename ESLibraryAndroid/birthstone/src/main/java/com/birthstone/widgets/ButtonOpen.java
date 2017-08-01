package com.birthstone.widgets;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickedListener;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.base.helper.FormHelper;
import com.birthstone.base.parse.CollectForm;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.core.parse.DataCollection;


public class ButtonOpen extends Button implements IDataInitialize, IFunctionProtected, IStateProtected
{
	protected Activity mActivity;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected String mOpenForm;
	protected String mSign;
	protected String mFuncSign = "";
	protected Boolean mIsClosed = false;
	protected OnClickingListener onClickingListener;
	protected OnClickedListener onClickedListener;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ButtonOpen( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			setOnClickListener(clickListener);
			mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
			mOpenForm = attrs.getAttributeValue(mNameSpace, "openForm");
			if(mOpenForm==null || "".equals(mOpenForm))
			{
				mOpenForm = attrs.getAttributeValue(mNameSpace, "open");
			}
			mSign = attrs.getAttributeValue(mNameSpace, "sign");
			mStateHiddenId = attrs.getAttributeValue(mNameSpace, "stateHiddenId");
			mWantedStateValue = attrs.getAttributeValue(mNameSpace, "wantedStateValue");
			mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
			mIsClosed = attrs.getAttributeBooleanValue(mNameSpace, "isClosed", false);
		}
		catch(Exception ex)
		{
			Log.e("ButtonOpen", ex.getMessage());
		}
	}

	public ButtonOpen(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	protected OnClickListener clickListener = new OnClickListener()
	{
		public Boolean onClicking()
		{
			if(onClickingListener != null) { return onClickingListener.onClicking(); }
			return false;
		}

		public void onClick(View v)
		{
			if(onClicking()) { return; }
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

	public void protectState(Boolean arg0)
	{
		if(!arg0)
		{
			this.setVisibility(View.GONE);
		}
	}

	public void dataInitialize()
	{

	}


	public void click()
	{
		new Thread()
		{
			public void run()
			{
				if(Looper.myLooper() == null)
				{
					Looper.prepare();
				}
				try
				{

					FormHelper formHelper = new FormHelper();
					if(mSign != null)
					{
						CollectForm collectForm = new CollectForm(mActivity, mSign);
						DataCollection datas = collectForm.collect();
						Log.v("OpenFormDatas", String.valueOf(datas.size()));
						// MessageBox.ShowMessage(Form, "Ϣ",
						// String.valueOf(Form.Controls.size()));
						if(mOpenForm.contains("."))
						{
							formHelper.open(mActivity, mOpenForm, datas);
						}
						else
						{
							formHelper.open(mActivity, mActivity.getClass().getPackage().getName() + "." + mOpenForm, datas);
						}
					}
					else
					{
						if(mOpenForm != null)
						{
							if(mOpenForm.contains("."))
							{
								formHelper.open(mActivity, mOpenForm);
							}
							else
							{
								formHelper.open(mActivity, mActivity.getClass().getPackage().getName() + "." + mOpenForm);
							}
						}
						else
						{
							Toast.makeText(mActivity, "δָActivity", Toast.LENGTH_SHORT).show();
						}
					}
					if(mIsClosed)
					{
						mActivity.finish();
					}
				}
				catch(Exception ex)
				{
					Log.v("ButtonOpen", ex.getMessage());
				}
				Looper.loop();
			}
		}.start();
	}

	/*
	 *
	 */
	public String[] getFuncSigns()
	{
		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	public String getStateHiddenId()
	{
		return mStateHiddenId;
	}

	public String getWantedStateValue()
	{
		return mWantedStateValue;
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

	/*
	 *
	 */
	public void setVisible(Boolean arg0)
	{
		if(arg0)
		{
			this.setVisibility(View.VISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}

	public String getOpenForm()
	{
		return mOpenForm;
	}

	public void setOpenForm(String openForm)
	{
		this.mOpenForm = openForm;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public String[] getFuncSign()
	{
		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	public void setFuncSign(String funcSign)
	{
		this.mFuncSign = funcSign;
	}

	public OnClickingListener getOnClickingListener()
	{
		return onClickingListener;
	}

	public Boolean Closed()
	{
		return mIsClosed;
	}

	public void SetClosed(Boolean isClosed)
	{
		this.mIsClosed = isClosed;
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
