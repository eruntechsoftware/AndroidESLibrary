package com.birthstone.widgets.ESPhotoAddView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.widgets.ESActionSheet;
import com.birthstone.widgets.ESGridView;

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
    private Drawable btnAddImage;
    private ESPhotoAddViewAdapter adapter;

    /**上传文件的列表**/
    public List<File> updateFileList;

    /**图片路径容器，之存储图片路径**/
    public static List<String> IMAGE_PATH_LIST = new LinkedList<String>();


    public ESPhotoAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESPhotoAddView);
        btnAddImage = a.getDrawable(R.styleable.ESPhotoAddView_buttonAddImage);
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
//            activity.pushViewController(MyPhotoView.class.getName(), params, false);
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

    @Override
    public void onClick(View view) {

    }
}
