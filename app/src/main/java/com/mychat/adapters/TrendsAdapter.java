package com.mychat.adapters;

import android.content.Context;
import android.text.TextUtils;
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

        //显示点赞的所有用户

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
}
