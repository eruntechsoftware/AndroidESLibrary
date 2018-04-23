package com.birthstone.core.parse;

import java.io.Serializable;
import java.util.LinkedList;

public class DataCollection extends LinkedList<Data> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 5187321951128267808L;
    public Data currentData = null;
    private boolean isChecked;

    public DataCollection ()
    {
    }

    public boolean addAll (DataCollection params)
    {
        return super.addAll(params);
    }

    @Override
    public boolean add (Data data)
    {
        return super.add(data);
    }

    /**
     * 获取指定名称的集合
     *
     * @param name 名称
     **/
    public Data get (String name)
    {
        if (currentData != null && currentData.Name.toUpperCase().equals(name.toUpperCase()))
        {
            return currentData;
        }

        int size = this.size();
        for(int i = 0; i < size; i++)
        {
            if(this.get(i).Name.toUpperCase().equals(name.toUpperCase()))
            {
                currentData = this.get(i);
                break;
            }
        }
        return currentData;
    }

    @Override
    public boolean remove (Object o)
    {
        currentData = null;
        return super.remove(o);
    }

    @Override
    public void clear ()
    {
        super.clear();
    }

    /**
     * 获取当前集合是否选中状态
     **/
    public boolean isChecked ()
    {
        return isChecked;
    }

    /**
     * 设置当前集合选中状态
     *
     * @param isChecked 状态
     **/
    public void setChecked (boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    @Override
    public Object clone ()
    {
        DataCollection dataCollection = null;
        try
        {
            dataCollection = (DataCollection) super.clone();
            dataCollection.clear();
            for (Data data : this)
            {
                dataCollection.add((Data) data.clone());
            }
        }
        catch (Exception ex)
        {

        }
        return dataCollection;
    }
}
