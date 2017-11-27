package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 隐藏字段控件，用于非可见的字段绑定
 * */
public class ESHiddenFeild extends ESTextBox
{
	public ESHiddenFeild(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		this.setVisibility(View.GONE);
	}

}
