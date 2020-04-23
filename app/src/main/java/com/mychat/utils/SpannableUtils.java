package com.mychat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;

import com.mychat.R;

public class SpannableUtils {

    /**
     * 转换string->SpannableString
     * @param context
     * @param word [xxx]你好！
     * @return
     */
    public static SpannableString getSpannableByWord(Context context,String word){
        if(TextUtils.isEmpty(word)) return null;
        SpannableString spannableString = new SpannableString(word);
        Drawable drawable = context.getResources().getDrawable(R.drawable.amap_man);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        //显示图标ImageSpan
        ImageSpan img = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
        int start = word.indexOf("[");
        int end = word.indexOf("]")+1;
        spannableString.setSpan(img,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置超链接效果
     * @param context
     * @param word <百度>这是一条带超链接的内容
     * @return
     */
    public static SpannableString getUrlSpanByWord(Context context,String word){
        if(TextUtils.isEmpty(word)) return null;
        int start,end;
        start = word.indexOf("<");
        end = word.indexOf(">")+1;
        String str = word.substring(start+1,end-1);
        String new_word = str+word.substring(end+1,word.length());
        SpannableString spannableString = new SpannableString(new_word);
        URLSpan urlSpan = new URLSpan("https://www.baidu.com");
        spannableString.setSpan(urlSpan,start,end-2,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
