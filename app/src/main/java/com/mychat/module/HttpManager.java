package com.mychat.module;

import android.util.Log;


import com.mychat.common.Constant;
import com.mychat.module.apis.ChatApi;
import com.mychat.module.apis.UploadApi;
import com.mychat.utils.SpUtils;
import com.mychat.utils.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 封装http网络请求
 */
public class HttpManager {

    /**
     * 上传接口
     */
    UploadApi uploadApi;

    /**
     * 聊天接口
     */
    ChatApi chatApi;


    private static volatile HttpManager instance;
    public static HttpManager getInstance(){
        if(instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取retrofit网络请求的类
     * @param url
     * @return
     */
    private static Retrofit getRetrofit(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkhttpclient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    private static OkHttpClient getOkhttpclient(){
        File file = new File(Constant.PATH_CACHE); //本地的缓存文件
        Cache cache = new Cache(file,100*1024*1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new NetworkInterceptor())
                .cache(cache)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(cookieJar)
                .build();
        return client;
    }

    /**
     * 获取wanApi
     * @return
     */
    public UploadApi getUploadApi(){
        if(uploadApi == null){
            uploadApi = getRetrofit(Constant.BASE_UPLOAD_IMAGE_URL).create(UploadApi.class);
        }
        return uploadApi;
    }

    /**
     * 聊天的api接口
     * @return
     */
    public ChatApi getChatApi(){
        if(chatApi == null){
            chatApi = getRetrofit(Constant.BASE_CHAT_URL).create(ChatApi.class);
        }
        return chatApi;
    }

    /**
     * 抽取获取对应网络请求api的接口
     * @param url
     * @param cls
     * @param <T>
     * @return
     */
    private synchronized <T> T getApi(String url,Class<T> cls){
        return getRetrofit(url).create(cls);
    }



    static class HeaderInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Connection","keep-alive")
                    .addHeader("Client-Type","ANDROID")
                    .addHeader("x-token", SpUtils.getInstance().getString("token"))
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 网络请求的日志 报文
     */
    static class LoggingInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.i("interceptor",String.format("Sending request %s on %s%n%s",request.url(),chain.connection(),request.headers()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.i("Received:",String.format("Received response for %s in %.1fms%n%s",response.request().url(),(t2-t1)/1e6d,response.headers()));
            if(response.header("session_id") != null){
                //Constant.session_id = response.header("session_id");
            }
            return response;
        }
    }

    /**
     * 网络拦截器封装
     */
    static class NetworkInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!SystemUtils.checkNetWork()){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            //通过判断网络连接是否存在获取本地或者服务器的数据
            if(!SystemUtils.checkNetWork()){
                int maxAge = 0;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control","public ,max-age="+maxAge).build();
            }else{
                int maxStale = 60*60*24*28; //设置缓存数据的保存时间
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control","public, onlyif-cached, max-stale="+maxStale).build();
            }
        }
    }


    /**
     * Cookie设置
     */
    private static CookieJar cookieJar = new CookieJar() {

        private final Map<String, List<Cookie>> cookieMap = new HashMap<String,List<Cookie>>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            String host = url.host();
            List<Cookie> cookieList = cookieMap.get(host);
            if(cookieList != null){
                cookieMap.remove(host);
            }
            cookieMap.put(host,cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookieList = cookieMap.get(url.host());
            return cookieList != null ? cookieList : new ArrayList<Cookie>();
        }
    };

}
