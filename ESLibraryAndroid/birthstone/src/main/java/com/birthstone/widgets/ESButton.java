package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

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
public abstract class ESButton extends android.widget.Button implements IFunctionProtected, IReleasable, IStateProtected, IDataInitialize {

    public String mFuncSign;
    public String mSign;
    public String mSql;
    public String mMessage;
    public String mConfirmMessage;
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
        mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
        mMessage = attrs.getAttributeValue(mNameSpace, "message");
        mSign = attrs.getAttributeValue(mNameSpace, "sign");
        mSql = attrs.getAttributeValue(mNameSpace, "sql");
        mConfirmMessage = attrs.getAttributeValue(mNameSpace, "confirmMessage");
        mStateHiddenId = attrs.getAttributeValue(mNameSpace, "stateHiddenId");
        mWantedStateValue = attrs.getAttributeValue(mNameSpace, "wantedStateValue");
        this.setOnClickListener(clickListener);
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


    public void setSql(String sql) {
        this.mSql = sql;
    }

    public String getSql() {
        return mSql;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.mConfirmMessage = confirmMessage;
    }

    public String getConfirmMessage() {
        return mConfirmMessage;
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

    public String[] getSigns() {
        return StringToArray.stringConvertArray(this.mSign);
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

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        this.mSign = sign;
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
