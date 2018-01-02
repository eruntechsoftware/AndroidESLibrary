package com.birthstone.core.parse;

import android.util.Log;

import com.birthstone.core.helper.DataType;

import java.io.Serializable;

public class Data implements Cloneable, Serializable
{

    private static final long serialVersionUID = 3158113122131617945L;
    public String Name;
    public DataType DataType;
    public Object Value;

    public Data ()
    {
    }

    public Data (String name, Object value)
    {
        this.Name = name;
        this.DataType = DataType.String;
        this.Value = value;
    }

    public Data (String name, Object value, DataType datatype)
    {
        this.Name = name;
        this.DataType = datatype;
        this.Value = value;
    }

    public Data copy ()
    {
        Data data = new Data();
        try
        {
            data.setName(this.Name);
            data.setValue(this.Value);
        }
        catch (Exception ex)
        {
            Log.e("copy", ex.getMessage());
        }
        return data;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String name)
    {
        Name = name;
    }

    public DataType getDataType ()
    {
        return DataType;
    }

    public void setDataType (DataType dataType)
    {
        DataType = dataType;
    }

    public Object getValue ()
    {
        return Value;
    }

    public String getStringValue ()
    {
        return this.Value.toString();
    }

    public int getIntValue ()
    {
        return Integer.valueOf(this.Value.toString());
    }

    public float getFloatValue ()
    {
        return Float.valueOf(this.Value.toString());
    }

    public Boolean getBooleanValue ()
    {
        Boolean result = Boolean.valueOf(false);
        if (this.Value != null && this.Value.toString().equals("1"))
        {
            result = Boolean.valueOf(true);
        }

        if (this.Value != null && this.Value.toString().equals("0"))
        {
            result = Boolean.valueOf(false);
        }
        else
        {
            result = Boolean.valueOf(Boolean.parseBoolean(this.Value.toString()));
        }

        return result;
    }

    public void setValue (Object value)
    {
        Value = value;
    }


    @Override
    public Object clone ()
    {
        Data data = null;
        try
        {
            data = (Data) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
