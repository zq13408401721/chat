package com.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class HandlerActivity extends AppCompatActivity {


    TextView txtView;
    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Button btn_click = findViewById(R.id.btn_click);
        txtView = findViewById(R.id.txt_msg);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask();
            }
        });
        myHandler = new MyHandler(this);
    }

    /**
     *
     * @param value
     */
    public void setTxtMsg(String value){
        txtView.setText(value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解决handler引起的内存泄漏
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 子线程的任务
     */
    private void asyncTask(){
        final int[] a = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    a[0]++;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = a[0];
                    myHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 内部类默认持有外部类的应用
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    txtView.setText(String.valueOf(msg.obj));
                    break;
            }
        }
    };

    /**
     * 用静态内部类解决handler引起的内存泄漏
     */
    static class MyHandler extends Handler{

        //对外部类的弱引用
        WeakReference<HandlerActivity> weakReference;

        public MyHandler(HandlerActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //通过弱引用调用对应的activity的方法
                    if(weakReference.get() != null){
                        weakReference.get().setTxtMsg(String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    }



}
