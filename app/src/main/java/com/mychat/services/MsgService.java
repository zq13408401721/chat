package com.mychat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.mychat.MyMessageController;
import com.mychat.module.msgs.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过aidl接口通信的类
 */
public class MsgService extends Service {

    private List<Msg> list;

    /**
     * 用来实现Service和外部通信的接口方法类
     */
    MyMessageController.Stub stub = new MyMessageController.Stub() {
        @Override
        public List<Msg> msgList() throws RemoteException {
            return list;
        }

        @Override
        public void addMsgInOut(Msg msg) throws RemoteException {
            if(msg != null){
                list.add(msg);
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list =  new ArrayList<>();
    }
}
