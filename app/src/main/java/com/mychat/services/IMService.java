package com.mychat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.mychat.BuildConfig;
import com.mychat.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
public class IMService extends Service {

    //websocket的基础地址
    private static String url;
    private WebSocket webSocket;

    //和外面通信的接口
    IMBinder imBinder;

    //service->activity的回调接口
    IMessage iMessage;

    //初始化基础地址
    static {
        if(BuildConfig.DEBUG){
           url = "ws://192.168.4.159:9001/webserver";   //debug模式用本地的服务
        }else{
            url = "ws://cdwan.cn:9001/webserver";  //发布正版 用外网正式的服务
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imBinder = new IMBinder();
        //初始化之前确定之前是否已经登录过，是否有token的缓存存在
        String token = SpUtils.getInstance().getString("token");
        if(!TextUtils.isEmpty(token)){
            init(token);
        }
    }

    /**
     * 初始化连接对象
     */
    private void init(String token){
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
                .header("x-token",token)
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
         * @param ws
         * @param response
         */
        @Override
        public void onOpen(WebSocket ws, Response response) {
            super.onOpen(ws, response);
            webSocket = ws;
        }

        /**
         * 接收服务器的返回数据  以String形式接收
         * @param webSocket
         * @param text
         */
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            try {
                JSONObject json = new JSONObject(text);
                if(json.has("event")){
                    String action = json.getString("event");
                    //长连接中的消息数据
                    String data = json.getString("data");
                    switch (action){
                        case "join":
                            break;
                        case "talk": // 接收到聊天消息推送到activity或fragment
                            pushTalkMsg(data);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


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


    /**
     * 提供IMBinder给外面使用
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return imBinder;
    }

    /**
     * 定义IMBinder和activity交互
     */
    public class IMBinder extends Binder {

        //有用户加入进来的接口方法 参数是json结构的字符串
        public void sendMsg(String jsonMsg){
            if(webSocket != null){
                webSocket.send(jsonMsg);
            }
        }

        //定义一个接收接口回调方法
        public void addIMessageListener(IMessage im){
            iMessage = im;
        }

    }

    /*******************定义接口回调实现Service通知activity**********************/

    public interface IMessage{
        //推送消息
        void pushMsg(String msg);
    }

    /**
     * 实现聊天消息的推送
     * @param string
     */
    private void pushTalkMsg(String string){
        if(iMessage != null){
            iMessage.pushMsg(string);
        }
    }


    /*************定义广播实现Service通知Activity**********************/


}
