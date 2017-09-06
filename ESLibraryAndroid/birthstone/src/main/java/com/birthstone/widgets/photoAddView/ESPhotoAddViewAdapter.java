package com.birthstone.widgets.photoAddView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.birthstone.R;
import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.widgets.ESImageView;
import com.birthstone.widgets.photoView.BitmapCollection;

import java.io.File;

/**
 * 时间：2015年09月08日
 *
 * 作者：张景瑞
 *
 * 功能：添加发布照片适配器
 */

@SuppressLint("HandlerLeak")
public class ESPhotoAddViewAdapter extends BaseAdapter {

    // 视图容器
    private LayoutInflater inflater;
    public int imageMaxCount=9, imageHeight, imageWidth;

    public ProgressBar[] progressBarArray;
    private ViewGroup.LayoutParams layoutParams;

    public ESPhotoAddViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        progressBarArray=new ProgressBar[BitmapCollection.getFilePahtList().size()+1];
        return (BitmapCollection.getFilePahtList().size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.es_photoaddview_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ESImageView) convertView.findViewById(R.id.item_grida_image);
//            layoutParams = new ViewGroup.LayoutParams(imageWidth,imageHeight);
//            holder.image.setLayoutParams(layoutParams);
            holder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        progressBarArray[position] = holder.progressBar;

        if(position == BitmapCollection.getFilePahtList().size()){
            holder.progressBar.setVisibility(View.GONE);
        }else{
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        if (position == BitmapCollection.getFilePahtList().size()) {
            holder.image.setImageResource(R.mipmap.es_addpic_unfocused);
            if (position == imageMaxCount-1) {
                holder.image.setVisibility(View.GONE);
            }
        }
        else {
            //.matches("(http[s]{0,1}|ftp)://[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?")
            String url = BitmapCollection.getFilePahtList().get(position);
            //判断是否为本地文件
            if(ValidatorHelper.isMached(DataTypeExpression.filePath(),url)){
                holder.image.setImageURI(Uri.fromFile(new File(url)));
                holder.progressBar.setVisibility(View.VISIBLE);
            }else {
                holder.image.setImageURI(url);
                holder.progressBar.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public ESImageView image;
        public ProgressBar progressBar;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ESPhotoAddViewAdapter.this.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //加载图片
    public void bind() {
        this.notifyDataSetChanged();
    }



}
