package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataType;

/**
 * 密码输入文本框
 */
public class ESTextBoxPassword extends ESTextBox {
    public ESTextBoxPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsRequired = true;
        this.setInputTypeWithDataType(DataType.Password.ordinal());
    }
}
