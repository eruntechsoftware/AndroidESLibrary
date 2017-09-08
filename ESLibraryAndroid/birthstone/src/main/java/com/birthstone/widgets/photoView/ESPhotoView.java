package com.birthstone.widgets.photoView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.birthstone.R;
import com.birthstone.base.switchlayout.SwichLayoutInterFace;
import com.birthstone.base.switchlayout.SwitchLayout;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;

//import com.birthstone.animation.switchlayout.SwichLayoutInterFace;
//import com.birthstone.animation.switchlayout.SwitchLayout;

/**
 * 时间：2015年09月07日
 * 作者：张景瑞，杜明悦
 * 功能：选择发布图片后点击查看大图
 * Intent参数名：bitmapList
 * 参数类型：BitmapCollection类
 * index：点击图片索引
 */
public class ESPhotoView extends AppCompatActivity implements View.OnClickListener,OnPageChangeListener,SwichLayoutInterFace {

	/**
	 * 声明控件
	 **/
	private ViewPager pager;
	private Button btnDelete;
	private RelativeLayout photo_relativeLayout;

	/**
	 * 声明变量
	 **/
	private ArrayList<View> listViews = null;
	private ESPhotoViewAdapter adapter;
	private int index;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.es_photoview);
		pager = (ViewPager) findViewById(R.id.viewpager);
		btnDelete = (Button) findViewById(R.id.btndelete);
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		pager.setOnPageChangeListener(this);

		Intent intent = getIntent();
		index = intent.getIntExtra("index",0);
		initListViews();


		// 构造adapter并设置
		adapter = new ESPhotoViewAdapter(listViews);
		pager.setAdapter(adapter);

		//接收信息，根据index判断点击的是第几张照片然后显示大图
		pager.setCurrentItem(index);

		//删除按钮
		btnDelete.setOnClickListener(this);

		setEnterSwichLayout();
	}

	/**
	 * 初始化图片预览列表视图
	 * **/
	private void initListViews() {
		int size = BitmapCollection.getFilePahtList().size();
		String path = "";
		if (listViews == null) {
			listViews = new ArrayList<View>();
		}
		for (int i = 0; i < size; i++) {

			path = BitmapCollection.getFilePahtList().get(i);

			//构造textView对象
			PhotoView img = new PhotoView(this);
			img.setBackgroundColor(0xff000000);
			img.setImageURI(Uri.parse(path));
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));

			PhotoViewAttacher mAttacher = new PhotoViewAttacher(img);
			//添加view
			listViews.add(img);
			mAttacher.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					setExitSwichLayout();
				}
			});
		}
	}

	// 页面选择响应函数
	public void onPageSelected(int arg0) {
		index = arg0;
	}

	// 滑动中。。。
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 滑动状态改变
	public void onPageScrollStateChanged(int arg0) {

	}


	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btndelete) {
			try {
				if (listViews.size() == 1) {
					BitmapCollection.remove(0);
					finish();
				} else {
					BitmapCollection.remove(index);
					pager.removeAllViews();
					listViews.remove(index);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			} catch (Exception ex) {
				Log.e("deleteOnClick", ex.getMessage());
			}
		}
	}

	public void setEnterSwichLayout() {
		SwitchLayout.getFadingIn(this);
	}

	public void setExitSwichLayout() {
		SwitchLayout.getFadingOut(this, true);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按返回键时退出Activity的Activity特效动画
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setExitSwichLayout();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
