package com.birthstone.widgets.tabHost;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 时间：2016年03月16日
 *
 * 作者：张景瑞
 *
 * 功能：界面Fragment适配器
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentsList;

    public FragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    public FragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragmentsList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
