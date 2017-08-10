package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickedListener;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.core.parse.Data;

import java.util.LinkedList;


@SuppressLint("DefaultLocale")
public class ESButton extends android.widget.Button implements IFunctionProtected, IReleasable, IStateProtected, IDataInitialize {

    public String mFuncSign;
    protected String mSign;
    public String mStateHiddenId;
    public String mWantedStateValue;
    public String mName;
    public Activity mActivity;
    public Boolean mCancel = false;
    public OnClickingListener onClickingListener;
    public OnClickedListener onClickedListener;
    public String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

    public ESButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            setOnClickListener(clickListener);
            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.eruntech);
            mFuncSign = a.getString(R.styleable.eruntech_funcSign);
            mSign = a.getString(R.styleable.eruntech_sign);
            mStateHiddenId = a.getString(R.styleable.eruntech_stateHiddenId);
            mWantedStateValue = a.getString(R.styleable.eruntech_wantedStateValue);
            a.recycle();
        }catch (Exception ex){
            Log.e(ESButton.this.toString(),ex.getMessage());
        }
    }

    public ESButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void click() {
    }

    protected OnClickListener clickListener = new OnClickListener() {
        public Boolean onClicking() {
            if (onClickingListener != null) {
                return onClickingListener.onClicking();
            }
            return false;
        }

        public void onClick(View v) {
            if (onClicking()) {
                return;
            }
            click();
            onClicked();
        }

        public void onClicked() {
            if (onClickedListener != null) {
                onClickedListener.onClicked();
            }
        }
    };

    public void dataInitialize() {
        if (mActivity != null) {
            String classnameString = mActivity.getPackageName() + ".R$id";
            mName = InitializeHelper.getName(classnameString, getId());
        }
    }

    public LinkedList<String> getRequest() {
        LinkedList<String> list = new LinkedList<String>();
        list.add(mName);
        return list;
    }

    /** 
     * 发布数据到UIView 
     *  @param dataName 数据名称 
     *  @param data 数据对象 
     * * **/
    public void release(String dataName, Data data) {
        if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null) {
            if (data.getValue() == null) {
                this.setText("");
                return;
            } else {
                this.setText(data.getValue().toString());
            }
        }
    }

    public String getName() {
        return mName;
    }

    public Object getActivity() {
        return mActivity;
    }

    public void setActivity(Object arg0) {
        if (arg0 instanceof Activity) {
            mActivity = (Activity) arg0;
        }
    }

    public Boolean getCancel() {
        return mCancel;
    }

    public void setCancel(Boolean cancel) {
        this.mCancel = cancel;
    }

    public void protectState(Boolean arg0) {
        if (arg0) {
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.GONE);
        }
    }

    public String getStateHiddenId() {
        return mStateHiddenId;
    }

    public String getWantedStateValue() {
        return mWantedStateValue;
    }

    public OnClickingListener getOnClickingListener() {
        return onClickingListener;
    }

    public void setOnClickingListener(OnClickingListener onClickingListener) {
        this.onClickingListener = onClickingListener;
    }

    public OnClickedListener getOnClickedListener() {
        return onClickedListener;
    }

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    /*
     * ö
     */
    public void setVisible(Boolean arg0) {
        if (arg0) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }

    public void setStateHiddenId(String stateHiddenId) {
        this.mStateHiddenId = stateHiddenId;
    }

    public void setWantedStateValue(String wantedStateValue) {
        this.mWantedStateValue = wantedStateValue;
    }

    /*
     * Ȩַ
     */
    public String[] getFuncSign() {
        return StringToArray.stringConvertArray(this.mFuncSign);
    }

    public void setFuncSign(String funcSign) {
        this.mFuncSign = funcSign;
    }

    public String getNameSpace() {
        return mNameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.mNameSpace = nameSpace;
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

}