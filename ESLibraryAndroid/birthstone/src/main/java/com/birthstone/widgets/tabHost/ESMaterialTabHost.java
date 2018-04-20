package com.birthstone.widgets.tabHost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.ActivityHelper;

import java.util.ArrayList;

/**
 * 时 间: 2016年04月14日
 *
 * 作者：杜明悦
 *
 * 功 能: 滑动MaterialTabHost
 */
public class ESMaterialTabHost extends LinearLayout implements View.OnClickListener{

    /**声明控件**/
    private LinearLayout titleLayout;
    private LinearLayout cursor;
    private ViewPager viewPager;
//    private View parentView;

    /**变量声明**/
    /**Fragment适配器**/
    private FragmentAdapter adapter;
    /**Fragment集合**/
    private ArrayList<Fragment> fragmentList;
    /**标题集合**/
    private ArrayList<ESMaterialTab> materialTabList;
    private Boolean mDisplayTabar = true;
    /**Fragment索引**/
    private int currIndex = 0;
    /**游标宽度**/
    private int mCurrorWidth = 60;
    /**游标位置**/
    private int offset;
    /**游标起始位置**/
    private float cursorStartx;
    /**标题栏背景色**/
    private int mTabTitleBackgroundColor = Color.WHITE;
    /**标题文字默认颜色**/
    private int mTabTitleTextDefaultColor = Color.BLACK;
    /**标题文字选中时颜色**/
    private int mTabTitleTextActiveColor = Color.WHITE;
    /**标题文字大小**/
    private float mTabTitleTextSize = 13;
    /**游标颜色**/
    private int mTabCurrorColor = Color.RED;

    /**上下文**/
    private Context mContext;
    private Animation animation = null;
    private Fragment mFragment;
    private FragmentActivity mFragmentActivity;
    private OnChangIndexListener mOnChangIndexListener;

    public ESMaterialTabHost(Context context) {
        this(context, null);

    }

    public ESMaterialTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ESMaterialTabHost);
        //标题栏背景色
        mTabTitleBackgroundColor = a.getColor(R.styleable.ESMaterialTabHost_tabBackgroundColor,Color.WHITE);
        //tab默认状态时的颜色
        mTabTitleTextDefaultColor = a.getColor(R.styleable.ESMaterialTabHost_titleColor,getResources().getColor(R.color.es_shenhui));
        //tab选中状态时的颜色
        mTabTitleTextActiveColor = a.getColor(R.styleable.ESMaterialTabHost_titleSelectedColor,Color.BLACK);

        mTabTitleTextSize = a.getDimension(R.styleable.ESMaterialTabHost_titleSize,13);
        //游标颜色
        mTabCurrorColor = a.getColor(R.styleable.ESMaterialTabHost_currorColor,getResources().getColor(R.color.es_shenhui));

        fragmentList = new ArrayList<Fragment>();
        materialTabList = new ArrayList<ESMaterialTab>();

        /**设置根布局宽、高**/
        LinearLayout.LayoutParams rootLinearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootLinearParams.gravity = Gravity.CENTER;
        this.setOrientation(LinearLayout.VERTICAL);

        /**设置title布局方式(宽、高)**/
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Activity.px2dip(mContext, Activity.dip2px(mContext,100)));
        linearParams.gravity = Gravity.CENTER;
        titleLayout = new LinearLayout(context);
        titleLayout.setGravity(Gravity.CENTER);
        titleLayout.setLayoutParams(linearParams);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setBackgroundColor(mTabTitleBackgroundColor);
        this.addView(titleLayout);

        /**实例化游标**/
        /**设置游标布局宽、高**/
        mCurrorWidth = Activity.dip2px(mContext,60);
        LinearLayout.LayoutParams cursorLinearParams = new LayoutParams(Activity.dip2px(mContext,60), Activity.px2dip(mContext, Activity.dip2px(mContext,3)));
        cursor = new LinearLayout(context);
        cursor.setBackgroundColor(mTabCurrorColor);
        cursor.setLayoutParams(cursorLinearParams);
        this.addView(cursor);

        /**实例化viewPager**/
        viewPager = new ViewPager(context);
        viewPager.setId(R.id.IDCard);
        viewPager.setBackgroundColor(Color.WHITE);
        viewPager.setLayoutParams(rootLinearParams);
        this.addView(viewPager);

    }

    /**
     * 修改时间：2016年04月14日
     * 作者：杜明悦
     * 功能：添加Fragment及标题
     * @param title 标题
     * @param fragment fragment
     */
    public void addFragment(Fragment parmentFragment, String title, Fragment fragment){

        this.mFragment = parmentFragment;

        //添加Fragment
        if(fragmentList != null){
            fragmentList.add(fragment);
        }
        //添加tab标签页
        if(materialTabList != null){
            ESMaterialTab subTab = new ESMaterialTab(mContext);
            subTab.setTitleText(title);
            subTab.setTitleSize(mTabTitleTextSize);
            materialTabList.add(subTab);
            subTab.setIndex(materialTabList.size()-1);
            subTab.setOnClickListener(this);
            materialTabList.get(0).setTextColor(mTabTitleTextActiveColor);
            titleLayout.addView(subTab);
        }
        initWidth();
        initViewPager();
    }



    /**
     * 修改时间：2016年04月14日
     * 作者：杜明悦
     * 功能：添加Fragment及标题
     * @param title 标题
     * @param fragment fragment
     */
    public void addFragment(FragmentActivity parmentFragmentActivity, String title, Fragment fragment){

        this.mFragmentActivity = parmentFragmentActivity;

        //添加Fragment
        if(fragmentList != null){
            fragmentList.add(fragment);
        }
        //添加tab标签页
        if(materialTabList != null){
            ESMaterialTab subTab = new ESMaterialTab(mContext);
            subTab.setTitleText(title);
            materialTabList.add(subTab);
            subTab.setIndex(materialTabList.size()-1);
            subTab.setOnClickListener(this);
            materialTabList.get(0).setTextColor(mTabTitleTextActiveColor);
            titleLayout.addView(subTab);
        }
        initWidth();
        initViewPager();
    }

    /**
     * 修改时间：2016年04月16日
     * 作者：杜明悦
     * 功能：添加Fragment及标题
     */
    public void initViewPager(){
        if(adapter == null) {
            if(mFragment!=null) {
                adapter = new FragmentAdapter(mFragment.getChildFragmentManager(), fragmentList);
            }
            if(mFragmentActivity != null){
                adapter = new FragmentAdapter(mFragmentActivity.getSupportFragmentManager(), fragmentList);
            }
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 修改时间：2016年04月16日
     *
     * 作者：杜明悦
     *
     * 功能：初始化获取屏幕以及游标宽度
     */
    private void initWidth(){
        //先获取屏幕宽度
        int screenW = 0;
        if(this.mFragment != null) {
            screenW = ActivityHelper.getActivityWidth(this.mFragment.getActivity());
        }
        if(this.mFragmentActivity != null){
            screenW = ActivityHelper.getActivityWidth(this.mFragmentActivity);
        }
        //每段title的宽度
        offset = screenW/fragmentList.size();

        mCurrorWidth = offset/2;
        cursor.getLayoutParams().width = mCurrorWidth;

        cursorStartx = mCurrorWidth / 2;
        animation = new TranslateAnimation(0, cursorStartx, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(0);
        cursor.startAnimation(animation);
    }

    public void onClick(View v) {
			viewPager.setCurrentItem(((ESMaterialTab)v).getIndex());
    }

    /**
     * 修改时间：2016年03月21日
     *
     * 作者：张景瑞
     *
     * 功能：页面切换监听事件处理
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            if(mOnChangIndexListener != null){
                mOnChangIndexListener.changed(i);
            }
            if(mDisplayTabar==true) {
                final int index = currIndex;
                animation = new TranslateAnimation(cursorStartx + offset * currIndex, offset * i + cursorStartx, 0, 0);//这个比较简洁，只有一行代码。
                currIndex = i;
                animation.setFillAfter(true);
                animation.setDuration(300);
                cursor.startAnimation(animation);

                if (materialTabList != null && materialTabList.size() > 0) {
                    //先设置为默认色
                    materialTabList.get(index).setTextColor(mTabTitleTextDefaultColor);
                    //设置为选中颜色
                    materialTabList.get(i).setTextColor(mTabTitleTextActiveColor);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 移除所有视图
     * **/
    public void removeAllViews(){
        super.removeAllViews();
        if(fragmentList!=null && fragmentList.size()>0){
            fragmentList.clear();
        }

        if(materialTabList!=null && materialTabList.size()>0){
            materialTabList.clear();
        }

        adapter=null;
    }

    /**
     * 修改时间：2016年05月12日
     * 作者：杜明悦
     * 功能：设置是否显示Tab栏
     * @param displayTabar 是否显示
     */
    public void setDisplayTabar(Boolean displayTabar){
        this.mDisplayTabar = displayTabar;
        if(this.mDisplayTabar == false){
            titleLayout.setVisibility(View.GONE);
            cursor.setVisibility(View.GONE);
        }else{
            titleLayout.setVisibility(View.VISIBLE);
            cursor.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 修改时间：2016年04月15日
     * 作者：杜明悦
     * 功能：设置标题栏背景色
     * @param titleBackgroundColor 标题栏背景色
     */
    public void setTitleBackgroundColor(int titleBackgroundColor){
        this.mTabTitleBackgroundColor =titleBackgroundColor;
        if(this.titleLayout!=null){
            this.titleLayout.setBackgroundColor(mTabTitleBackgroundColor);
        }
    }

    /**
     * 修改时间：2016年04月15日
     * 作者：杜明悦
     * 功能：设置标题文本默认色
     * @param titleTextDefaultColor 标题文本默认色
     */
    public void setTitleTextDefaultColor(int titleTextDefaultColor){
        this.mTabTitleBackgroundColor =titleTextDefaultColor;
        if(this.materialTabList!=null && materialTabList.size()>0){
            for(ESMaterialTab tab:materialTabList){
                tab.setTextColor(mTabTitleTextDefaultColor);
            }
        }
    }

    /**
     * 修改时间：2016年04月15日
     * 作者：杜明悦
     * 功能：设置标题栏背景色
     * @param titleTextActiveColor 标题栏背景色
     */
    public void setTitleTextActiveColor(int titleTextActiveColor){
        this.mTabTitleTextActiveColor =titleTextActiveColor;
    }

    /**
     * 修改时间：2016年04月15日
     * 作者：杜明悦
     * 功能：设置游标背景色
     * @param currorColor 标题栏背景色
     */
    public void setCurrorColor(int currorColor){
        this.mTabCurrorColor = currorColor;
        if(this.cursor!=null){
            this.cursor.setBackgroundColor(mTabCurrorColor);
        }
    }

    /**
     * 设置滑动处理事件监听
     * @param onChangIndexListener 事件监听接口
     * **/
    public void setOnChangIndexListener(OnChangIndexListener onChangIndexListener){
        this.mOnChangIndexListener = onChangIndexListener;
    }

    /**
     * 修改时间：2016年04月26日
     * 作者：杜明悦
     * 功能：获取当前选择的索引
     */
    public int getCurrIndex(){
        return currIndex;
    }

    /**
     * 修改时间：2016年07月1日
     * 作者：杜明悦
     * 功能：设置当前选择的索引
     */
    public void setCurrIndex(int currIndex){
        if(viewPager!=null) {
            viewPager.setCurrentItem(currIndex, true);
        }
    }

}
