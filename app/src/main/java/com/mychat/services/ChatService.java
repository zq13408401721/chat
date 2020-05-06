package com.mychat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.http.Url;

/**
 * 聊天服务类
 * 使用okhttp实现websocket
 * 通过Binder实现Service与activity之间的通信
 *
 */
public class ChatService extends Service {

    String websocketUrl = "http://127.0.0.1:9001";

    private OkHttpClient client;
    private Request request;
    private WebSocket mWebSocket;

    //回调接口对象
    DataListener dataListener;

    private MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();
        request = new Request.Builder().url(websocketUrl).build();
        client.newWebSocket(request,createListener());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    /**
     * 数据连接监听
     * @return
     */
    private WebSocketListener createListener(){
        return new WebSocketListener() {

            /**
             * 连接接上
             * @param webSocket
             * @param response
             */
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mWebSocket = webSocket;
            }

            /**
             * 接收到字符串消息
             * @param webSocket
             * @param text
             */
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            /**
             * 接收到消息以ByteString格式传递
             * @param webSocket
             * @param bytes
             */
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            /**
             * 连接正在关闭
             * @param webSocket
             * @param code
             * @param reason
             */
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            /**
             * 连接关闭
             * @param webSocket
             * @param code
             * @param reason
             */
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            /**
             * 连接失败
             * @param webSocket
             * @param t
             * @param response
             */
            @Override
            public void onFailure(WebSocket webSocket, Throwable t,Response response) {
                super.onFailure(webSocket, t, response);
            }
        };
    }

    /********************Binder************************/
    class MyBinder extends Binder{

        //获取聊天服务
        ChatService getChatService(){
            return ChatService.this;
        }

        void sendMsg(String data){
            if(mWebSocket != null){
                mWebSocket.send(data);
            }
        }

    }

    /**
     * 定义一个servie数据变化的接口
     */
    public interface DataListener{
        //监听数据
        void handleMsg(String data);
        //连接关闭
        void clossListener();
    }

    /**
     * 设置数据监听接口
     * @param listener
     */
    public void addDataListener(DataListener listener){
        dataListener = listener;
    }

}
