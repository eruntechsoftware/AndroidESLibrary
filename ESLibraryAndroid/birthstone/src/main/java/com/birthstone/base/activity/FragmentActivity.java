package com.birthstone.base.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.birthstone.annotation.ViewInjectUtils;
import com.birthstone.base.event.OnReleasedListener;
import com.birthstone.base.event.OnReleaseingListener;
import com.birthstone.base.helper.ActivityManager;
import com.birthstone.base.helper.FormHelper;
import com.birthstone.base.helper.FragmentActivityManager;
import com.birthstone.base.helper.FragmentManager;
import com.birthstone.base.helper.ReleaseHelper;
import com.birthstone.base.helper.StatusBarUtil;
import com.birthstone.base.parse.CollectForm;
import com.birthstone.base.parse.ControlStateProtector;
import com.birthstone.base.parse.DataQueryForm;
import com.birthstone.base.parse.FunctionProtected;
import com.birthstone.base.parse.InitializeForm;
import com.birthstone.base.parse.ValidatorForm;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends android.support.v4.app.FragmentActivity implements IUINavigationBar
{
	/****/
	protected UINavigationBar mUINavigationBar;
	protected FragmentActivity mFragmentActivity;
	protected Fragment mFragment;
	protected Activity mParentActivity;
	protected Context mParentContext;
	
	protected ArrayList<View> views = new ArrayList<View>();
	protected ArrayList<Data> mTransferParams =   null;
	private DataCollection releaseParams,mReceiveDataParams, mTransferDataParams;
	protected String mTitle, mRightButtonText;
	protected Boolean mParentRefresh=false, mIsParentStart=false;
	protected int index = 0;
	protected int mReleaseCount = 0;
	
	private static List<String> FUNCTION_LIST  = new ArrayList<String>();
	public static int LEFT_IMAGE_RESOURCE_ID;
	private static float DENSITY;
	public static int RESULT_OK = 185324;
	public static int RESULT_CANCEL = 185816;
	
//	protected OnClickListener mLeftViewOnClickListener, mRightViewOnClickListener;
	
	
	public OnReleaseingListener onReleaseingListener;
	public OnReleasedListener onReleasedListener;

	public FragmentActivity( )
	{

	}

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState)
	{
		if(android.os.Build.VERSION.SDK_INT > 13)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll()

					.penaltyLog()
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// .detectLeakedSqlLiteObjects() //̽SQLiteݿ
					.penaltyLog()
					.penaltyDeath().build());
		}
		super.onCreate(savedInstanceState);
		ViewInjectUtils.inject(this);
		initalizeNavigationBar();
		//ܶ
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        DENSITY = metric.density;
       
		Intent intent = getIntent();
		if(intent != null)
		{
			String activityType = intent.getStringExtra("ActivityType");
			mTransferParams = (ArrayList<Data>) intent.getSerializableExtra("Parameter");
			
			if(activityType!=null && activityType.equals("Activity"))
			{
				if(ActivityManager.last() instanceof Activity){
					this.mParentActivity =  (Activity) ActivityManager.last();
				}
				this.mIsParentStart = true;
				if(mIsParentStart)
				{
					if(this.mReceiveDataParams == null)
					{
						this.mReceiveDataParams = new DataCollection();
					}
					this.mReceiveDataParams.addAll(this.mTransferParams);
				}
			}
			
			if(activityType!=null && activityType.equals("Fragment"))
			{
				android.support.v4.app.Fragment fragment = FragmentManager.last();
				if(fragment instanceof Fragment){
					this.mFragment =  (Fragment)fragment;
				}
				this.mIsParentStart = true;
				if(mIsParentStart)
				{
					if(this.mReceiveDataParams == null)
					{
						this.mReceiveDataParams = new DataCollection();
					}
					this.mReceiveDataParams.addAll(this.mTransferParams);
				}
			}
			
			if(activityType!=null && activityType.equals("FragmentActivity"))
			{
				android.support.v4.app.FragmentActivity fragmentActivity = FragmentActivityManager.last();
				if(fragmentActivity instanceof FragmentActivity){
					this.mFragmentActivity =  (FragmentActivity)fragmentActivity;
				}
				
				this.mIsParentStart = true;
				if(mIsParentStart)
				{
					if(this.mReceiveDataParams == null)
					{
						this.mReceiveDataParams = new DataCollection();
					}
					this.mReceiveDataParams.addAll(this.mTransferParams);
				}
			}
			
			if(activityType!=null && activityType.equals("Context"))
			{
				if(this.mReceiveDataParams == null)
				{
					this.mReceiveDataParams = new DataCollection();
				}
				this.mReceiveDataParams.addAll(this.mTransferParams);
			}
		}
		initView();
	}
	
	/**
	 * 设置内容视图资源ID
	 * @param layoutResID 资源ID
	 * **/
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
	}

	/**
	 *初始化UIView参数
	 * **/
	public void initView()
	{
		try
		{
			initializeActivity();
			release();
			query();
			setFunctionProtected();
			// setStateControl();
		}
		catch(Exception ex)
		{
			Log.e("getInitialize", ex.getMessage());
		}
	}

	/**
	 * 初始化NavigationBar
	 * **/
	public void initalizeNavigationBar()
	{
		View rootView = ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		if(rootView instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup)rootView;

			mUINavigationBar = new UINavigationBar(this, true);
			mUINavigationBar.UINavigationBarDelegat=this;
			viewGroup.addView(mUINavigationBar);
			
			StatusBarUtil.setTranslucent(this);

			StatusBarUtil.setColorNoTranslucent(this, UINavigationBar.BACKGROUND_COLOR);

			if(mRightButtonText != null)
			{
				mUINavigationBar.setRightText(mRightButtonText);
			}
			
			/****/
			if(mTitle != null)
			{
				mUINavigationBar.setTitle(mTitle);
			}
			mUINavigationBar.setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
			
		}
	}

	/*
	* 是否完成初始化
	* */
	public Boolean getInitialize()
	{
		try
		{
			initializeActivity();
			return true;
		}
		catch(Exception ex)
		{
			Log.e("getInitialize", ex.getMessage());
		}
		return false;
	}

	@Override
	public View findViewById(int id)
	{
		View view = super.findViewById(id);
		if(!views.contains(view))
		{
			views.add(view);
		}
		return view;
	}

	/*
	* 初始化Activity
	* */
	public void initializeActivity()
	{
		try
		{
			InitializeForm initializeForm = new InitializeForm((Activity) this.getBaseContext());
			initializeForm.initialize();
		}
		catch(Exception ex)
		{
			Log.v("InitializeForm", ex.getMessage());
		}
	}

	/*
	* 发布数据集到当前屏幕
	* */
	private void release()
	{
		ReleaseHelper releaseHelper;
		try
		{
			if(onReleaseingListener != null)
			{
				onReleaseingListener.onReleaseing();
			}
			
			if(mReceiveDataParams != null && mReceiveDataParams.size() > 0)
			{
				releaseHelper = new ReleaseHelper(mReceiveDataParams, this);
				releaseHelper.release(null);
			}

			if(onReleasedListener != null)
			{
				onReleasedListener.onReleased();
			}
		}
		catch(Exception ex)
		{
			Log.e("ݴݴ", ex.getMessage());
		}
	}

	/*
	*发布数据集到当前屏幕
	* @param params 数据集
	* */
	public void release(DataCollection params)
	{
		mReleaseCount=1;
		releaseParams = (DataCollection) params.clone(); 
		if(releaseParams != null && releaseParams.size() > 0)
		{
			ReleaseHelper releaseHelper;
			try
			{
				if(onReleaseingListener != null)
				{
					onReleaseingListener.onReleaseing();
				}
				releaseHelper = new ReleaseHelper(releaseParams, this);
				releaseHelper.release(null);
				if(onReleasedListener != null)
				{
					onReleasedListener.onReleased();
				}
				releaseParams.clear();
			}
			catch(Exception ex)
			{
				Log.e("ݴݴ", ex.getMessage());
			}
		}
	}
	
	/**
	 * ռָǵActivity
	 * **/
	public DataCollection collect(String collectSign)
	{
		CollectForm collecter = new CollectForm((Activity) this.getBaseContext(), collectSign);
		return collecter.collect();
	}

	/**
	 * öȨ
	 */
	public void setFunctionProtected()
	{
		try
		{
			FunctionProtected function = new FunctionProtected();
			function.setStateControl(this);
		}
		catch(Exception ex)
		{
			Log.e("Ȩ޿", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void query() throws Exception
	{
		DataQueryForm DataQueryForm;
		try
		{
			if(this != null)
			{
				DataQueryForm = new DataQueryForm((Activity) this.getBaseContext());
				DataQueryForm.query();
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	/**
	 * У鵱ǰǷϱʽ
	 * 
	 * @return ɹʧ
	 */
	public Boolean validator()
	{
		ValidatorForm validatorForm = new ValidatorForm((Activity) this.getBaseContext());
		try
		{
			return validatorForm.validator();
		}
		catch(Exception ex)
		{
			Log.e("validator", ex.getMessage());
		}
		return false;
	}

	/**
	 * Ȩַ
	 */
	@SuppressLint("DefaultLocale")
	public static void setFunction(String funStr)
	{
		try
		{
			String str = "";
			FUNCTION_LIST.clear();
			while(funStr.indexOf(",") > 0)
			{
				str = funStr.substring(0, funStr.indexOf(","));
				funStr = funStr.substring(funStr.indexOf(",") + 1);
				if(!str.equals("") && !FUNCTION_LIST.contains(str))
				{
					FUNCTION_LIST.add(str.trim().toLowerCase());
				}
			}
			if(funStr.length() > 0 && !FUNCTION_LIST.contains(funStr))
			{
				FUNCTION_LIST.add(funStr.trim().toLowerCase());
			}
		}
		catch(Exception ex)
		{
			Log.e("Ȩ޴", ex.getMessage());
		}
	}

	/**
	 * Ȩб
	 */
	public static List<String> getFunction()
	{
		return FUNCTION_LIST;
	}

	public void setStateControl()
	{
		ControlStateProtector.createControlStateProtector().setStateControl(this);
	}
	
	/**
	 * Ƿת򿪵ǰҳ
	 * **/
	public Boolean getIsParentStart()
	{
		return mIsParentStart;
	}

	/**
	 * ø
	 * 
	 * @parentForm
	 */
	public void setParentActivity(Activity parentActivity)
	{
		this.mParentActivity = parentActivity;
	}

	/*
	* 关闭当前屏幕
	* */
	public void finish()
	{
		Intent intent = new Intent();
		intent.putExtra("isRefresh", mParentRefresh);
		this.setResult(RESULT_OK, intent);
		intent=null;
		FragmentActivityManager.pop(this);
		super.finish();
	}
	
	/*
	* 关闭当前屏幕并传递参数
	* @param intent 参数集合
	* */
	public void finish(Intent intent)
	{
		if(intent==null)
		{
			intent = new Intent();
		}
		intent.putExtra("isRefresh", mParentRefresh);
		this.setResult(RESULT_OK, intent);
		intent=null;
		FragmentActivityManager.pop(this);
		super.finish();
	}

	
	/**
     * еActivityķֵ
     * requestCode:    ʾһActivityʱȥrequestCodeֵ
     * resultCodeʾActivityشֵʱresultCodeֵ
     * dataʾActivityشIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode)
		{
			case  185324:
				if(data.getBooleanExtra("isRefresh", false))
				{
					onRefresh(data);
				}
				break;
				default:
					break;
		}
    }
    
	/**
	 * ҳ淵غԶˢ´
	 * @param data ݵĲ
	 * **/
	public void onRefresh(Intent data)
	{
		
	}
    

	@Override
	public void onBackPressed()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				if(Looper.myLooper() == null)
				{
					Looper.prepare();
				}
				finish();
				Looper.loop();
			}
		}.start();

	}


	public ArrayList<View> getViews()
	{
		return views;
	}

	public void setViews(ArrayList<View> views)
	{
		this.views = views;
	}
	
	/**
	 * յĲ
	 * **/
	public DataCollection getReceiveDataParams()
	{
		return mReceiveDataParams;
	}

	/**
	 *设置当前Activity接收父级屏幕传递的参数集
	 * @param receiveDataParams 接收参数集合
	 * **/
	public void setReceiveDataParams(DataCollection receiveDataParams)
	{
		this.mReceiveDataParams = receiveDataParams;
	}
	
	
	/**
	 * ݵһĲ
	 * **/
	public DataCollection getTransferDataParams()
	{
		return mTransferDataParams;
	}
	
	/**
	 * ôݵһĲ
	 * @param transferDataParams ݲ
	 * **/
	public void setTransferDataParams(DataCollection transferDataParams)
	{
		this.mTransferDataParams = transferDataParams;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public static List<String> getFunctionList()
	{
		return FUNCTION_LIST;
	}

	public static void setFunctionList(List<String> functionList)
	{
		FragmentActivity.FUNCTION_LIST = functionList;
	}

	/**
	 * ǰʾ豸ܶ
	 * **/
	public static float getDensity()
	{
		return DENSITY;
	}

	/**
	 * ǰʾ豸ܶ
	 * **/
	public static void setDensity(float DENSITY)
	{
		FragmentActivity.DENSITY = DENSITY;
	}

	/**
	 * @  ҳִзǰ
	 */
	public OnReleaseingListener getOnReleaseingListener()
	{
		return onReleaseingListener;
	}

	/**
	 * @  ҳִзǰ
	 */
	public void setOnReleaseingListener(OnReleaseingListener onReleaseingListener)
	{
		this.onReleaseingListener = onReleaseingListener;
	}

	/**
	 * @  ҳִз󼤷
	 */
	public OnReleasedListener getOnReleasedListener()
	{
		return onReleasedListener;
	}
	
	//תdipΪpx
	public static int convertDIP2PX(Context context, int dip) 
	{ 
	    float scale = context.getResources().getDisplayMetrics().density; 
	    return (int)(dip*scale + 0.5f*(dip>=0?1:-1)); 
	} 
	 
	//תpxΪdip
	public static int convertPX2DIP(Context context, int px) 
	{ 
	    float scale = context.getResources().getDisplayMetrics().density; 
	    return (int)(px/scale + 0.5f*(px>=0?1:-1)); 
	}

	/**
	 * @  ҳִз󼤷
	 */
	public void setOnReleasedListener(OnReleasedListener onReleasedListener)
	{
		this.onReleasedListener = onReleasedListener;
	}

	/**
	 *获取导航栏
	 */
	public UINavigationBar getNavigationBar()
	{
		return mUINavigationBar;
	}

	/*
	* 设置导航栏背景色
	* @param color 背景色
	* */
	public void setUINavigationBarBackgroundColor(int color){
		if (mUINavigationBar!=null){
			mUINavigationBar.setBackgroundColor(color);
		}
		UINavigationBar.BACKGROUND_COLOR=color;
		StatusBarUtil.setTranslucent(this);
		//设置状态栏和标题栏颜色一致，实现沉浸式状态栏
		StatusBarUtil.setColorNoTranslucent(this, color);
	}

	/*
	* 设置导航栏背景色
	* @param color 背景色
	* @param isTranslucent 是否半透明状态栏
	* */
	public void setUINavigationBarBackgroundColor(int color, boolean isTranslucent){
		if (mUINavigationBar!=null){
			mUINavigationBar.setBackgroundColor(color);
		}
		UINavigationBar.BACKGROUND_COLOR=color;
		if (isTranslucent) {
			StatusBarUtil.setTranslucent(this);
			//设置状态栏和标题栏颜色一致，实现沉浸式状态栏
			StatusBarUtil.setColorNoTranslucent(this, color);
		}
	}

	/**
	 * 设置NavigationBar左侧按钮是否可见
	 * @param visible 设置可见性
	 * **/
	public void setUINavigationBarLeftButtonVisibility(int visible)
	{
		if (mUINavigationBar!=null){
			mUINavigationBar.setLeftButtonVisibility(visible);
		}

	}

	/**
	 * 设置NavigationBar右侧按钮是否可见
	 * @param visible 设置可见性
	 * **/
	public void setUINavigationBarRightButtonVisibility(int visible)
	{
		if(this.getNavigationBar()!=null){
			this.getNavigationBar().setRightButtonVisibility(visible);
		}
	}

	/**
	 * 设置NavigationBar是否可见
	 * @param visible 设置可见性
	 * **/
	public void setUINavigationBarVisibility(int visible)
	{
		if(this.getNavigationBar()!=null){
			this.getNavigationBar().setVisibility(visible);
		}
	}

	/**
	 *
	 * @param title
	 * **/
	public void setTitleText(String title)
	{
		this.mTitle = title;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setTitle(mTitle);
		}
	}

	/**
	 *设置左侧按钮图片
	 * @param resouceid 图片资源
	 * **/
	public void setLeftButtonImage(int resouceid)
	{
		LEFT_IMAGE_RESOURCE_ID=resouceid;
		Activity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		FragmentActivity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		if(getNavigationBar() != null)
		{
			this.getNavigationBar().setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
		}
	}

	/**
	 * 设置导航栏右侧按钮文本
	 * @param buttonText 按钮文本
	 * **/
	public void setRightText(String buttonText)
	{
		this.mRightButtonText = buttonText;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setRightText(buttonText);
		}
	}

	/**
	 * 左侧按钮单击事件
	 * **/
	public void onLeftClick(){

	}

	/**
	 * 右侧按钮单击事件
	 * **/
	public void onRightClick(){

	}

	/*
	* 设置状态栏颜色，实现沉浸式状态栏
	* @param color 颜色id
	* */
	public void setStatusBackgroundColor(int color)
	{
		StatusBarUtil.setTranslucent(this);

		StatusBarUtil.setColorNoTranslucent(this, color);
	}
	
	/**
	 * onRefresh
	 * 
	 * @param mParentRefresh
	 * **/
	public void setParentRefresh(boolean mParentRefresh)
	{
		this.mParentRefresh = mParentRefresh;
	}
	
	/**
	 * ͼ
	 * @param targetViewController ͼ
	 * @param navigationbar
	 * **/
	public void pushViewController(String targetViewController, DataCollection params, Boolean navigationbar)
	{
		try
		{
			FormHelper open = new FormHelper();
			open.open(this, targetViewController, params, true, navigationbar);
		}
		catch(Exception ex)
		{
			
		}
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(View view : views){
			if(view!=null && view.hasOnClickListeners())
        	{
        		view.setOnClickListener(null);
        	}
        	view = null;
        }
        views.clear();
        views=null;
        
        if(mTransferParams!=null){
        	mTransferParams.clear();
        	mTransferParams =   null;
        }
    	
        if(releaseParams!=null){
        	releaseParams.clear();
        	releaseParams =   null;
        }
    	
        if(mReceiveDataParams!=null){
        	mReceiveDataParams.clear();
        	mReceiveDataParams =   null;
        }
        
        if(mTransferDataParams!=null){
        	mTransferDataParams.clear();
        	mTransferDataParams =   null;
        }
        
//        if(mLeftViewOnClickListener!=null){
//        	mLeftViewOnClickListener=null;
//        }
//
//        if(mRightViewOnClickListener!=null){
//        	mRightViewOnClickListener=null;
//        }

        if(onReleaseingListener!=null){
        	onReleaseingListener=null;
        }
    	
        if(onReleasedListener!=null){
        	onReleasedListener=null;
        }
        
        if(mUINavigationBar!=null){
        	mUINavigationBar.setRightViewClickListener(null);
        	mUINavigationBar.setLeftViewClickListener(null);
        }
        mUINavigationBar=null;
    	mFragmentActivity=null;
    	mFragment=null;
    	mParentActivity=null;
    	mParentContext=null;
    }
}
