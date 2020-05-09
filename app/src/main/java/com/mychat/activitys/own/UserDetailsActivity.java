package com.mychat.activitys.own;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mychat.R;
import com.mychat.apps.GlideEngine;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.own.UserConstact;
import com.mychat.module.bean.DetailsUpdateBean;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.persenters.own.DetailsPersenter;
import com.mychat.utils.SpUtils;
import com.mychat.utils.UserUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailsActivity extends BaseActivity<UserConstact.DetailsPersenter> implements UserConstact.DetailsView {

    public static final int  CODE_HEADCROPACTIVITY = 200;

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
    @BindView(R.id.layout_head)
    ConstraintLayout layoutHead;

    UserDetailsBean userDetailsBean;

    String editContent; //当前修改的内容
    TextView curTxt; //当前操作的信息文本
    String newAvater; //当前最新的头像地址

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

    @OnClick({R.id.item_nickname,R.id.item_sex,R.id.item_age,R.id.item_sign,R.id.layout_head})
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
            case R.id.layout_head:
                //更新头像
                openLocalPhoto();
                break;
        }
    }

    /**
     * 打开本地相机相册选取图片的功能
     */
    private void openLocalPhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .imageSpanCount(4)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .rotateEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

    /**
     * 处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // onResult Callback  本地相机相册图片选取的结果
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if(selectList.size() == 0) return;
                //打开图片剪切界面 图片上传
                Intent intent = new Intent(UserDetailsActivity.this,HeadCropActivity.class);
                intent.putExtra("img_path",selectList.get(0).getPath());
                startActivityForResult(intent,CODE_HEADCROPACTIVITY);
                break;
            case CODE_HEADCROPACTIVITY:
                //处理图片剪切页面图片上传结果的地址
                String imgUrl = data.getStringExtra("imgUrl");
                Map<String,String> map = new HashMap<>();
                map.put("avater",imgUrl);
                persenter.updateDetails(map);
                //更新当前页面的头像
                break;
            default:
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

        /**
         * 关闭编辑框的监听
         */
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //editContent = null;
                //curTxt = null;
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
                editContent = null;
                curTxt = null;
            }
            if(!TextUtils.isEmpty(newAvater)){
                Glide.with(context).load(newAvater).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        newAvater = null;
                        return false;
                    }
                }).into(imgHead);
            }

        }
    }
}
