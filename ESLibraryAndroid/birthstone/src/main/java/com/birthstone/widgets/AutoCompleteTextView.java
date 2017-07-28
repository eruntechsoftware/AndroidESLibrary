package com.birthstone.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeHelper;
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


public class AutoCompleteTextView extends android.widget.AutoCompleteTextView implements ICollectible, IReleasable, IDataInitialize, IValidatible, ICellTitleStyleRequire {
    protected DataType mDataType;
    protected Activity mActivity;
    protected String mName;
    protected String mCollectSign;
    protected Boolean mIsRequired;
    protected Boolean mEmpty2Null = true;
    protected String mNameSpace = "http://schemas.android.com/res/eruntech.birthStone.widgets";
    protected String mTipText = "";

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            mCollectSign = attrs.getAttributeValue(mNameSpace, "collectSign");
            mIsRequired = attrs.getAttributeBooleanValue(mNameSpace, "isRequired", false);
            String dataType = attrs.getAttributeValue(mNameSpace, "dataType");
            if (dataType != null && dataType.length() > 0) {
                this.mDataType = DataTypeHelper.valueOf(dataType);
            } else {
                this.mDataType = com.birthstone.core.helper.DataType.String;
            }
        } catch (Exception ex) {
            Log.e("AutoCompleteTextView", ex.getMessage());
        }
    }

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
     *@param dataName 数据名称 
     *@param data 数据对象 
     **/
    public void release(String dataName, Data data) {
        if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null) {
            this.setText(data.getValue().toString());
        }
    }

    /** 
     * 数据类型校验，并返回是否成功 
     * @return 是否校验成功 
     * **/
    public Boolean dataValidator() {
        try {
            if (mIsRequired) {
                mTipText = ValidatorHelper.createRequiredValidator(getText().toString().trim());
            }
            invalidate();
            if (mTipText.length() != 0) {
                return false;
            }
        } catch (Exception ex) {
            Log.e("Validator", ex.getMessage());
        }
        return true;
    }

    /**
     * 数据收集，返回DataCollection
     **/
    public DataCollection collect() {
        DataCollection datas = new DataCollection();
        if (this.getText().equals("") && mEmpty2Null.equals(true)) {
            datas.add(new Data(this.mName, null, mDataType));
        } else {
            datas.add(new Data(mName, getText(), mDataType));
        }
        return datas;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // ַ
        canvas.drawText(mTipText, getTextSize(), this.getHeight() / 2 + 5, mPaint);
    }

    public void setDataType(DataType dataType) {
        this.mDataType = dataType;
    }

    public DataType getDataType() {
        return mDataType;
    }

    public Boolean getIsRequired() {
        return mIsRequired;
    }

    public void setIsRequired(Boolean arg0) {
        mIsRequired = arg0;
    }

    public String getName() {
        return mName;
    }

    public boolean getEmpty2Null() {
        return mEmpty2Null;
    }

    public void setEmpty2Null(boolean empty2Null) {
        this.mEmpty2Null = empty2Null;
    }

    public String getNameSpace() {
        return mNameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.mNameSpace = nameSpace;
    }

    public String getTipText() {
        return mTipText;
    }

    public void setTipText(String tipText) {
        this.mTipText = tipText;
    }

    public String[] getCollectSign() {
        return StringToArray.stringConvertArray(this.mCollectSign);
    }

    public void setCollectSign(String collectSign) {
        this.mCollectSign = collectSign;
    }

    public Object getActivity() {
        return mActivity;
    }

    public void setActivity(Object arg0) {
        if (arg0 instanceof Activity) {
            mActivity = (Activity) arg0;
        }
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setEmpty2Null(Boolean empty2Null) {
        this.mEmpty2Null = empty2Null;
    }

}
