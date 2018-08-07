package com.birthstone.core.helper;


public class DataTypeHelper {
	public static DataType valueOf(String value)
	{
		DataType dataType = null;
		if(value==null || value.equals(""))
		{
			return dataType=DataType.String;
		}
		switch (value){
			case "String":
				dataType=DataType.String;
				break;
			case "Integer":
				dataType=DataType.Integer;
				break;
			case "Numeric":
				dataType=DataType.Numeric;
				break;
			case "Date":
				dataType=DataType.Date;
				break;
			case "DateTime":
				dataType=DataType.DateTime;
				break;
			case "EMail":
				dataType=DataType.EMail;
				break;
			case "URL":
				dataType=DataType.URL;
				break;
			case "IDCard":
				dataType=DataType.IDCard;
				break;
			case "Phone":
				dataType=DataType.Phone;
				break;
		}
		return dataType;	
	}
}
