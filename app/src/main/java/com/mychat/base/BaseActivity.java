package com.mychat.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 定义Activity的基类，用来实现mvp中V层的基础功能
 * MVP框架中BaseActivity类应该具备如下特征：
 * 1.应该包含用来处理网络数据逻辑的P层
 * 2.应该具备界面初始化的方法initView
 * 3.应该具备数据初始化的方法initData
 * 4.获取当前应该显示的xml布局页面
 * 5.生命周期结束的时候解绑p层的关联
 *
 */
public abstract class BaseActivity<P extends IBasePersenter> extends AppCompatActivity implements IBaseView {

    protected P persenter;
    Unbinder unbinder;
    protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        context = this;
        unbinder = ButterKnife.bind(this);
        initView();
        persenter = createPersenter();
        if(persenter != null){
            persenter.attachView(this);
        }
        initData();
    }

    /**
     * 通过模板的设计模式，定义需要处理的方法
     */
    protected abstract int getLayout();
    //初始化界面的操作
    protected abstract void initView();
    //初始化数据
    protected abstract void initData();
    //创建p的方法
    protected abstract P createPersenter();

    /**
     * 用来显示提示信息
     * @param msg
     */
    @Override
    public void showTips(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    //添加友盟的统计

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }

    //友盟统计
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }

    /**
     * activity移除的时候解绑persenter和v层
     * 解绑当前activity的butterknife
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(persenter != null){
            persenter.detachView();
        }
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
