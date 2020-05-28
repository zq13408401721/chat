package com.mychat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.ConnectionService;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mychat.module.msgs.Msg;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AidlActivity extends AppCompatActivity {

    @BindView(R.id.btn_msglist)
    Button btnMsglist;
    @BindView(R.id.btn_addmsg)
    Button btnAddmsg;
    @BindView(R.id.txt_msg)
    TextView txtMsg;


    MyMessageController msgController;
    boolean isConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);
        bindService();
    }

    private void bindService(){
        Intent intent = new Intent();
        intent.setAction("com.mychat.msgservice");
        intent.setPackage("com.mychat");
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msgController = MyMessageController.Stub.asInterface(service);
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
        }
    };



    @OnClick({R.id.btn_msglist, R.id.btn_addmsg})
    public void onViewClicked(View view) {
        if(!isConnect){
            Toast.makeText(this, "服务连接已经断开", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_msglist:
                getMsgList();
                break;
            case R.id.btn_addmsg:
                addMsgData();
                break;
        }
    }

    /**
     * 通过aidl往MsgService服务中添加数据
     */
    private void addMsgData(){
        Date date = new Date();
        Msg msg = new Msg("msg1",date.toString());
        try {
            msgController.addMsgInOut(msg);
        }catch (RemoteException e){
            Toast.makeText(this, "访问接口异常"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取msg数据显示到txt中
     */
    private void getMsgList(){
        try {
            List<Msg> list = msgController.msgList();
            StringBuilder sb = new StringBuilder();
            for(Msg item : list){
                sb.append(item.getTitle());
                sb.append("----"+item.getDate());
                sb.append("\n");
            }
            txtMsg.setText(sb.toString());
        }catch (RemoteException e){
            Toast.makeText(this, "访问接口异常"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isConnect){
            isConnect = false;
            unbindService(conn);
        }
    }
}
