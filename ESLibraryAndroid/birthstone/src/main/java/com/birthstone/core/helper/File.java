package com.birthstone.core.helper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * @author
 * д
 * **/
public class File {
	
	private Context context;
	
	/**
	 * File ĬϹ캯
	 * **/
	public File()
	{}
	
	/**
	 * @author
	 * File
	 * @param context 뵱ǰĶ
	 **/
	public File(Context context)
	{
		this.setContext(context);
	}
	
	/**
	 * ǰ豸SDCard·
	 * @return 洢·
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
			Log.e("SDCard·쳣", ex.getMessage());
		}
		return null;
	}

	/**
	 * @author
	 * ȷǷЧ
	 * @param fileName
	 * @return ǷЧ
	 * **/
	public static Boolean exists(String fileName) 
	{
		java.io.File file = new java.io.File(fileName);
		return file.exists();
	}
	
	/**
	 * @author
	 * ɾָ
	 * **/
	public static void delete(String path)
	{
		java.io.File file = new java.io.File(path);
		file.delete();
	}
	
	/** 
	 * @author
     *
     * @param filename
     * @param content
     * @param mode ģʽ 
     * @throws Exception 
     */  
    public void save(String filename, String content, int mode) throws Exception{  
        FileOutputStream fos = context.openFileOutput(filename, mode);  
        //д
        fos.write(content.getBytes());  
        //ر
        fos.close();  
    }  
    
    /** 
     * 洢д
     * @param filename
     * @param content дַ
     * @throws Exception 
     */  
    public void saveToSDCard(String filename, String content) throws Exception{  
        //SDcard·2.2ǰ/sdcard2.2ϰ汾/mnt/sdcardòķʽа汾
    	java.io.File file = new java.io.File(filename);  
        FileOutputStream fos = new FileOutputStream(file);  
        //д
        fos.write(content.getBytes());  
        fos.flush();
        //ر
        fos.close();  
          
    }  

	/** 
	 * @author
     * ָ
     * @param fileName
     * @return ַ
     */  
	public String read(String fileName)  
	{
		StringBuffer StringB = new StringBuffer(1024);
		try 
		{
			FileReader reader = new FileReader(fileName);
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
			Log.e("ʧܣ", ex.getMessage());
		}
		return StringB.toString();
	}

	/**
	 * @author
	 * ض
	 * @param fileName
	 * @return ضֽ
	 * @
	 **/
	public static byte[] readFile(String fileName) 
	{
		byte[] data = null;
		try 
		{
			FileInputStream fis = new FileInputStream(fileName);
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
			Log.e("", ex.getMessage());
		}
		return data;
	}
	
	/**
	 * @author
	 * ͼƬ
	 * @param fileName
	 * @return BitmapͼƬ
	 * */
	public Bitmap readImage(String fileName)
	{
		try 
		{
			byte[] bytes=readFile(fileName);
			if(bytes!=null && bytes.length>0)
			{
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		} 
		catch (Exception ex) 
		{
			Log.e("ͼƬ", ex.getMessage());
		}
		return null;
	}

	/**
	 * @author
	 * Դ
	 * @param fileName ƣ·
	 * @param context
	 * @return رԴ
	 */
	public static String getAssetsString(String fileName, Context context)
	{
		int len;
		InputStream inputStream;
		ByteArrayOutputStream outputStream = null;
		byte buf[] = new byte[1024];
		try 
		{
			inputStream = context.getAssets().open(fileName); // context.getClass().getResourceAsStream(fileName);//
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
	 * @author
	 * ָд
	 * @param filepath (·)
	 * @param data ֽ
	 * @return дǷɹ
	  **/
	public static Boolean write(String filepath, byte[] data) 
	{
		try 
		{
			String[] name = filepath.split("/");
			String fileName = name[name.length - 1];
			String path = filepath.substring(0,filepath.length() - fileName.length());
			java.io.File filedir = new java.io.File(path);
			if (!filedir.exists()) 
			{
				filedir.mkdirs();
			}
			java.io.File file = new java.io.File(filepath);
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
			Log.e("д", ex.getMessage());
		}
		return false;
	}
	
	/**
	 * AssetsµͼƬԴ
	 * @param filename
	 * @param context 豸
	 **/
	public static Bitmap getAssetsImage(String filename,Context context) {
		Bitmap bitmap = null;
		try 
		{
			InputStream fis = context.getAssets().open(filename);
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
	 * AssetsµĶ
	 * @param filename
	 * @param context 豸
	 **/
	public static byte[] getAssetsByte(String filename,Context context) {
		byte[] fileBytes = null;
		try 
		{
			InputStream fis = context.getAssets().open(filename);
			fileBytes = new byte[fis.available()];
			fis.read(fileBytes);
		} 
		catch (Exception ex) 
		{
			Log.e("Assets", ex.getMessage());
		}
		return fileBytes;
	}
	
	/**
	 * Ķ
	 * @return Ķ
	 * **/
	public Context getContext() {
		return context;
	}
	
	/**
	 * Ķ
	 * @param context Ķ
	 * **/
	public void setContext(Context context) {
		this.context = context;
	}
}
