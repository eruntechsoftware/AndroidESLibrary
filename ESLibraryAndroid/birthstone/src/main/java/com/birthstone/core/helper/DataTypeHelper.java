package com.birthstone.core.helper;


public class DataTypeHelper {
	public static DataType valueOf(String value)
	{
		DataType dataType = null;
		
		if(value==null)
		{
			dataType=DataType.String;
		}
		else
		{
			if(value.toUpperCase().equals(DataType.Binary.toString().toUpperCase().trim().toUpperCase().trim()))
			{
				dataType=DataType.Binary;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Boolean.toString().toUpperCase().trim()))
			{
				dataType=DataType.Boolean;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Date.toString().toUpperCase().trim()))
			{
				dataType=DataType.Date;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.DateTime.toString().toUpperCase().trim()))
			{
				dataType=DataType.DateTime;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Guid.toString().toUpperCase().trim()))
			{
				dataType=DataType.Guid;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Integer.toString().toUpperCase().trim()))
			{
				dataType=DataType.Integer;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Money.toString().toUpperCase().trim()))
			{
				dataType=DataType.Money;
				return dataType;
			}
			if(value.toUpperCase().equals(DataType.Numeric.toString().toUpperCase().trim()))
			{
				dataType=DataType.Numeric;
				return dataType;
			}
			else
			{
				dataType=DataType.String;
				return dataType;
			}
		}
		return dataType;	
	}
}
