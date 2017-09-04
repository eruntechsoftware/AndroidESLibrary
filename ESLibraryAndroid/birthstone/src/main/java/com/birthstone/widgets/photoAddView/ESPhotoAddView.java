package com.birthstone.widgets.photoAddView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.widgets.ESActionSheet;
import com.birthstone.widgets.ESGridView;
import com.birthstone.widgets.photoView.BitmapCollection;
import com.birthstone.widgets.photoView.ESPhotoView;
import com.birthstone.widgets.photoView.FrescoImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.AndroidManifest.xml的Application节点添加
 <provider
 android:name="android.support.v4.content.FileProvider"
 android:authorities="com.yancy.gallerypickdemo.fileprovider"
 android:exported="false"
 android:grantUriPermissions="true">
 <meta-data
 android:name="android.support.FILE_PROVIDER_PATHS"
 android:resource="@xml/filepaths" />
 </provider>
 * 2.values下边添加名为xml的文件夹，新建文件名为filepaths.xml
 * 内容
 <?xml version="1.0" encoding="utf-8"?>
 <resources>
 <paths>
 <external-path
 name="external"
 path="" />
 <files-path
 name="files"
 path="" />
 <cache-path
 name="cache"
 path="" />
 </paths>
 </resources>
 *
 * **/

/**
 * 图片上传组件
 */
public class ESPhotoAddView extends ESGridView implements AdapterView.OnItemClickListener, IDataInitialize, ESActionSheet.OnActionSheetClickListener{

    /**Web图片计数**/
    private static int WEB_IMAGE_COUNT;

    private Activity activity;
    private ESActionSheet actionSheetPhoto;
    private String bitmapCachePath,authorities;
    private ESPhotoAddViewAdapter adapter;
    /**上传文件的列表**/
    public BitmapCollection bitmapCollection;

    /**记录已选择的图片**/
    private List<String> path = new ArrayList<>();
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    //声明常量权限
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String TAG="ESPhotoAddView";


    public ESPhotoAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESPhotoAddView);
        bitmapCachePath = a.getString(R.styleable.ESPhotoAddView_bitmapCachePath);
        authorities = a.getString(R.styleable.ESPhotoAddView_authorities);
        if (authorities==null || "".equals(authorities)){
            ToastHelper.toastShow(context,"必须配置在Manifests配置provider，并在布局中指定authorities属性");
        }
        bitmapCollection = new BitmapCollection();
        adapter = new ESPhotoAddViewAdapter(context,bitmapCollection);
        this.setOnItemClickListener(this);
    }

    public void setAdapter(ESPhotoAddViewAdapter adapter){
        super.setAdapter(adapter);
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

        if(position == bitmapCollection.getFilePahtList().size()){
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
        setAdapter(adapter);

        if (bitmapCachePath==null || "".equals(bitmapCachePath)){
            if(!com.birthstone.core.helper.File.getSDCardPath().equals("")){
                bitmapCachePath = com.birthstone.core.helper.File.getSDCardPath()+"eruntechcache//bitmap//";
            }else{
                ToastHelper.toastShow(activity,R.string.sd_card);
            }
        }

        initHandlerCallBack();

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new FrescoImageLoader(activity))    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider(authorities)   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
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
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                Toast.makeText(activity, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onClick(View view) {
        requestPermission();
        switch (view.getId()){
            case 0:
                //从相机拍照选取图片
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).openCamera(activity);
                break;
            case 1:
                //启动GalleryPick图片选择器
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity);
                break;
        }
    }

    /**
     * 绑定选择的图片
     * **/
    private void initHandlerCallBack() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                for (String s : photoList) {
                    bitmapCollection.add(s);
                }
                adapter.bind();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };

    }
}
