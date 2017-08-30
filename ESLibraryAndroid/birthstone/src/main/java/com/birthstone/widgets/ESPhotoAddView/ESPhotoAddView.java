package com.birthstone.widgets.ESPhotoAddView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.helper.UUIDGenerator;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.widgets.ESActionSheet;
import com.birthstone.widgets.ESGridView;
import com.birthstone.widgets.ESPhoto.ESPhotoBucket;

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
    //照相机拍照得到的照片
    private File mCurrentPhotoFile;

    /**上传文件的列表**/
    public List<File> updateFileList;

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
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
                    0);
        }

        if(position == IMAGE_PATH_LIST.size()){
            //设计显示底部弹出框
            String [] btnItems = {"拍照", "相册选择", "取消"};
            actionSheetPhoto = new ESActionSheet(activity, this, btnItems);
            actionSheetPhoto.setOnActionSheetClickListener(this);
            actionSheetPhoto.show();
        }else{
//            DataCollection params = new DataCollection();
//            params.add(new Data("index",position));
//            activity.pushViewController(ESPhotoView.class.getName(), params, false);
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
        int size = getFileList().length;
        ProgressBar[] newProgressbars = new ProgressBar[size];

        for(int p = 0;p<size; p++){
            newProgressbars[p] = this.adapter.progressBarArray[p+WEB_IMAGE_COUNT];
            newProgressbars[p].setVisibility(View.VISIBLE);
        }
        return newProgressbars;
    }

    /**
     * 获取上传文件的列表
     * **/
    private File[] getFileList(){
        updateFileList.clear();
        //循环检查本地图片数量并统计web图片数
        for (String path:IMAGE_PATH_LIST){
            if(ValidatorHelper.isMached(DataTypeExpression.filePath(),path)){
                updateFileList.add(new File(path));
            }else{
                WEB_IMAGE_COUNT++;
            }
        }
        return (File[]) updateFileList.toArray();
    }

    /**
     * 时间：2015年09月06日
     * 作者：张景瑞
     * 功能：拍照获取照片
     */
    public void doTakePhoto(){
        // 给新照的照片文件命名
        mCurrentPhotoFile = new File(bitmapCachePath, UUIDGenerator.generate()+".jpg");
        final Intent intent = getTakePickIntent(mCurrentPhotoFile);
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
            Uri imageUri = Uri.fromFile(mCurrentPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            activity.startActivityForResult(intent, TAKE_PICTURE);
        }else {
            ToastHelper.toastShow(activity,R.string.sd_card);
        }

    }

    public Intent getTakePickIntent(File f){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE",null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
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
                doTakePhoto();
                break;
            case 1:
                DataCollection params = new DataCollection();
                params.add(new Data("count",9));
                activity.pushViewController(ESPhotoBucket.class.getName(),params,true);
                break;
        }
    }


}
