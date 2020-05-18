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
import com.mychat.fragments.trends.TrendsFragment;
import com.mychat.module.bean.TrendsBean;

import java.util.List;

public class TrendsDiscussAdapter extends BaseAdapter {

    public int trendsid;

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
            String content = data.getDiscussusername()+"回复"+data.getTargetusername()+":"+data.getContent();
            SpannableString spannableString = new SpannableString(content);
            //计算富文本中样式和点击范围的开始和结束位置
            int start=0,end=0;
            end = data.getDiscussusername().length();
            //设置用户名的颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            spannableString.setSpan(colorSpan,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //处理点击回复数据的用户名
            ClickableSpan discussClick = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击的用户："+data.getDiscussusername(),Toast.LENGTH_SHORT).show();
                    //打开用户信息的界面
                    openUserInfoActivity(data.getDiscussuid());
                }
            };
            spannableString.setSpan(discussClick,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //设置目标用户的名字颜色
            start = end+2;
            end = start+data.getTargetusername().length()+1;
            ForegroundColorSpan targetColorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            spannableString.setSpan(targetColorSpan,start,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //处理被点击数据中的用户名
            ClickableSpan targetClick = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击的用户："+data.getTargetusername(),Toast.LENGTH_SHORT).show();
                    openUserInfoActivity(data.getTargetuid());
                }
            };
            //设置点击事件
            spannableString.setSpan(targetClick,start,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            txtContent.setText(spannableString);
        }else{
            //当前是评论数据 AA:XXX
            String content = data.getDiscussusername()+":"+data.getContent();
            SpannableString spannableString = new SpannableString(content);
            int start=0,end=0;
            end = data.getDiscussusername().length()+1;
            //设置用户名的颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3F739C"));
            spannableString.setSpan(colorSpan,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //对当前的用户名添加点击事件
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext,"点击了:"+data.getDiscussusername(),Toast.LENGTH_SHORT).show();
                    openUserInfoActivity(data.getDiscussuid());
                }
            };
            //设置点击事件
            spannableString.setSpan(clickableSpan,start,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //把带有样式的字符内容显示到文本框中
            txtContent.setText(spannableString);
        }
        //设置txtContent文本响应点击事件
        txtContent.setMovementMethod(LinkMovementMethod.getInstance());

        //处理回复评论的条目点击
        txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickHandler != null){
                    // trendsid,discussid,targettype,targetuid
                    itemClickHandler.itemClick(TrendsFragment.TYPE_REPLY,trendsid,data.getId(),2,data.getDiscussuid());
                }
            }
        });
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
