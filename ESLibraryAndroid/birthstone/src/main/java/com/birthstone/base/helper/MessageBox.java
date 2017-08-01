package com.birthstone.base.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MessageBox
{

	public static boolean showMessage(Activity from, String caption, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setTitle(caption);
		builder.setMessage(message);
		builder.setPositiveButton("ȷ", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{

			}
		}).show();
		return true;
	}
}
