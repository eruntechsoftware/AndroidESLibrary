package com.birthstone.core.parse;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class DataCollection extends LinkedList<Data> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 5187321951128267808L;
    public Data CurrentData = null;
    private boolean isChecked;

    public DataCollection ()
    {
    }

    public boolean addAll (DataCollection params)
    {
        return super.addAll(params);
    }

    public Data get (String name)
    {
        Data data = null;
        if (this.CurrentData != null && this.CurrentData.Name.toUpperCase().equals(name.toUpperCase()))
        {
            return this.CurrentData;
        }
        else
        {
            int size = this.size();

            for (int i = 0; i < size; ++i)
            {
                if (((Data) this.get(i)).Name.toUpperCase().equals(name.toUpperCase()))
                {
                    data = (Data) this.get(i);
                    this.CurrentData = data;
                    break;
                }
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
        }
        catch (Exception var4)
        {
            ;
        }

        return dataCollection;
    }
}
