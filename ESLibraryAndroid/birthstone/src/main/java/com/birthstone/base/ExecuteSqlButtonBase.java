package com.birthstone.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;

import com.birthstone.base.activity.event.OnClickedListener;
import com.birthstone.base.activity.event.OnClickingListener;
import com.birthstone.base.activity.helper.MessageBox;
import com.birthstone.base.activity.parse.CollectForm;
import com.birthstone.base.activity.parse.ReleaseForm;
import com.birthstone.base.activity.parse.ValidatorForm;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IReleaser;


public abstract class ExecuteSqlButtonBase extends Button
{

	private SQLiteDatabase mSqlDb;
	protected CollectForm mCollForm;
	protected ReleaseForm mReleaseForm;
	protected ValidatorForm mValidatorForm;

	public ExecuteSqlButtonBase( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		mFuncSign = attrs.getAttributeValue(mNameSpace, "funcSign");
		mMessage = attrs.getAttributeValue(mNameSpace, "message");
		mSign = attrs.getAttributeValue(mNameSpace, "sign");
		mSql = attrs.getAttributeValue(mNameSpace, "sql");
		mConfirmMessage = attrs.getAttributeValue(mNameSpace, "confirmMessage");
		mStateHiddenId = attrs.getAttributeValue(mNameSpace, "stateHiddenId");
		mWantedStateValue = attrs.getAttributeValue(mNameSpace, "wantedStateValue");
		mSqlDb = new SQLiteDatabase(super.getContext());
	}

	public void setOnClickingListener(OnClickingListener onClickingListener)
	{
		super.onClickingListener = onClickingListener;
	}

	public void setOnClickedListener(OnClickedListener onClickedListener)
	{
		super.onClickedListener = onClickedListener;
	}

	private void execute()
	{
		try
		{
			if(super.mActivity.validator())
			{
				mCollForm = new CollectForm(super.mActivity, mSign);
				mReleaseForm = new ReleaseForm(super.mActivity);
				mSqlDb.setSql(mSql);
				mSqlDb.getCollectors().add((ICollector) mCollForm);
				mSqlDb.getReleasers().add((IReleaser) mReleaseForm);
				mSqlDb.executeTable();
			}
		}
		catch(Exception ex)
		{
			MessageBox.showMessage(mActivity, "Ϣ", ex.getMessage());
		}
	}

	/**
	 * SqlǷΪջnull
	 * 
	 * @return
	 */
	private Boolean checkSqlIsNull()
	{
		if(mSql != null && mSql.trim().length() > 0)
		{
			Log.v("CheckSql", "false");
			return false;
		}
		Log.v("CheckSql", "true");
		return true;
	}

	public void click()
	{
		try
		{
			if(checkSqlIsNull())
			{
				MessageBox.showMessage(mActivity, "", "ִеSql");
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			mValidatorForm = new ValidatorForm(super.mActivity);
			if(mValidatorForm.validator())
			{
				if(mConfirmMessage != null && !mConfirmMessage.trim().equals(""))
				{
					builder.setTitle("ʾ");
					builder.setMessage(mConfirmMessage);
					builder.setNegativeButton("(C)", new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
						}
					});

					builder.setPositiveButton("ȷ(O)", new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							try
							{
								execute();
								if(mMessage != null && !mMessage.trim().equals(""))
								{
									MessageBox.showMessage(mActivity, "ʾ", mMessage);
								}
							}
							catch(Exception ex)
							{
								Log.v("Execute", ex.getMessage());
							}
						}
					}).show();
				}
				else
				{
					execute();
					if(mMessage != null && !mMessage.trim().equals(""))
					{
						MessageBox.showMessage(mActivity, "ʾ", mMessage);
					}
				}
			}
			else
			{
				MessageBox.showMessage(mActivity, "", "ʹ룡");
			}
		}
		catch(Exception ex)
		{
			MessageBox.showMessage(mActivity, "", ex.getMessage());
		}
	}

	public CollectForm getCollForm()
	{
		return mCollForm;
	}

	public void setCollForm(CollectForm collForm)
	{
		this.mCollForm = collForm;
	}

	public ReleaseForm getReleaseForm()
	{
		return mReleaseForm;
	}

	public void setReleaseForm(ReleaseForm releaseForm)
	{
		this.mReleaseForm = releaseForm;
	}

	public ValidatorForm getValidatorForm()
	{
		return mValidatorForm;
	}

	public void setValidatorForm(ValidatorForm validatorForm)
	{
		this.mValidatorForm = validatorForm;
	}

}
