package com.birthstone.annotation;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.Fragment;

import java.lang.reflect.Field;


public class ViewInjectUtils
{

	/**
	 * 绑定Activity
	 * @param activity
	 * **/
	public static void inject(Activity activity)
	{
		Log.e("TAG", "inject");
		injectContentView(activity);
		injectViews(activity);
		// injectEvents(activity);
	}

	/**
	 * 绑定Activity
	 * @param activity
	 */
	private static void injectViews(Activity activity)
	{
		Class<? extends Activity> clazz = activity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields)
		{
			Log.e("TAG", field.getName() + "");
			SetViewInject viewInjectAnnotation = field.getAnnotation(SetViewInject.class);
			if(viewInjectAnnotation != null)
			{
				int viewId = viewInjectAnnotation.value();
				if(viewId != -1)
				{
					Log.e("TAG", viewId + "");
					try
					{
						Object resView = activity.findViewById(viewId);
						field.setAccessible(true);
						field.set(activity, resView);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

				}
			}

		}

	}

	/**
	 * 绑定Activity布局视图
	 * @param activity
	 */
	private static void injectContentView(Activity activity)
	{
		Class<? extends Activity> clazz = activity.getClass();
		SetContentView contentView = clazz.getAnnotation(SetContentView.class);
		if(contentView != null)// ����
		{
			int contentViewLayoutId = contentView.value();
			try
			{
				activity.setContentView(contentViewLayoutId);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 绑定FragmentActivity
	 * @param fragmentActivity
	 * **/
	public static void inject(FragmentActivity fragmentActivity)
	{
		Log.e("TAG", "inject");
		injectContentView(fragmentActivity);
		injectViews(fragmentActivity);
	}

	/**
	 * 绑定FragmentActivity
	 * @param fragmentActivity
	 */
	private static void injectViews(FragmentActivity fragmentActivity)
	{
		Class<? extends FragmentActivity> clazz = fragmentActivity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields)
		{
			Log.e("TAG", field.getName() + "");
			SetViewInject viewInjectAnnotation = field.getAnnotation(SetViewInject.class);
			if(viewInjectAnnotation != null)
			{
				int viewId = viewInjectAnnotation.value();
				if(viewId != -1)
				{
					
					try
					{
						Object resView = fragmentActivity.findViewById(viewId);
						field.setAccessible(true);
						field.set(fragmentActivity, resView);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

				}
			}

		}

	}

	/**
	 * 绑定FragmentActivity布局视图
	 * @param fragmentActivity
	 */
	private static void injectContentView(FragmentActivity fragmentActivity)
	{
		Class<? extends FragmentActivity> clazz = fragmentActivity.getClass();

		SetContentView contentView = clazz.getAnnotation(SetContentView.class);
		if(contentView != null)
		{
			int contentViewLayoutId = contentView.value();
			try
			{
				fragmentActivity.setContentView(contentViewLayoutId);
//				Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
//				method.setAccessible(true);
//				method.invoke(fragmentActivity, contentViewLayoutId);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 绑定Fragment
	 * @param fragment
	 * **/
	public static void inject(Fragment fragment)
	{
		Log.e("TAG", "inject");
		injectContentView(fragment);
		injectViews(fragment);
		// injectEvents(activity);
	}
	
	/**
	 * 绑定Fragment
	 * @param fragment
	 */
	private static void injectViews(Fragment fragment)
	{
		Class<? extends Fragment> clazz = fragment.getClass();
		Field[] fields = clazz.getDeclaredFields();
		View view = fragment.getRootView();

		for(Field field : fields)
		{
			Log.e("TAG", field.getName() + "");
			SetViewInject viewInjectAnnotation = field.getAnnotation(SetViewInject.class);
			if(viewInjectAnnotation != null)
			{
				int viewId = viewInjectAnnotation.value();
				if(viewId != -1)
				{
					Log.e("TAG", viewId + "");
					try
					{
						Object resView = view.findViewById(viewId);
						fragment.views.add((View)resView);
						field.setAccessible(true);
						field.set(fragment, resView);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

				}
			}

		}

	}
	
	/**
	 * 绑定Fragment布局视图
	 * @param fragment
	 */
	private static void injectContentView(Fragment fragment)
	{
		Class<? extends Fragment> clazz = fragment.getClass();
		SetContentView contentView = clazz.getAnnotation(SetContentView.class);
		if(contentView != null)
		{
			int contentViewLayoutId = contentView.value();
			try
			{
				fragment.setCreateView(contentViewLayoutId);
//				Method method = clazz.getMethod("setCreateView", int.class);
//				method.setAccessible(true);
//				method.invoke(fragment, contentViewLayoutId);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
