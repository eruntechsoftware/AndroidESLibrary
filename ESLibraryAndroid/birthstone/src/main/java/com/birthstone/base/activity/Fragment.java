package com.birthstone.base.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.birthstone.annotation.ViewInjectUtils;
import com.birthstone.base.event.OnReleasedListener;
import com.birthstone.base.event.OnReleaseingListener;
import com.birthstone.base.helper.FormHelper;
import com.birthstone.base.helper.FragmentManager;
import com.birthstone.base.helper.StatusBarUtil;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.ArrayList;

public class Fragment extends android.support.v4.app.Fragment implements IUINavigationBar
{
	protected UINavigationBar mUINavigationBar;
	private Fragment mFragment;
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private static Dialog mPromptDialog;
	private Bundle mSavedInstanceState;
	private View mView;
	private DataCollection dataParams = new DataCollection();
	public ArrayList<View> views = new ArrayList<View>();
	protected ArrayList<Data> mTransferParams =   null;
	private DataCollection releaseParams,mReceiveDataParams, mTransferDataParams;
	protected int mReleaseCount = 0;
	public OnReleaseingListener onReleaseingListener;
	public OnReleasedListener onReleasedListener;
	public static int LEFT_IMAGE_RESOURCE_ID;
	protected String mTitle, mRightButtonText;
	protected Boolean mParentRefresh=false, mIsParentStart=true;
//	protected OnClickListener mLeftViewOnClickListener, mRightViewOnClickListener;
	
	public static int RESULT_OK = 185324;
	public static int RESULT_CANCEL = 185816;
	
	public Fragment( )
	{

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		this.mContainer = container;
		this.mSavedInstanceState = savedInstanceState;
		ViewInjectUtils.inject(this);
		return mView;
	}

	/**
	 * 设置内容视图资源ID
	 * @param resID 资源ID
	 * **/
	public void setCreateView(int resID)
	{
		mView = mInflater.inflate(resID,mContainer, false);
		initalizeNavigationBar();

		if(mSavedInstanceState != null)
		{
			String activityType = mSavedInstanceState.getString("ActivityType");
			mTransferParams = (ArrayList<Data>) mSavedInstanceState.getSerializable("Parameter");

			if(activityType!=null)
			{
				android.support.v4.app.Fragment fragment = FragmentManager.last();
				if(fragment instanceof Fragment){
					this.mFragment =  (Fragment)fragment;
				}

				if(mFragment!=null && mFragment.getTransferDataParams() != null)
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
	}
	
	/**
	 *
	 * **/
	public void initalizeNavigationBar()
	{
		if(mView instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup)mView;
			mUINavigationBar = new UINavigationBar(this.getActivity(), true);
			mUINavigationBar.UINavigationBarDelegat=this;
			viewGroup.addView(mUINavigationBar);

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
	
	/**
	 * ǰView
	 * **/
	public View getRootView()
	{
		return mView;
	}
	
	/***
	 * ǰ
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.getClass().getName();
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
	 *
	 * @param transferDataParams
	 * **/
	public void setTransferDataParams(DataCollection transferDataParams)
	{
		this.mTransferDataParams = transferDataParams;
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

	/**
	 * @  ҳִз󼤷
	 */
	public void setOnReleasedListener(OnReleasedListener onReleasedListener)
	{
		this.onReleasedListener = onReleasedListener;
	}


	
	/**
	 * Ƿת򿪵ǰҳ
	 * **/
	public Boolean getIsParentStart()
	{
		return mIsParentStart;
	}

	/**
	 *获取导航栏
	 */
	public UINavigationBar getNavigationBar()
	{
		return mUINavigationBar;
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
		StatusBarUtil.setTranslucent(this.getActivity());

		StatusBarUtil.setColorNoTranslucent(this.getActivity(), color);
	}
	
	/**
	 * öбΪʱʾ
	 * @param promptDialog ʾ
	 * **/
	public static void  setPromptDialog(Dialog promptDialog)
	{
		mPromptDialog = promptDialog;
	}
	
	/**
	 * бΪʱʾ
	 * **/
	public static Dialog  getPromptDialog()
	{
		return mPromptDialog;
	}
	
	/**
	 * øҳǷִˢonRefresh
	 * 
	 * @param mParentRefresh Ƿ
	 * **/
	public void setParentRefresh(boolean mParentRefresh)
	{
		this.mParentRefresh = mParentRefresh;
	}
	
	/**
	 * ͼ
	 * @param targetViewController ͼ
	 * @param params ͼĲ
	 * @param navigationbar ǷĬ
	 * **/
	public void pushViewController(String targetViewController, DataCollection params, Boolean navigationbar)
	{
		try
		{
			FormHelper open = new FormHelper();
			if(params  == null)
			{
				params = new DataCollection();
			}
			open.open(this, targetViewController, params, true, navigationbar);
		}
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * еActivityķֵ requestCode:
	 * ʾһActivityʱȥrequestCodeֵ
	 * resultCodeʾActivityشֵʱresultCodeֵ
	 * dataʾActivityشIntent
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
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
	 *  @param data ݵĲ
	 * **/
	public void onRefresh(Intent data)
	{
		
	}
	
	/**
	 * ʾ󵯳
	 * **/
	public void showDialog()
	{
		if(mPromptDialog != null)
		{
			mPromptDialog.show();
		}
	}
	
	@Override
	public void onDestroy() {
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
    	mFragment=null;
    	
    }
}
