package com.mychat.fragments.live;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mvvm.live.Constant;
import com.mvvm.live.LiveActivity;
import com.mychat.R;
import com.mychat.base.BaseFragment;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LiveFragment extends BaseFragment {
    @BindView(R.id.edit_roomNo)
    EditText editRoomNo;
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.btn_enterRoom)
    Button btnEnterRoom;

    @Override
    protected int getLayout() {
        return R.layout.fragment_live;
    }

    @Override
    protected void initView() {
        String username = SpUtils.getInstance().getString("username");
        if (TextUtils.isEmpty(username)){
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        txtUsername.setText(username);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }

    @OnClick(R.id.btn_enterRoom)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_enterRoom:
                enterRoom();
                break;
        }
    }

    private void enterRoom(){
        if(TextUtils.isEmpty(editRoomNo.getText().toString())){
            Toast.makeText(context, "请输入房间号", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = SpUtils.getInstance().getString("username");
        if (TextUtils.isEmpty(username)){
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        String roomNo = editRoomNo.getText().toString();
        Intent intent = new Intent();
        intent.setAction("liveactivity");
        intent.putExtra(Constant.USER_ID,username);
        intent.putExtra(Constant.ROOM_ID,roomNo);
        startActivity(intent);
    }
}
