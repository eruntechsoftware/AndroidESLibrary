package com.birthstone.widgets.ESPhotoAddView;

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
    // 选中的位置
    private int selectedPosition = -1;
    private int counts;

    public ProgressBar[] progressBarArray;

    public ESPhotoAddViewAdapter(Context context, int counts) {
        inflater = LayoutInflater.from(context);
        this.counts = counts;
    }

    public int getCount() {
        progressBarArray=new ProgressBar[ESPhotoAddView.IMAGE_PATH_LIST.size()+1];
        return (ESPhotoAddView.IMAGE_PATH_LIST.size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    //ListView Item设置
    public View getView(int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.esphotoaddview_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ESImageView) convertView.findViewById(R.id.item_grida_image);
            holder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        progressBarArray[position] = holder.progressBar;

        if(position == ESPhotoAddView.IMAGE_PATH_LIST.size()){
            holder.progressBar.setVisibility(View.GONE);
        }else{
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        if (position == ESPhotoAddView.IMAGE_PATH_LIST.size()) {
            holder.image.setImageResource(R.mipmap.es_addpic_unfocused);
            if (position == counts) {
                holder.image.setVisibility(View.GONE);
            }
        }
        else {
            //.matches("(http[s]{0,1}|ftp)://[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?")
            String url = ESPhotoAddView.IMAGE_PATH_LIST.get(position);
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
    public void loading() {
        this.notifyDataSetChanged();
    }



}
