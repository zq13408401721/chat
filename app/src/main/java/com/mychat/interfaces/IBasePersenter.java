package com.mychat.interfaces;

/**
 * 定义一个关联V层的接口
 * 定义一个V层接口的泛型类
 */
public interface IBasePersenter<V extends IBaseView> {

    //关联V层
    void attachView(V view);
    //取消V层的关联
    void detachView();

}
