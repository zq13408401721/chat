package com.mychat.base;


import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * p层的实现基类 实现特征：
 * 1.关联和解绑V层
 * 2.对网络请求对象进行背压式处理CompositeDisposable
 *
 */
public abstract class BasePersenter<V extends IBaseView> implements IBasePersenter<V> {

    protected V mView;
    //对V层的进行弱引用
    WeakReference<V> weakReference;

    //rxjava2 背压式网络请求处理
    CompositeDisposable compositeDisposable;

    @Override
    public void attachView(V view) {
        weakReference = new WeakReference<>(view);
        mView = weakReference.get();
    }


    /**
     * 把当前业务下的网络请求对象添加到compositedisposable
     * @param disposable
     */
    public void addSubscribe(Disposable disposable){
        if(compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(disposable);
    }

    /**
     * 在界面关闭时候移除网络请求对象
     */
    public void unSubscribe(){
        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
