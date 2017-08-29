package com.birthstone.widgets;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ValidatorHelper;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 图片显示控件
 */
public class ESImageView extends SimpleDraweeView {

    public static String IMAGE_URL_HEAD="";

    public ESImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 获取网络地址头段
     * **/
    public static String getImageUrlHead(){
        return IMAGE_URL_HEAD;
    }

    /**
     显示网络图片
     @param urlBody  图片网络尾地址
     */
    public void setImageWithUrlBody(String urlBody){
        String imageUrl = IMAGE_URL_HEAD+urlBody;
        this.setImageURI(Uri.parse(imageUrl));
    }

    /**
     *设置图片显示的web路径
     * @param url 图片url，可以只设置图片后段地址
     */
    public void setImageURI(String url){
        //判断是否URL地址
        if(ValidatorHelper.isMached(DataTypeExpression.URL(),url)){
            this.setImageURI(Uri.parse(url));
        }else {
            this.setImageWithUrlBody(url);
        }
    }
}
