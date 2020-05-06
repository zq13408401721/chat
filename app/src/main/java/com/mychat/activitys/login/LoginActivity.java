package com.mychat.activitys.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mychat.R;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.login.LoginConstract;
import com.mychat.module.bean.UserInfoBean;
import com.mychat.persenters.login.LoginPersenter;
import com.mychat.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginConstract.Persenter> implements LoginConstract.View {
    @BindView(R.id.edit_username)
    EditText editUsername;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.layout_edit)
    LinearLayout layoutEdit;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected LoginConstract.Persenter createPersenter() {
        return new LoginPersenter();
    }

    /**
     * 登录结果返回
     * @param result
     */
    @Override
    public void loginReturn(UserInfoBean result) {
        if(result.getErr() == 200){
            //把接收到的登录成功返回的值保存到sp
            SpUtils.getInstance().setValue("token",result.getData().getToken());
            SpUtils.getInstance().setValue("username",result.getData().getUsername());
            SpUtils.getInstance().setValue("avater",result.getData().getAvater());
            SpUtils.getInstance().setValue("phone",result.getData().getPhone());
            finish();
        }else{
            Toast.makeText(context,result.getErrmsg(),Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.txt_login)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.txt_login:
                login();
                break;
        }
    }

    //登录
    private void login(){
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(context,"请输入正确的用户名或密码",Toast.LENGTH_SHORT).show();
            return;
        }
        //正常的业务逻辑下 password
        persenter.login(username,password);
    }
}
