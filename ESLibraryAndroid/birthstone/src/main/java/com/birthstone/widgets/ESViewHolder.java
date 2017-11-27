package com.birthstone.widgets;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.birthstone.base.helper.ReleaseHelper;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ming on 2017/11/27.
 */

public class ESViewHolder extends RecyclerView.ViewHolder implements IChildView
{
    private List<View> views = new LinkedList<>();

    public ESViewHolder (View itemView)
    {
        super(itemView);
    }

    /**
     *发布数据集到当前屏幕
     * */
    public void release(DataCollection params)
    {
        ReleaseHelper releaseHelper;
        try
        {
            releaseHelper = new ReleaseHelper(params, this);
            releaseHelper.release(null);
        }
        catch(Exception ex)
        {
            Log.e("", ex.getMessage());
        }
    }

    /**
     *添加view到当前视图容器
     * @param view 视图
     * **/
    public void addView(View view)
    {
        if(views==null)
        {
            views = new LinkedList<>();
        }
        views.add(view);
    }

    @Override
    public List<View> getViews ()
    {
        return views;
    }
}
