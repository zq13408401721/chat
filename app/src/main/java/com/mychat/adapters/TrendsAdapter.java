package com.mychat.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mychat.R;
import com.mychat.base.BaseAdapter;
import com.mychat.module.bean.TrendsBean;

import java.util.List;

public class TrendsAdapter extends BaseAdapter {

    public TrendsAdapter(List<TrendsBean.DataBean> list, Context context){
        super(list,context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_trends_item;
    }

    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        //当前的动态数据
        TrendsBean.DataBean data = (TrendsBean.DataBean) o;
        //更新头像
        ImageView imgHead = (ImageView) holder.getView(R.id.img_head);
        if(!TextUtils.isEmpty((CharSequence) data.getAvater())){
            RequestOptions options = RequestOptions.circleCropTransform();
            options.placeholder(R.mipmap.ic_launcher);
            Glide.with(mContext).load(data.getAvater()).apply(options).into(imgHead);
        }
        //更新用户昵称
        TextView txtUserName = (TextView) holder.getView(R.id.txt_username);
        if(!TextUtils.isEmpty(data.getUsername())){
            txtUserName.setText(data.getUsername());
        }
        //显示创建动态的时间
        TextView txtTime = (TextView) holder.getView(R.id.txt_time);
        txtTime.setText(String.valueOf(txtTime));
    }
}
