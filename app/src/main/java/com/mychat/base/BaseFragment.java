package com.mychat.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础Fragment
 *  * MVP框架中BaseFragment类应该具备如下特征：
 *  * 1.应该包含用来处理网络数据逻辑的P层
 *  * 2.应该具备界面初始化的方法initView
 *  * 3.应该具备数据初始化的方法initData
 *  * 4.获取当前应该显示的xml布局页面
 *  * 5.生命周期结束的时候解绑p层的关联
 */
public abstract class BaseFragment<P extends IBasePersenter> extends Fragment implements IBaseView {

    protected P persenter;
    protected Context context;
    protected Activity activity;
    Unbinder unbinder;

    public boolean isVisible;  //当前fragment是否显示

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(),null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        activity = getActivity();
        unbinder = ButterKnife.bind(this,view);
        persenter = createPersenter();
        if(persenter != null) persenter.attachView(this);
        initView();
        initData();
    }

    //获取子类的布局id
    protected abstract int getLayout();
    //初始化界面
    protected abstract void initView();
    //初始化数据
    protected abstract void initData();
    //创建p层
    protected abstract P createPersenter();



    @Override
    public void showTips(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(persenter != null){
            persenter.detachView();
        }
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
