package com.birthstone.core.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;

/**
 * @author 杜明悦
 * 文件处理类
 * **/
public class File {
	
	/**
	 * File构造函数
	 * **/
	public File()
	{}

	
	/**
	 * 获取SDCard路径
	 * @return SDCard路径
	 * **/
	public static String getSDCardPath()
	{
		java.io.File sdDir = null; 
		try
		{
	       boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //жsdǷ
	       if(sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//
	       }   
	       else
	       {
	    	   sdDir = Environment.getRootDirectory();
	       }
	       return sdDir.toString(); 
		}
		catch(Exception ex)
		{
			Log.e("SDCard", ex.getMessage());
		}
		return null;
	}

	/**
	 * 检查文件是否存在
	 * @param filePath 文件路径
	 * @return ǷЧ
	 * **/
	public static Boolean exists(String filePath)
	{
		java.io.File file = new java.io.File(filePath);
		return file.exists();
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件路径
	 * **/
	public static void delete(String filePath)
	{
		java.io.File file = new java.io.File(filePath);
		file.delete();
	}
	
	/**
     *保存内容到指定文件
	 * @param context 上下文
     * @param filePath 文件路径
     * @param content 文件内容
     * @param mode 读写模式
     * @throws Exception 
     */  
    public static void save(Context context, String filePath, String content, int mode) throws Exception{
        FileOutputStream fos = context.openFileOutput(filePath, mode);
        fos.write(content.getBytes());
        fos.close();  
    }  
    
    /** 
     * 保存文件到sdcard
     * @param filePath 文件路径
     * @param content 文件内容
     * @throws Exception 异常
     */  
    public static void saveToSDCard(String filePath, String content) throws Exception{
    	java.io.File file = new java.io.File(filePath);  
        FileOutputStream fos = new FileOutputStream(file);  

        fos.write(content.getBytes());  
        fos.flush();
        fos.close();  
          
    }  

	/**
     * ָ读取文件内容
     * @param filePath 文件路径
     * @return ַ内容
     */  
	public static String read(String filePath)  
	{
		StringBuffer StringB = new StringBuffer(1024);
		try 
		{
			FileReader reader = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(reader);
			String str = "";
			while ((str = bufferReader.readLine()) != null) 
			{
				if (!str.equals("")) 
				{
					StringB.append(new String(str.getBytes(), "UTF-8"));
				}
			}
		} 
		catch (Exception ex) 
		{
			Log.e("read", ex.getMessage());
		}
		return StringB.toString();
	}

	/**
	 * 读取文件
	 * @param filePath 文件路径
	 * @return byte[]字节流
	 * @
	 **/
	public static byte[] readFile(String filePath) 
	{
		byte[] data = null;
		try 
		{
			FileInputStream fis = new FileInputStream(filePath);
			byte[] buf = new byte[1024];  
	        int len = 0;  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        //
	        while((len = fis.read(buf)) != -1)
	        {  
	            baos.write(buf, 0, len);  
	        }  
	        data = baos.toByteArray();  
	        //ر
	        baos.close();  
	        fis.close();  
		} 
		catch (Exception ex) 
		{
			Log.e("read", ex.getMessage());
		}
		return data;
	}
	
	/**
	 * 读取图片
	 * @param filePath 文件路径
	 * @return Bitmap图片
	 * */
	public static Bitmap readImage(String filePath)
	{
		try 
		{
			byte[] bytes=readFile(filePath);
			if(bytes!=null && bytes.length>0)
			{
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		} 
		catch (Exception ex) 
		{
			Log.e("readImage", ex.getMessage());
		}
		return null;
	}

	/**
	 * 获取assets资源文件
	 * @param filePath 文件路径
	 * @param context 上下文
	 * @return 内容
	 */
	public static String getAssetsString(String filePath, Context context)
	{
		int len;
		InputStream inputStream;
		ByteArrayOutputStream outputStream = null;
		byte buf[] = new byte[1024];
		try 
		{
			inputStream = context.getAssets().open(filePath); // context.getClass().getResourceAsStream(filePath);//
			outputStream = new ByteArrayOutputStream();
			while ((len = inputStream.read(buf)) != -1) 
			{
				outputStream.write(buf, 0, len);
			}
			inputStream.close();
			outputStream.close();
		} 
		catch (Exception ex) 
		{
			Log.e("", ex.getMessage());
		}
		return outputStream.toString();
	}

	/**
	 * 写入byte[]字节流到指定文件
	 * @param filePath 文件路径
	 * @param data ֽ字节流
	 * @return 是否写入成功
	  **/
	public static Boolean write(String filePath, byte[] data)
	{
		try
		{
			String[] name = filePath.split("/");
			String fileTargetPath = name[name.length - 1];
			String path = filePath.substring(0,filePath.length() - filePath.length());
			java.io.File filedir = new java.io.File(path);
			if (!filedir.exists())
			{
				filedir.mkdirs();
			}
			java.io.File file = new java.io.File(filePath);
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			file.delete();
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(data);
			outputStream.close();
			return true;
		} 
		catch (Exception ex) 
		{
			Log.e("write", ex.getMessage());
		}
		return false;
	}
	
	/**
	 * 获取assets图片资源
	 * @param filePath 文件路径
	 * @param context 上下文
	 **/
	public static Bitmap getAssetsImage(String filePath,Context context) {
		Bitmap bitmap = null;
		try 
		{
			InputStream fis = context.getAssets().open(filePath);
			byte[] fileBytes = new byte[fis.available()];
			fis.read(fileBytes);
			bitmap =BitmapFactory.decodeByteArray(fileBytes, 0,fileBytes.length);
		} 
		catch (Exception ex) 
		{
			Log.e("Assets", ex.getMessage());
		}
		return bitmap;
	}
	
	/**
	 * 获取assets的byte[]
	 * @param filePath 文件路径
	 * @param context 上下文
	 **/
	public static byte[] getAssetsByte(String filePath,Context context) {
		byte[] fileBytes = null;
		try 
		{
			InputStream fis = context.getAssets().open(filePath);
			fileBytes = new byte[fis.available()];
			fis.read(fileBytes);
		} 
		catch (Exception ex) 
		{
			Log.e("Assets", ex.getMessage());
		}
		return fileBytes;
	}
}
