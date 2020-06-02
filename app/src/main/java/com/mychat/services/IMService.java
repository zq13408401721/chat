package com.mychat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mychat.BuildConfig;
import com.mychat.ChatActivity;
import com.mychat.IndexActivity;
import com.mychat.common.Constant;
import com.mychat.fragments.trends.TrendsFragment;
import com.mychat.utils.ActivityUtils;
import com.mychat.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
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

    private static final int RETRY_TIME = 5*1000;  //心跳间隔时间

    //websocket的基础地址
    private static String url;
    private WebSocket webSocket;

    //和外面通信的接口
    IMBinder imBinder;

    //service->activity的回调接口
    IMessage iMessage;
    //本地广播的管理类
    LocalBroadcastManager localBroadcastManager;

    String token;

    //websocket连接
    OkHttpClient client;
    Request request;
    WSListener listener;
    //心跳定时器
    Handler retryHandler = new Handler();
    long retrytime = 0;

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
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        imBinder = new IMBinder();
        //初始化之前确定之前是否已经登录过，是否有token的缓存存在
        token = SpUtils.getInstance().getString("token");
        if(!TextUtils.isEmpty(token)){
            init();
        }
    }

    /**
     * 初始化连接对象
     */
    private void init(){
        //okhttp对象初始化
        client = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        //请求的初始化
        request = new Request.Builder()
                .url(url)
                .header("x-token",token)
                .build();
        //实例化监听对象
        listener = new WSListener();
        //关联websocket的连接
        initConnect();
        //启动心跳包
        retryHandler.postDelayed(retryRunable,RETRY_TIME);
    }

    /**
     * 关联websocket的连接
     */
    private void initWebSocket(){
        client.newWebSocket(request,listener);
        client.dispatcher().executorService().shutdown();
    }

    /**
     * 在子线程重完成websocket的创建和关联
     */
    private void initConnect(){
        new ConnectThreadClass().start();
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
            joinUser(token);
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
                        case Constant.ACTION_EVENT_TALK: // 接收到聊天消息推送到activity或fragment
                            // pushTalkMsg(data);  //接口回调实现通信
                            pushBroadCastMsg(data);  //广播实现通信
                            break;
                        case Constant.ACTION_EVENT_PRAISE:  //点赞
                        case Constant.ACTION_EVENT_DISCUSS:  //评论
                        case Constant.ACTION_EVENT_REPLY:  //回复
                            //ActivityUtils.isForeground(IndexActivity.this,"IndexActivity");
                            pushTrendsMsg(action,data);
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

    /*******************注册用户到websocket*************************/
    private void joinUser(String token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event","join");
            jsonObject.put("data",token);
            webSocket.send(jsonObject.toString());
        }catch (JSONException e){
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
        //通过接口回调的形式通知activity
        if(iMessage != null){
            iMessage.pushMsg(string);
        }
    }


    /*************定义广播实现Service通知Activity**********************/
    private void pushBroadCastMsg(String string){
        //通过广播通知activity
        Intent intent = new Intent(ChatActivity.CHAT_BROADCAST_ACTION);
        intent.putExtra("data",string);
        localBroadcastManager.sendBroadcast(intent);

    }

    /*********************动态推送操作*****************/
    private void pushTrendsMsg(String action,String msg){
        /*if(getApplicationContext().getWindow().getDecorView().getVisibility() == View.VISIBLE){

        }*/
        //判断当前的inexactivity是否是显示状态
        if(ActivityUtils.isForeground(getBaseContext(),"com.mychat.IndexActivity")){
            if(IndexActivity.isShowTrendsFragment()){
                if(action.equals(Constant.ACTION_EVENT_PRAISE)){   //接收到点赞的推送消息

                }else if(action.equals(Constant.ACTION_EVENT_DISCUSS)){   //接收到评论的推送消息

                }else if(action.equals(Constant.ACTION_EVENT_REPLY)){    //接收到后恢复的推送消息

                }
                Intent intent = new Intent(TrendsFragment.TRENDS_BROADCAST_ACTION);
                intent.putExtra("data",msg);
                localBroadcastManager.sendBroadcast(intent);

            }else{
                //发通知
            }
        }else{
            // 发通知
        }
    }

    /**
     * 发通知
     */
    private void sendNotification(){

    }


    /************************连接的子线程********************/

    Runnable retryRunable = new Runnable() {
        @Override
        public void run() {
            //判断当前的系统时间是否达到心跳时间
            if(System.currentTimeMillis() - retrytime > RETRY_TIME){
                if(webSocket != null){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("event","retry");
                        jsonObject.put("data","1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean res = webSocket.send(jsonObject.toString());
                    //false连接已经断开
                    if(!res){
                        retryHandler.removeCallbacks(retryRunable);
                        webSocket.cancel();
                        new ConnectThreadClass().start();
                    }
                    retrytime = System.currentTimeMillis();
                }
            }
            retryHandler.postDelayed(retryRunable,RETRY_TIME);
        }
    };

    class ConnectThreadClass extends Thread{
        @Override
        public void run() {
            super.run();
            initWebSocket();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webSocket != null){
            webSocket.close(1000,null);
        }
    }
}
