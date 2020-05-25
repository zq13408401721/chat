package com.mychat.services;

import com.mychat.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 使用OKHttp协议实现websocket长连接
 *
 */
public class IMService {

    //websocket的基础地址
    private static String url;

    //初始化基础地址
    static {
        if(BuildConfig.DEBUG){
           url = "ws://192.168.4.158:9001/webserver";   //debug模式用本地的服务
        }else{
            url = "ws://cdwan.cn:9001/webserver";  //发布正版 用外网正式的服务
        }
    }

    /**
     * 初始化连接对象
     */
    public void init(){
        //okhttp对象初始化
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        //请求的初始化
        Request request = new Request.Builder()
                .url(url)
                .build();
        //实例化监听对象
        WSListener listener = new WSListener();
        client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();
    }

    /**
     *webscoket监听
     */
    final class WSListener extends WebSocketListener{

        /**
         * websocket连接状态打开
         * @param webSocket
         * @param response
         */
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

        /**
         * 接收服务器的返回数据  以String形式接收
         * @param webSocket
         * @param text
         */
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
        }

        /**
         *接收服务器返回处理  以ByteString形式接收
         * @param webSocket
         * @param bytes
         */
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        /**
         * 正在关闭websocket连接
         * @param webSocket
         * @param code
         * @param reason
         */
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        /**
         * 断开websocket连接
         * @param webSocket
         * @param code
         * @param reason
         */
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        /**
         * 连接失败的监听
         * @param webSocket
         * @param t
         * @param response
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
        }
    }

}
