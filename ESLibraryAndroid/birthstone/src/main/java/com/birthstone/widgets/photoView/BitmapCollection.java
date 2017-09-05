package com.birthstone.widgets.photoView;

import android.net.Uri;

import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ValidatorHelper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * bitmap容器类，用于处理选中图片
 */
public class BitmapCollection{

    /**存储UIR对象文件**/
    public static List<Uri> fileURIList = new LinkedList<Uri>();

    /**存储String路径文件**/
    public static List<String> filePahtList = new LinkedList<String>();

    /**bitmap数据接口，供外部删除图片时使用**/
    public static BitmapCollectionDelegate delegate;

    /**
     * 添加文件到文件容器对象
     * @param uriFile uri文件
     * **/
    public static void add(Uri uriFile){
        fileURIList.add(uriFile);
        filePahtList.add(uriFile.getPath());
    }

    /**
     * 添加文件到文件容器对象
     * @param filePath 文件路径
     * **/
    public static void add(String filePath){
        fileURIList.add(Uri.parse(filePath));
        filePahtList.add(filePath);
    }

    /**
     * 移除指定索引对象
     * @param index 索引
     * **/
    public static void remove(int index){
        if(filePahtList!=null && filePahtList.size()>index){
            filePahtList.remove(index);
            fileURIList.remove(index);
            if (delegate!=null){
                delegate.changed();
            }
        }
    }

    /**
     * 移除指定对象
     * @param object 对象
     * **/
    public static void remove(Object object){
        if(filePahtList!=null && filePahtList.size()>0){
            filePahtList.remove(object);
            fileURIList.remove(object);
            if (delegate!=null){
                delegate.changed();
            }
        }
    }

    /**
     * 从文件容器中获取所有文件的列表
     * **/
    public static List<String> getFilePahtList(){
        return filePahtList;
    }

    /**
     * 从文件容器中获取本地文件的列表
     * **/
    public static File[] getFileList(){
        List<File> updateFileList = new LinkedList<File>();
        //循环检查本地图片数量并统计web图片数
        for (String path:filePahtList){
            if(ValidatorHelper.isMached(DataTypeExpression.filePath(),path)){
                updateFileList.add(new File(path));
            }
        }
        return (File[]) updateFileList.toArray();
    }

    /**
     * 从文件容器中获取web文件的列表
     * **/
    public static String[] getWebFileList(){
        List<File> webFileList = new LinkedList<File>();
        //循环检查本地图片数量并统计web图片数
        for (String path:filePahtList){
            if(!ValidatorHelper.isMached(DataTypeExpression.filePath(),path)){
                webFileList.add(new File(path));
            }
        }
        return (String[]) webFileList.toArray();
    }

    /**
     * 清空数据
     * **/
    public static void clear(){
        fileURIList.clear();
        filePahtList.clear();
    }

    /**图片数据操作委托**/
    public interface BitmapCollectionDelegate{
        public void changed();
    }
}
