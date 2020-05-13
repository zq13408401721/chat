package com.mychat.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.mychat.apps.MyApp;

/**
 * 图片处理的工具类
 */
public class ImgUtils {


    /**
     * 封装一个圆形头像处理的方法
     * @param url
     * @param img
     */
    public static void userHeadCircle(String url, ImageView img){
        if(!TextUtils.isEmpty(url) && img != null){
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(MyApp.myApp).load(url).apply(options).into(img);
        }
    }

    /**
     * 带有图片加载是否完成的监听方法
     * @param url
     * @param img
     * @param listener
     */
    public static void userHeadCircle(String url, ImageView img,RequestListener listener){
        if(!TextUtils.isEmpty(url) && img != null){
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(MyApp.myApp).load(url).addListener(listener).apply(options).into(img);
        }
    }

}
