package com.mychat.module.apis;

import com.mychat.module.bean.DetailsUpdateBean;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.module.bean.UserInfoBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatApi {

    @POST("user/login")
    @FormUrlEncoded
    Flowable<UserInfoBean> login(@Field("username") String username,@Field("password") String password);

    //用户的详情信息
    @GET("user/details")
    Flowable<UserDetailsBean> getUserDetails();

    /**
     * 更新用户信息
     * @param map
     * @return
     */
    @POST("user/updateinfo")
    @FormUrlEncoded
    Flowable<DetailsUpdateBean> updateUserDetails(@FieldMap Map<String,String> map);


}
