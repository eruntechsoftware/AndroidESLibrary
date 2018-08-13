package com.birthstone.core.parse;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataCollection extends LinkedList<Data> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 5187321951128267808L;

    private List<String> columns = new LinkedList<>();
    public Data CurrentData = null;
    private boolean isChecked;

    public DataCollection ()
    {
    }

    @Override
    public boolean add(Data data)
    {
        if(!columns.contains(data.getName().toLowerCase()))
        {
            columns.add(data.getName().toLowerCase());
        }
        return super.add(data);
    }

    public boolean addAll(DataCollection params)
    {
        for(Data data:params)
        {
            if(!columns.contains(data.getName()))
            {
                columns.add(data.getName());
            }
        }
        return super.addAll(params);
    }

    public Data get (String name)
    {
        Data data = null;
        if (this.CurrentData != null && this.CurrentData.getName().toUpperCase().equals(name.toUpperCase()))
        {
            return this.CurrentData;
        }
        else
        {
            if(columns.contains(name.toLowerCase()))
            {
                int index = columns.indexOf(name.toLowerCase());
                return this.get(index);
            }

            return data;
        }
    }

    public boolean remove (Object o)
    {
        this.CurrentData = null;
        return super.remove(o);
    }

    public boolean isChecked ()
    {
        return this.isChecked;
    }

    public void setChecked (boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public Object clone ()
    {
        DataCollection dataCollection = null;

        try
        {
            dataCollection = (DataCollection) super.clone();
            dataCollection.clear();
            Iterator var2 = this.iterator();

            while (var2.hasNext())
            {
                Data data = (Data) var2.next();
                dataCollection.add((Data) data.clone());
            }
            dataCollection.setColumns(this.getColumns());
        }
        catch (Exception var4)
        {
            ;
        }

        return dataCollection;
    }

    public List<String> getColumns()
    {
        return columns;
    }

    public void setColumns(List<String> columns)
    {
        this.columns=columns;
    }
}
