package com.mychat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mychat.R;
import com.mychat.base.BaseAdapter;
import com.mychat.module.bean.TrendsBean;

import java.util.List;

public class TrendsDiscussAdapter extends BaseAdapter {

    public TrendsDiscussAdapter(List<TrendsBean.DataBean.DiscussBean> list, Context context){
        super(list,context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_discuss_item;
    }

    /**
     * 评论数据的显示   BB: 你的表情666
     *                AA回复BB: 你搞笑
     * @param holder
     * @param o
     */
    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        TrendsBean.DataBean.DiscussBean data = (TrendsBean.DataBean.DiscussBean) o;
        TextView txtContent = (TextView) holder.getView(R.id.txt_content);
        //判断当前的数据是评论还是回复
        if(!TextUtils.isEmpty(data.getTargetuid())){
            //当前是回复数据 AA回复BB:XXXX
            SpannableStringBuilder builder = new SpannableStringBuilder();
            //回复的用户名
            SpannableString username = new SpannableString(data.getDiscussusername());
            builder.append(username);
            //设置用户名的颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            builder.setSpan(colorSpan,0,username.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //设置默认文字颜色
            int start = username.length();
            builder.append("回复");
            ForegroundColorSpan normalSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            builder.setSpan(normalSpan,start,builder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //目标的username
            SpannableString targetUserName = new SpannableString(data.getTargetusername()+":");
            start = builder.length();
            builder.append(targetUserName);
            //设置目标用户的名字颜色
            ForegroundColorSpan targetColorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            builder.setSpan(targetColorSpan,0,username.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(targetColorSpan,start,builder.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //放入回复的内容
            start = builder.length();
            builder.append(data.getConent());
            ForegroundColorSpan contentSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            builder.setSpan(contentSpan,start,builder.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            txtContent.setText(builder);
        }else{
            //当前是评论数据 AA:XXX
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString username = new SpannableString(data.getDiscussusername()+":");
            builder.append(username);
            //设置用户名的颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            builder.setSpan(colorSpan,0,username.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            int start = builder.length();
            //放入回复的内容
            builder.append(data.getConent());
            ForegroundColorSpan normalSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            builder.setSpan(normalSpan,start,builder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            txtContent.setText(builder);
        }
    }
}
