package com.mychat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mychat.R;
import com.mychat.activitys.own.UserInfoActivity;
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
     *  实现点击评论回复中的用户名打开对应的用户信息页面
     *
     *
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
            //处理点击回复数据的用户名
            ClickableSpan discussClick = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击的用户："+data.getDiscussusername(),Toast.LENGTH_SHORT).show();
                    //打开用户信息的界面
                    openUserInfoActivity(data.getDiscussuid());
                }
            };
            builder.setSpan(discussClick,0,username.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
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
            builder.setSpan(targetColorSpan,start,builder.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //处理被点击数据中的用户名
            ClickableSpan targetClick = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击的用户："+data.getTargetusername(),Toast.LENGTH_SHORT).show();
                    openUserInfoActivity(data.getTargetuid());
                }
            };
            builder.setSpan(targetClick,start,builder.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //放入回复的内容
            if(!TextUtils.isEmpty(data.getConent())){
                start = builder.length();
                builder.append(data.getConent());
                ForegroundColorSpan contentSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
                builder.setSpan(contentSpan,start,builder.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            txtContent.setText(builder);
        }else{
            //当前是评论数据 AA:XXX
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString username = new SpannableString(data.getDiscussusername()+":");
            builder.append(username);
            //设置用户名的颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            builder.setSpan(colorSpan,0,username.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //对当前的用户名添加点击事件
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击了:"+data.getDiscussusername(),Toast.LENGTH_SHORT).show();
                    openUserInfoActivity(data.getDiscussuid());
                }
            };
            builder.setSpan(clickableSpan,0,username.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            if(!TextUtils.isEmpty(data.getConent())) {
                int start = builder.length();
                //放入回复的内容
                builder.append(data.getConent());
                ForegroundColorSpan normalSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
                builder.setSpan(normalSpan, start, builder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            txtContent.setText(builder);
        }
        //设置txtContent文本响应点击事件
        txtContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 打开用户信息页面
     * @param uid
     */
    private void openUserInfoActivity(String uid){
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra("uid",uid);
        mContext.startActivity(intent);
    }
}
