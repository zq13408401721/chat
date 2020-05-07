package com.mychat.activitys.own;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.mychat.R;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.own.UserConstact;
import com.mychat.module.bean.DetailsUpdateBean;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.persenters.own.DetailsPersenter;
import com.mychat.utils.SpUtils;
import com.mychat.utils.UserUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    UserDetailsBean userDetailsBean;

    String editContent; //当前修改的内容
    TextView curTxt; //当前操作的信息文本

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
        userDetailsBean = result;
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

    @OnClick({R.id.item_nickname,R.id.item_sex,R.id.item_age,R.id.item_sign})
    public void onViewClicked(View view){
        if(userDetailsBean == null) return;
        switch (view.getId()){
            case R.id.item_nickname:
                curTxt = ((TextView)nicknameLayout.findViewById(R.id.txt_word));
                openDialog("修改昵称", (String) userDetailsBean.getData().getNickname(),"nickname");
                break;
            case R.id.item_sex:
                curTxt = ((TextView)sexLayout.findViewById(R.id.txt_word));
                openDialog("修改性别", UserUtils.parseSex(userDetailsBean.getData().getSex()),"sex");
                break;
            case R.id.item_age:
                curTxt = ((TextView)ageLayout.findViewById(R.id.txt_word));
                openDialog("修改年龄", (String) userDetailsBean.getData().getAge(),"age");
                break;
            case R.id.item_sign:
                curTxt = ((TextView)signLayout.findViewById(R.id.txt_word));
                openDialog("修改签名", (String) userDetailsBean.getData().getSign(),"sign");
                break;
        }
    }

    /**
     * 打开修改信息的弹框
     * title当前修改的内容
     * value当前要修改的数据值
     */
    private void openDialog(String title,String value,String key){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_edit_detailsinfo, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                //.setCancelable(true)
                .create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView txtTitle = view.findViewById(R.id.txt_edit_title);
        EditText editWord = view.findViewById(R.id.edit_word);
        LinearLayout layout_sex = view.findViewById(R.id.layout_sex);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if(key.equals("sex")){
            layout_sex.setVisibility(View.VISIBLE);
            editWord.setVisibility(View.GONE);
        }else{
            layout_sex.setVisibility(View.GONE);
            editWord.setVisibility(View.VISIBLE);
        }
        txtTitle.setText(title);
        if(!TextUtils.isEmpty(value)){
            editWord.setHint(value);
        }else{
            editWord.setHint("请输入内容");
        }
        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key.equals("sex")){
                    int curId = radioGroup.getCheckedRadioButtonId();
                    if(curId < 0){
                        Toast.makeText(context,"未输入任何内容",Toast.LENGTH_SHORT).show();
                    }else{
                        Map<String,String> map = new HashMap<>();
                        if(curId == R.id.radioBtn_man){
                            map.put(key,"1");
                            editContent = "男";
                        }else{
                            map.put(key,"2");
                            editContent = "女";
                        }
                        //调用后台接口，更新用户信息
                        persenter.updateDetails(map);
                        alertDialog.dismiss();
                    }
                }else{
                    editContent = editWord.getText().toString();
                    if(!TextUtils.isEmpty(editContent)){
                        Map<String,String> map = new HashMap<>();
                        map.put(key,editContent);
                        //调用后台接口，更新用户信息
                        persenter.updateDetails(map);
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(context,"未输入任何内容",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 更新用户信息返回
     * @param result
     */
    @Override
    public void updateDetailsReturn(DetailsUpdateBean result) {
        if (result.getErr() == 200){
            if(curTxt != null && !TextUtils.isEmpty(editContent)){
                curTxt.setText(editContent);
            }
        }
    }
}
