package com.mychat.module.apis;

import com.mychat.module.bean.UserDetailsBean;
import com.mychat.module.bean.UserInfoBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ChatApi {

    @POST("user/login")
    @FormUrlEncoded
    Flowable<UserInfoBean> login(@Field("username") String username,@Field("passowrd") String password);

    //用户的详情信息
    @POST("user/details")
    @FormUrlEncoded
    Flowable<UserDetailsBean> getUserDetails();


}
