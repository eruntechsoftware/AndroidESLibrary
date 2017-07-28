package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class HiddenFeild extends TextView
{
	public HiddenFeild( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		this.setVisibility(View.GONE);
	}

}
