package com.birthstone.widgets.photoView;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 *时间：2015年09月08日
 *
 * 作者：张景瑞
 *
 * 功能：viewpager显示图片适配器
 */
public class ESPhotoViewAdapter extends PagerAdapter {
    private ArrayList<View> listViews;// content

    // 页数
    private int size;

    // 构造函数
    public ESPhotoViewAdapter(ArrayList<View> listViews) {

        // 初始化viewpager的时候给的一个页面
        this.listViews = listViews;
    }

    // 添加数据方法
    public void setListViews(ArrayList<View> listViews) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }


    public int getCount() {
        size = listViews == null ? 0 : listViews.size();
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // 销毁view对象
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    // 返回view对象
    public Object instantiateItem(View arg0, int arg1) {
        try {
            ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

        } catch (Exception e) {
        }
        return listViews.get(arg1 % size);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
