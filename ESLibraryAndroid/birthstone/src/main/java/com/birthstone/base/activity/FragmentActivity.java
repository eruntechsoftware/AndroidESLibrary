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
	public DataCollection dataParams = new DataCollection();
	protected ArrayList<Data> mTransferParams =   null;
	private DataCollection releaseParams,mReceiveDataParams, mTransferDataParams;
	protected String mTitle, mRightButtonText;
	protected Boolean mShowBtnBack=true, mShowNavigationbar, mParentRefresh=false, mIsParentStart=false;
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
		
		//ܶ
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        DENSITY = metric.density;
       
		Intent intent = getIntent();
		if(intent != null)
		{
			String activityType = intent.getStringExtra("ActivityType");
			mTransferParams = (ArrayList<Data>) intent.getSerializableExtra("Parameter");
			mShowNavigationbar =  intent.getBooleanExtra("Navigationbar", false);
			mShowBtnBack = intent.getBooleanExtra("ShowBtnBack", false);
			
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
					this.dataParams = this.mReceiveDataParams;
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
					this.dataParams = this.mReceiveDataParams;
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
					this.dataParams = this.mReceiveDataParams;
				}
			}
			
			if(activityType!=null && activityType.equals("Context"))
			{
				if(this.mReceiveDataParams == null)
				{
					this.mReceiveDataParams = new DataCollection();
				}
				this.mReceiveDataParams.addAll(this.mTransferParams);
				this.dataParams = this.mReceiveDataParams;
			}
		}
		
	}
	
	/**
	 * òԴ
	 * @param layoutResID Դid
	 * **/
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		if(mShowNavigationbar)
		{
			initalizeNavigationBar();
		}
	}

	/**
	 * ͼ
	 * **/
	public void onCreateView()
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
	 *
	 * **/
	public void initalizeNavigationBar()
	{
		View rootView = ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		if(rootView instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup)rootView;
			//ص
			mUINavigationBar = new UINavigationBar(this, mShowBtnBack);
			mUINavigationBar.UINavigationBarDelegat=this;
			viewGroup.addView(mUINavigationBar);
			
			StatusBarUtil.setTranslucent(this);

			StatusBarUtil.setColorNoTranslucent(this, UINavigationBar.BACKGROUND_COLOR);
			/**Ҳఴı**/
			if(mRightButtonText != null)
			{
				mUINavigationBar.setRightText(mRightButtonText);
			}
			
			/****/
			if(mTitle != null)
			{
				mUINavigationBar.setTitle(mTitle);
			}
			/**ఴͼƬԴ**/
			mUINavigationBar.setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
			
		}
	}

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

	public void release()
	{
		ReleaseHelper releaseHelper;
		try
		{
			if(onReleaseingListener != null)
			{
				onReleaseingListener.onReleaseing();
			}
			
			if(dataParams != null && dataParams.size() > 0)
			{
				releaseHelper = new ReleaseHelper(dataParams, this);
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

	/**
	 * ݵActivity
	 * params:ݼ
	 * **/
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

	/**
	 * رյǰҳ
	 * **/
	public void finish()
	{
		Intent intent = new Intent();
		intent.putExtra("isRefresh", mParentRefresh);
		this.setResult(RESULT_OK, intent);
		intent=null;
		super.finish();
		FragmentActivityManager.pop(this);
	}
	
	/**
	 * رյǰҳ
	 * @param intent رպ͵Ĳ
	 * **/
	public void finish(Intent intent)
	{
		if(intent==null)
		{
			intent = new Intent();
		}
		intent.putExtra("isRefresh", mParentRefresh);
		this.setResult(RESULT_OK, intent);
		intent=null;
		super.finish();
		FragmentActivityManager.pop(this);
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
	@Deprecated
	public DataCollection getDataParams()
	{
		return dataParams;
	}

	/**
	 * õǰҪݵĲ
	 * dataParams
	 * **/
	@Deprecated 
	public void setDataParams(DataCollection dataParams)
	{
		this.dataParams = dataParams;
	}
	
	/**
	 * յĲ
	 * **/
	public DataCollection getReceiveDataParams()
	{
		return mReceiveDataParams;
	}

	/**
	 * õǰҪյĲ
	 * dataParams
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
	 *
	 */
	public UINavigationBar getNavigationBar()
	{
		return mUINavigationBar;
	}
	
	/**

	 * @param color ֵ
	 * **/
	public void setStatusBackgroundColor(int color)
	{
		StatusBarUtil.setTranslucent(this);

		StatusBarUtil.setColorNoTranslucent(this, color);
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
	 * Ҳ಼ʾ״̬
	 * @param visiility ʾ״̬
	 * **/
	public void setRightButtonVisibility(int visiility)
	{
		if(this.getNavigationBar()!=null){
			this.getNavigationBar().setRightButtonVisibility(visiility);
		}
	}
	
	/**
	 * ಼ʾ״̬
	 * @param visiility ʾ״̬
	 * **/
	public void setLeftButtonVisibility(int visiility)
	{
		if(this.getNavigationBar()!=null){
			this.getNavigationBar().setLeftButtonVisibility(visiility);
		}
	}
	
	/**
	 *
	 * @param resouceid ͼƬԴ
	 * **/
	public void setLeftButtonImage(int resouceid)
	{
		LEFT_IMAGE_RESOURCE_ID=resouceid;
		Activity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		FragmentActivity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
		}
	}
	
	/**
	 * Ҳ
	 * 
	 * @param resouceid ͼƬԴ
	 * **/
	public void setRightButtonImage(int resouceid)
	{
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setRightButtonImage(resouceid);
		}
	}
	
	/**
	 * Ҳఴʾı
	 * @param buttonText ı
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
		try{
			FragmentActivity.this.finish();
		}catch (Exception ex){
			Log.e("leftClick",ex.getMessage());
		}
	}

	/**
	 * 右侧按钮单击事件
	 * **/
	public void onRightClick(){

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
