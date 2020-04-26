package com.mychat;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mychat.adapters.ChatAdapter;
import com.mychat.adapters.FaceListItemAdapter;
import com.mychat.adapters.FaceTabAdapter;
import com.mychat.common.Message;
import com.mychat.fragments.FaceFragment;
import com.mychat.module.ChatMsgBean;
import com.mychat.module.FaceListItemVo;
import com.mychat.module.FaceTabVo;
import com.mychat.utils.SpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @BindView(R.id.list_chat)
    RecyclerView listChat;
    @BindView(R.id.edit_chat)
    EditText editChat;
    @BindView(R.id.img_face)
    ImageView imgFace;
    @BindView(R.id.layout_bottom)
    ConstraintLayout layoutBottom;
    @BindView(R.id.list_face_tab)
    RecyclerView listFaceTab;
    @BindView(R.id.viewpager_face)
    ViewPager viewpagerFace;
    @BindView(R.id.layout_faces)
    ConstraintLayout layoutFaces;
    @BindView(R.id.btn_send)
    TextView btnSend;


    List<FaceTabVo> tabList;
    FaceTabAdapter faceTabAdapter;
    List<FaceFragment> faceFragments;

    List<ChatMsgBean> msgList; //消息列表
    ChatAdapter chatAdapter; //聊天消息列表

    String uid;

    StringBuilder chatInput;

    int beforeLg = 0;
    CharSequence curChar = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initSmailTab();
        initFaceList();
        addListener();
        initMsg();
        chatInput = new StringBuilder();
        uid = SpUtils.getInstance().getString("uid");
    }

    /**
     * 初始化表情tab导航
     */
    private void initSmailTab(){
        tabList = SmileyParser.getInstance(MyApp.myApp).getFaceTabList();
        faceTabAdapter = new FaceTabAdapter(tabList);
        listFaceTab.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        listFaceTab.setAdapter(faceTabAdapter);
    }

    private void initFaceList(){
        faceFragments = new ArrayList<>();
        int size = SmileyParser.getInstance(MyApp.myApp).getFaceListSize();
        for(int i=0; i<size; i++){
            FaceFragment faceFragment = FaceFragment.getInstance(i, new FaceListItemAdapter.ListClick() {
                @Override
                public void onListClick(FaceListItemVo itemVo) {
                    if(itemVo.getFaceType() == SmileyParser.FACE_TYPE_1){
                        addSmiley(itemVo);
                    }else if(itemVo.getFaceType() == SmileyParser.FACE_TYPE_2){
                        //发送表情
                    }
                }
            });
            faceFragments.add(faceFragment);
        }
        viewpagerFace.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return faceFragments.get(position);
            }

            @Override
            public int getCount() {
                return faceFragments.size();
            }
        });

    }

    private void addListener(){

        /*editChat.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    int lg = chatInput.length();
                    if(lg > 0){
                        chatInput.deleteCharAt(lg-1);
                        editChat.setText(chatInput.toString());
                    }
                }
                return false;
            }
        });*/

        editChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLg = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                curChar = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int afterLg = s.length();
                if(afterLg < beforeLg){
                    int lg = chatInput.length();
                    if(lg > 0){
                        int start = chatInput.lastIndexOf("[");
                        chatInput.delete(start,chatInput.length());
                        CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(chatInput);
                        editChat.setText(chat);
                    }
                }else{
                    if(!TextUtils.isEmpty(editChat.getText().toString())){
                        if (btnSend.getVisibility() == View.GONE){
                            btnSend.setVisibility(View.VISIBLE);
                        }
                    }else{
                        btnSend.setVisibility(View.GONE);
                    }
                }
            }
        });


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    /**
     * 初始化消息列表
     */
    private void initMsg(){
        msgList = new ArrayList<>();
        chatAdapter = new ChatAdapter(msgList,this);
        listChat.setLayoutManager(new LinearLayoutManager(this));
        listChat.setAdapter(chatAdapter);
    }

    @OnClick({R.id.txt_back, R.id.img_face,R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                finish();
                break;
            case R.id.img_face:
                showFaceTab();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
        }
    }


    private void showFaceTab(){
        layoutFaces.setVisibility(layoutFaces.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }


    private void sendMsg(){

        String content = editChat.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
            return;
        }

        int own = (int) (Math.random()*3);
        String _uid = own == 1 ? uid : "200";
        //构建消息对象
        ChatMsgBean chatMsgBean = new ChatMsgBean();
        chatMsgBean.setFromUid(_uid);

        /*if(own == 2){
            chatMsgBean.setContent("http://img2.imgtn.bdimg.com/it/u=360742069,3464339755&fm=26&gp=0.jpg");
            chatMsgBean.setMsgType(Message.MSG_TYPE_IMAGE);
        }else{}*/
        chatMsgBean.setContent(content);
        chatMsgBean.setMsgType(Message.MSG_TYPE_WORD);

        int time = (int) (new Date().getTime()/1000);
        chatMsgBean.setTime(time);

        msgList.add(chatMsgBean);
        //清空输入框
        editChat.setText("");
        chatInput.delete(0,chatInput.length());

        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 插入表情
     * @param itemVo
     */
    private void addSmiley(FaceListItemVo itemVo){
        String str = chatInput.toString()+itemVo.getTag();
        CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(str);
        chatInput.append(chat);
        editChat.setText(chat);
    }



}
