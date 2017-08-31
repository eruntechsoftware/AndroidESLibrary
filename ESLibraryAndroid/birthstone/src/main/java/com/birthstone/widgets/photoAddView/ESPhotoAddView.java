package com.birthstone.widgets.photoAddView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.widgets.ESActionSheet;
import com.birthstone.widgets.ESGridView;
import com.birthstone.widgets.photoView.BitmapCollection;
import com.birthstone.widgets.photoView.ESPhotoView;
import com.linchaolong.android.imagepicker.ImagePicker;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片上传组件
 */
public class ESPhotoAddView extends ESGridView implements AdapterView.OnItemClickListener, IDataInitialize, ESActionSheet.OnActionSheetClickListener{

    /**Web图片计数**/
    private static int WEB_IMAGE_COUNT;

    private Activity activity;
    private ESActionSheet actionSheetPhoto;
    private String bitmapCachePath;
    private ESPhotoAddViewAdapter adapter;
    private ImagePicker imagePicker;
    //照相机拍照得到的照片
    private File mCurrentPhotoFile;
    /**上传文件的列表**/
    public BitmapCollection bitmapCollection;

    /**图片路径容器，之存储图片路径**/
    public static List<String> IMAGE_PATH_LIST = new LinkedList<String>();

    //声明常量权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int TAKE_PICTURE = 0;


    public ESPhotoAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESPhotoAddView);
        bitmapCachePath = a.getString(R.styleable.ESPhotoAddView_bitmapCachePath);
        bitmapCollection = new BitmapCollection();
    }

    public void setAdapter(ESPhotoAddViewAdapter adapter){
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 虚拟键盘隐藏 判断view是否为空
        View v= activity.getWindow().peekDecorView();
        if (v != null) {
            //隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(position == IMAGE_PATH_LIST.size()){
            //设计显示底部弹出框
            String [] btnItems = {"拍照", "相册选择", "取消"};
            actionSheetPhoto = new ESActionSheet(activity, this, btnItems);
            actionSheetPhoto.setOnActionSheetClickListener(this);
            actionSheetPhoto.show();
        }else{
            Intent intent = new Intent();
            intent.putExtra("index",position);
            intent.putExtra("bitmapList",bitmapCollection);
            intent.setClass(activity,ESPhotoView.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public Object getActivity() {
        return activity;
    }

    @Override
    public void setActivity(Object obj) {
        if(obj instanceof Activity) {
            activity = (Activity) obj;
        }
    }

    @Override
    public void dataInitialize() {
        //初始化imagePicker
        imagePicker = new ImagePicker();
        imagePicker.setTitle("选择图片");

        if (bitmapCachePath==null || "".equals(bitmapCachePath)){
            if(!com.birthstone.core.helper.File.getSDCardPath().equals("")){
                bitmapCachePath = com.birthstone.core.helper.File.getSDCardPath()+"eruntechcache//bitmap//";
            }else{
                ToastHelper.toastShow(activity,R.string.sd_card);
            }
        }
    }

    /**
     * 获取每个adapter的进度条
     * **/
    public ProgressBar[] getProgressBarArray(){
        int size = bitmapCollection.getFileList().length;
        ProgressBar[] newProgressbars = new ProgressBar[size];

        for(int p = 0;p<size; p++){
            newProgressbars[p] = this.adapter.progressBarArray[p+WEB_IMAGE_COUNT];
            newProgressbars[p].setVisibility(View.VISIBLE);
        }
        return newProgressbars;
    }



    /**
     * 权限注册
     * */
    private void requestPermission(){
        // 是否添加权限
        int mPermisson = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (mPermisson != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onClick(View view) {
        requestPermission();
        switch (view.getId()){
            case 0:
                //从相册中选取图片
                imagePicker.startGallery(activity,new ImagePicker.Callback(){
                    /**
                     * 图片选择回调
                     * @param imageUri
                     */
                    public void onPickImage(Uri imageUri){
                        bitmapCollection.add(imageUri);
                    }
                });
                break;
            case 1:
//                imagePicker.startCamera();
                break;
        }
    }


}
