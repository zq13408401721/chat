package com.mychat.activitys.own;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.mychat.R;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.own.UserConstact;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.persenters.own.DetailsPersenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends BaseActivity<UserConstact.DetailsPersenter> implements UserConstact.DetailsView {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_head)
    ImageView imgHead;

    @BindView(R.id.item_nickname)
    ConstraintLayout nicknameLayout;
    @BindView(R.id.item_sex)
    ConstraintLayout sexLayout;
    @BindView(R.id.item_age)
    ConstraintLayout ageLayout;
    @BindView(R.id.item_sign)
    ConstraintLayout signLayout;
    @BindView(R.id.item_level)
    ConstraintLayout levelLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_details;
    }

    @Override
    protected void initView() {
        ((TextView)nicknameLayout.findViewById(R.id.txt_name)).setText("昵称");
        ((TextView)sexLayout.findViewById(R.id.txt_name)).setText("性别");
        ((TextView)ageLayout.findViewById(R.id.txt_name)).setText("年龄");
        ((TextView)signLayout.findViewById(R.id.txt_name)).setText("签名");
        ((TextView)levelLayout.findViewById(R.id.txt_name)).setText("等级");
    }

    @Override
    protected void initData() {
        //获取用户的详情信息
        persenter.getDetails();
    }

    @Override
    protected UserConstact.DetailsPersenter createPersenter() {
        return new DetailsPersenter();
    }

    @Override
    public void getDetailsReturn(UserDetailsBean result) {
        //头像刷新
        if(result.getData().getAvater() != null && !TextUtils.isEmpty((CharSequence) result.getData().getAvater())){
            Glide.with(context).load(result.getData().getAvater()).into(imgHead);
        }
        //显示昵称
        if(result.getData().getNickname() != null && !TextUtils.isEmpty((CharSequence) result.getData().getNickname())){
            ((TextView)nicknameLayout.findViewById(R.id.txt_word)).setText((String) result.getData().getNickname());
        }else{
            ((TextView)nicknameLayout.findViewById(R.id.txt_word)).setText("请输入");
        }
        //显示性别
        if(result.getData().getSex() == 0){
            ((TextView)sexLayout.findViewById(R.id.txt_word)).setText("无");
        }else if(result.getData().getSex() == 1){
            ((TextView)sexLayout.findViewById(R.id.txt_word)).setText("男");
        }else{
            ((TextView)sexLayout.findViewById(R.id.txt_word)).setText("女");
        }
        //签名
        if(result.getData().getSign() != null && !TextUtils.isEmpty((CharSequence) result.getData().getSign())){
            ((TextView)signLayout.findViewById(R.id.txt_word)).setText((String) result.getData().getSign());
        }else{
            ((TextView)signLayout.findViewById(R.id.txt_word)).setText("请输入");
        }
    }


}
