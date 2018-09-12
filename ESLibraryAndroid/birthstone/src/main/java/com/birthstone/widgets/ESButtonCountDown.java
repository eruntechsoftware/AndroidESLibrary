package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.widgets.ESButton;
import com.birthstone.widgets.ESMessageBox;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * 验证码倒计时按钮
 * **/
public class ESButtonCountDown extends ESButton implements OnClickingListener
{
	protected String mMessage;
	public int T = 30; //倒计时时长
	private Handler mHandler = new Handler();

	public ESButtonCountDown(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		try
		{
			this.setOnClickingListener(this);
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESButtonCountDown);
			mMessage = a.getString(R.styleable.ESButtonCountDown_message);
		}
		catch(Exception ex)
		{
			Log.e("ESButtonCountDown",ex.getMessage());
		}
	}

	@Override
	public Boolean onClicking()
	{
		ESButtonCountDown.this.setEnabled(false);
		if(mSign==null && mSign.equals(""))
		{
			ToastHelper.toastShow(ESButtonCountDown.this.getContext(),"请设置按钮采集属性（sign）");
			this.setEnabled(true);
			return true;
		}
		else
		{
			Activity activity = (Activity) this.getActivity();
			DataCollection params = activity.collect(mSign);
			if(params==null || params.size()==0)
			{
				new ESMessageBox(ESButtonCountDown.this.getContext(),"验证码","请设置验证码文本框采集属性（collectSign）").show();
				ESButtonCountDown.this.setEnabled(true);
				return true;
			}
			else
			{
				if(params.getFirst().getStringValue().trim().equals(""))
				{
					new ESMessageBox(ESButtonCountDown.this.getContext(),"验证码","手机号、身份证号不能为空").show();
					ESButtonCountDown.this.setEnabled(true);
					return true;
				}
				else
				{
					//开始执行倒计时
					new Thread(new MyCountDownTimer()).start();
				}
			}
		}
		return false;
	}


	/**
	 * 自定义倒计时类，实现Runnable接口
	 */
	class MyCountDownTimer implements Runnable
	{

		@Override
		public void run()
		{

			//倒计时开始，循环
			while(T > 0)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						ESButtonCountDown.this.setEnabled(false);
						ESButtonCountDown.this.setClickable(false);
						ESButtonCountDown.this.setText(T + "秒后重新获取");
					}
				});
				try
				{
					Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				T--;
			}

			//倒计时结束，也就是循环结束
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					ESButtonCountDown.this.setEnabled(true);
					ESButtonCountDown.this.setClickable(true);
					ESButtonCountDown.this.setText("获取验证码");
				}
			});
			T = 30; //最后再恢复倒计时时长
		}
	}

}