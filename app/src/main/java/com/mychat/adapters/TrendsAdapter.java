package com.mychat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mychat.R;
import com.mychat.base.BaseAdapter;
import com.mychat.fragments.trends.TrendsFragment;
import com.mychat.module.bean.TrendsBean;

import java.util.Arrays;
import java.util.List;

public class TrendsAdapter extends BaseAdapter {

    public TrendsAdapter(List<TrendsBean.DataBean> list, Context context){
        super(list,context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_trends_item;
    }


    /**
     * 设置嵌套适配器
     * @param itemClickHandler
     */
    public void setDiscussItemClickHandler(ItemClickHandler itemClickHandler){
        this.itemClickHandler = itemClickHandler;
    }

    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        //当前的动态数据
        TrendsBean.DataBean data = (TrendsBean.DataBean) o;
        ConstraintLayout layoutUser = (ConstraintLayout) holder.getView(R.id.layout_userinfo);
        //更新头像
        ImageView imgHead = (ImageView) layoutUser.findViewById(R.id.img_head);
        if(!TextUtils.isEmpty((CharSequence) data.getAvater())){
            RequestOptions options = RequestOptions.circleCropTransform();
            options.placeholder(R.mipmap.ic_launcher);
            Glide.with(mContext).load(data.getAvater()).apply(options).into(imgHead);
        }
        //更新用户昵称
        TextView txtNickname = (TextView) layoutUser.findViewById(R.id.txt_nickname);
        if(!TextUtils.isEmpty(data.getUsername())){
            txtNickname.setText(data.getUsername());
        }
        //显示创建动态的时间
        TextView txtTime = (TextView) holder.getView(R.id.txt_time);
        txtTime.setText(String.valueOf(data.getTime()));
        //内容显示
        TextView txtContent = (TextView) holder.getView(R.id.txt_content);
        if(!TextUtils.isEmpty(data.getContent())){
            txtContent.setText(data.getContent());
        }
        //图片内容
        RecyclerView recyImgs = (RecyclerView) holder.getView(R.id.recy_imgs);
        if(!TextUtils.isEmpty(data.getResources())){
                recyImgs.setVisibility(View.VISIBLE);
                String[] imgs = data.getResources().split("$");
                List<String> list = Arrays.asList(imgs);
                recyImgs.setLayoutManager(new GridLayoutManager(mContext,3));
                TrendsImgAdapter trendsImgAdapter = new TrendsImgAdapter(list,mContext);
                recyImgs.setAdapter(trendsImgAdapter);
        }else{
            recyImgs.setVisibility(View.GONE);
        }

        //显示点赞和评论的数量
        TextView txtDiscuss = (TextView) holder.getView(R.id.txt_discuss);
        txtDiscuss.setText(String.valueOf(data.getDiscussNum()));
        txtDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickHandler != null){
                    itemClickHandler.itemClick(TrendsFragment.TYPE_DISCUSS,data.getId());
                }
            }
        });

        //显示点赞的所有用户
        TextView txtPraise = (TextView) holder.getView(R.id.txt_praise);
        txtPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickHandler != null){
                    //点赞
                    itemClickHandler.itemClick(TrendsFragment.TYPE_PRAISE,data.getId(),0);
                }
            }
        });
        //点赞的数据显示
        TextView txtPraiseUser = (TextView) holder.getView(R.id.txt_praise_users);
        if(data.getPraise().size() > 0){
            txtPraiseUser.setVisibility(View.VISIBLE);
            prasePraiseData(txtPraiseUser,data.getPraise());
        }else{
            txtPraiseUser.setVisibility(View.GONE);
        }

        //显示评论回复的信息
        RecyclerView recy_discuss = (RecyclerView) holder.getView(R.id.recy_discuss);
        if(data.getDiscuss().size() > 0){
            recy_discuss.setVisibility(View.VISIBLE);
            recy_discuss.setLayoutManager(new LinearLayoutManager(mContext));
            TrendsDiscussAdapter trendsDiscussAdapter = new TrendsDiscussAdapter(data.getDiscuss(),mContext);
            trendsDiscussAdapter.trendsid = data.getId(); //当前动态的id给评论回复的适配
            recy_discuss.setAdapter(trendsDiscussAdapter);
            //把外层实现的点击接口传入嵌套适配器
            trendsDiscussAdapter.setOnItemClickHandler(itemClickHandler);
        }else{
            recy_discuss.setVisibility(View.GONE);
        }
    }

    /**
     * 解析显示点赞的用户信息
     * 点赞用户信息默认显示前10个用户，超过10个更多，用户名之间用逗号隔开
     *
     */
    private void prasePraiseData(TextView txtPraiseUser, List<TrendsBean.DataBean.PraiseBean> list){
        String content = "[icon]"; //用户信息点赞按钮的图片
        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        //添加图标到富文本中显示
        Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.icon_praise_user);
        drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(),  drawable1.getIntrinsicHeight());
        ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(imageSpan1, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //文字颜色
        ForegroundColorSpan clr = new ForegroundColorSpan(Color.parseColor("#1296db"));
        //把点赞中所有的用户名取出来显示到富文本中
        for(int i=0; i<list.size(); i++){
            if(i >= 10){
                SpannableString spannableString = new SpannableString("等更多");
                spannableString.setSpan(clr,0,3,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                spannable.append(spannableString);
            }else{
                String username;
                //添加逗号
                if(i <= list.size()-1){
                    username = list.get(i).getUsername();
                }else{
                    username = list.get(i).getUsername()+",";
                }
                //用户显示和颜色处理
                SpannableString spannableString = new SpannableString(username);
                spannableString.setSpan(clr,0,username.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                spannable.append(spannableString);
            }
        }
        txtPraiseUser.setText(spannable);
    }
}
