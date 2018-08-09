package com.birthstone.widgets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.birthstone.base.activity.Activity;
import com.birthstone.core.helper.UUIDGenerator;

import java.io.File;

/**
 * 图片选择器
 **/
public class ESPhotoPicker extends ESImageView implements OnClickListener, ESActionSheet.OnActionSheetClickListener, PreferenceManager.OnActivityResultListener
{

	private ESActionSheet actionSheetPhoto;
	private Activity activity;
	/**
	 * 创建一个以当前时间为名称的文件
	 **/
	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp//" + UUIDGenerator.generate() + ".jpg");

	private final String TAG = "ESPhotoPicker";
	//声明常量权限
	private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

	/**
	 * 声明私有变量
	 **/
	public static final int PHOTO_TAKEPHOTO = 1;//拍照
	public static final int PHOTO_PICKPHOTO = 2;//从相册选择
	public static final int PHOTO_RESULT = 3;//结果

	public ESPhotoPicker(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		try
		{
			this.setOnClickListener(this);
			activity = (Activity) getActivity();
			activity.setOnActivityResultListener(this);
			String[] btnItems = {"拍照", "相册选择", "取消"};
			actionSheetPhoto = new ESActionSheet((android.app.Activity) getActivity(), this, btnItems);
			actionSheetPhoto.setOnActionSheetClickListener(this);
		}
		catch(Exception ex)
		{

		}
	}


	@SuppressLint("ResourceType")
	@Override
	public void onClick(View view)
	{
		if (view.getId() == getId())
		{
			requestPermission();
			if (!actionSheetPhoto.isShowing())
			{
				actionSheetPhoto.show();
			}
		}
		if (view.getId() == 0)
		{
			//调用系统拍照功能
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//指定调用相机拍照后照片的存储路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaStorageDir));
			activity.startActivityForResult(intent, PHOTO_TAKEPHOTO);
		}
		if (view.getId() == 1)
		{
			//调用系统相册
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			activity.startActivityForResult(intent1, PHOTO_PICKPHOTO);
		}
	}

	/**
	 * 权限注册
	 */
	private void requestPermission()
	{
		if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
			Log.i(TAG, "需要授权 ");
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{
				Log.i(TAG, "拒绝过了");
				Toast.makeText(activity, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Log.i(TAG, "进行授权");
				ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
			}
		}
	}

	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			//当选择拍照时调用
			case PHOTO_TAKEPHOTO:
				ESPhotoPicker.this.setImageURI(mediaStorageDir.getPath());
				break;
			//当选择从本地获取照片时调用
			case PHOTO_PICKPHOTO:
				//做非空判断，当不满意或想重新裁剪时不会包异常，下同
				ESPhotoPicker.this.setImageURI(mediaStorageDir.getPath());
				break;
			//返回的结果
			case PHOTO_RESULT:
				if (data != null)
				{
					Bundle bundle = data.getExtras();
					if (bundle != null)
					{
						Bitmap photo = bundle.getParcelable("data");
						//上传头像
						ESPhotoPicker.this.setImageBitmap(photo);
					}
				}
				break;
		}

		return true;
	}
}
