package com.birthstone.core.parse;

import java.io.Serializable;
import java.util.LinkedList;

import android.util.Log;

public class DataTable extends LinkedList<DataCollection> implements Serializable
{
	private static final long serialVersionUID = -7116458462588700584L;
	int index = 0;
	int lastRet = -1;

	public DataTable( )
	{

	}

	public boolean hasNext()
	{
		return index != size();
	}

	public DataCollection next()
	{
		DataCollection datas = this.get(index);
		lastRet = index++;
		return datas;
	}

	public void setPosition(int position)
	{
		if(position < size() && position > -1)
		{
			index = position;
			lastRet = index;
		}
	}

	public DataTable getIndexData(int startIndex, int rows)
	{
		DataTable DataTable = new DataTable();
		try
		{
			int size = this.size();
			int len = startIndex + rows;
			if(len >= 0)
			{
				for(int i = startIndex; i < len; i++)
				{
					if(i < size)
					{
						DataTable.add(this.get(i));
					}
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("getAllData", ex.getMessage());
		}
		return DataTable;
	}
}
