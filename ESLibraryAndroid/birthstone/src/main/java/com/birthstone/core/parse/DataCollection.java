package com.birthstone.core.parse;

import com.birthstone.core.parse.Data;

import java.io.Serializable;
import java.util.LinkedList;

public class DataCollection extends LinkedList<Data> implements Serializable
{
	private static final long serialVersionUID = 5187321951128267808L;
	public Data CurrentData = null;
	private boolean isChecked;

	public DataCollection( )
	{
	}

	public boolean addAll(DataCollection params)
	{
		return super.addAll(params);
	}
	
	/**
	 * 获取指定名称的集合
	 * @param name 名称
	 * **/
	public Data get(String name)
	{
		Data data = null;
		if(CurrentData != null && CurrentData.Name.toUpperCase().equals(name.toUpperCase())) { 
			 return CurrentData; 
		}
		int size = this.size();
		 for(int i = 0; i < size; i++)
		 {
			 if(this.get(i).Name.toUpperCase().equals(name.toUpperCase()))
			 {
				 data = this.get(i);
				 CurrentData = data;
				 break;
			 }
		 }
		 return data;
	}
	

	/**
	 * 获取当前集合是否选中状态
	 * **/
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * 设置当前集合选中状态
	 * @param isChecked 状态
	 * **/
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
