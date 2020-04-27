package com.mychat;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.luck.picture.lib.entity.LocalMedia;
import com.mychat.adapters.ChatAdapter;
import com.mychat.adapters.FaceListItemAdapter;
import com.mychat.adapters.FaceTabAdapter;
import com.mychat.apps.MyApp;
import com.mychat.common.Message;
import com.mychat.fragments.FaceFragment;
import com.mychat.fragments.ImageFragment;
import com.mychat.fragments.WidgetFragment;
import com.mychat.module.HttpManager;
import com.mychat.module.apis.UploadApi;
import com.mychat.module.vo.ChatMsgBean;
import com.mychat.module.vo.FaceListItemVo;
import com.mychat.module.vo.FaceTabVo;
import com.mychat.utils.SpUtils;
import com.mychat.utils.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

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
    @BindView(R.id.btn_add)
    TextView btnAdd;
    @BindView(R.id.layout_widget)
    ConstraintLayout layoutWidget;
    @BindView(R.id.viewpager_widget)
    ViewPager viewPagerWidget;


    List<FaceTabVo> tabList;
    FaceTabAdapter faceTabAdapter;
    List<FaceFragment> faceFragments;

    List<WidgetFragment> widgetFragments;

    List<ChatMsgBean> msgList; //消息列表
    ChatAdapter chatAdapter; //聊天消息列表

    String uid;

    StringBuilder chatInput;

    int beforeLg = 0;
    CharSequence curChar = null;

    /**
     * 聊天图片的预览popwindow实现
     * @param savedInstanceState
     */
    PopupWindow photoWin;
    List<String> chatAllImages;
    List<ImageFragment> imgFragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        initSmailTab();
        initFaceList();
        initWidget();
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

    /**
     * 初始化添加面版
     */
    private void initWidget(){
        widgetFragments = new ArrayList<>();
        for(int i=0; i<2; i++){
            WidgetFragment widgetFragment = WidgetFragment.getInstance(i);
            widgetFragment.addPhotoSelectListener(new WidgetFragment.PhotoSelect() {
                @Override
                public void selected(List<LocalMedia> list) {
                    //实现图片上传
                    //uploadImage(list);
                    sendImageChat(list);
                    Toast.makeText(ChatActivity.this,"开发中",Toast.LENGTH_SHORT).show();
                }
            });
            widgetFragments.add(widgetFragment);
        }
        viewPagerWidget.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return widgetFragments.get(position);
            }

            @Override
            public int getCount() {
                return widgetFragments.size();
            }
        });

    }

    private void addListener(){

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
                if(!TextUtils.isEmpty(editChat.getText().toString())){
                    if (btnSend.getVisibility() == View.GONE){
                        btnSend.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.GONE);
                    }
                }else{
                    btnSend.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 初始化消息列表
     */
    private void initMsg(){
        msgList = new ArrayList<>();
        chatAdapter = new ChatAdapter(msgList,this);
        listChat.setLayoutManager(new LinearLayoutManager(this));
        listChat.setAdapter(chatAdapter);
        chatAdapter.addChatItemClickListener(new ChatAdapter.ChatItemClickListener() {
            @Override
            public void click(ChatMsgBean bean,View view) {
                //点击的是图片，做分支处理
                if(bean.getMsgType() == Message.MSG_TYPE_IMAGE){
                    previewImage(bean,view);
                }
            }
        });
    }

    @OnClick({R.id.txt_back, R.id.img_face,R.id.btn_send,R.id.btn_add,R.id.list_chat})
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
            case R.id.btn_add:
                showWidget();
                break;
            case R.id.list_chat:
                //隐藏键盘
                SystemUtils.setKeyBroad(this,false,editChat);
                break;
        }
    }


    private void showFaceTab(){

        if(layoutFaces.getVisibility() == View.GONE){
            layoutFaces.setVisibility(View.VISIBLE);
            imgFace.setBackgroundResource(R.mipmap.icon_broad);
            //关闭软键盘
            SystemUtils.setKeyBroad(this,false,editChat);
            listFaceTab.setVisibility(View.VISIBLE);
            viewpagerFace.setVisibility(View.VISIBLE);
            layoutWidget.setVisibility(View.GONE);
            viewPagerWidget.setVisibility(View.GONE);
        }else{
            layoutFaces.setVisibility(View.GONE);
            imgFace.setBackgroundResource(R.mipmap.bp6);
            //软键盘的显示和隐藏
            SystemUtils.setKeyBroad(this,true,editChat);
        }
    }

    private void showWidget(){
        if(layoutFaces.getVisibility() == View.GONE){
            layoutFaces.setVisibility(View.VISIBLE);

            SystemUtils.setKeyBroad(this,false,editChat);
            layoutWidget.setVisibility(View.VISIBLE);
            viewPagerWidget.setVisibility(View.VISIBLE);
            listFaceTab.setVisibility(View.GONE);
            viewpagerFace.setVisibility(View.GONE);
        }else{
            layoutFaces.setVisibility(View.GONE);
            //如果已经是显示状态，控制软键盘的显示和隐藏
            SystemUtils.setKeyBroad(this,true,editChat);
        }
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
        String str=null;
        int selectPos;
        //获取editChat光标所在的位置
        int start = editChat.getSelectionStart();
        if(editChat.getText().length() == 0 || start >= editChat.getText().toString().length()){  //在输入文本后面插入
            str = editChat.getText().toString()+itemVo.getTag();
            selectPos = str.length();
        }else if(start == 0 && editChat.getText().length() > 0){ //在输入文本的头中插入数据
            str = itemVo.getTag()+editChat.getText().toString();
            selectPos = itemVo.getTag().length();
        }else{  //在输入文本中间插入
            String temp = editChat.getText().toString();
            //光标前面的字符截取
            String str1 = temp.substring(0,start);
            //光标后面的字符截取
            String str2 = temp.substring(start,temp.length());
            str = str1+itemVo.getTag()+str2;
            selectPos = str.length()-str2.length();
        }
        CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(str);
        editChat.setText(chat);
        //设置光标的位置
        editChat.setSelection(selectPos);
    }

    /********************************本地图库处理***************************************/

    private void sendImageChat(List<LocalMedia> list){
        for(LocalMedia item : list){
            //图片上传
            ChatMsgBean chatMsgBean = new ChatMsgBean();
            chatMsgBean.setFromUid(uid);
            chatMsgBean.setStatus(Message.MSG_STATUS_ASYNC);
            //自己能看的本地图片地址
            chatMsgBean.setContent(item.getPath());
            chatMsgBean.setMsgType(Message.MSG_TYPE_IMAGE);
            int time = (int) (new Date().getTime()/1000);
            chatMsgBean.setTime(time);
            msgList.add(chatMsgBean);
            chatAdapter.notifyDataSetChanged();

            uploadImage(item.getPath());
        }
    }


    /**
     * 缺少图片上传的功能
     * @param path
     */
    private void uploadImage(String path){

        String img_format = "image/jpg";
        String key = "2020";
        //sd卡图片文件
        //File file = new File(Utils.getSDPath()+"/d.jpg");
        File file = new File(path);
        if(file.exists()){
            //创建一个RequestBody 封装文件格式以及文件内容
            RequestBody requestFile = MultipartBody.create(MediaType.parse(img_format),file);
            //创建一个MultipartBody.Part 封装的文件数据（文件流） file参数是给后台接口读取文件用，file.getName() 保存到后台的文件名字
            MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),requestFile);
            //设置对应的key application/json; charset=utf-8
            RequestBody key_file = RequestBody.create(MediaType.parse("multipart/form-data"),key);
            //通过requestbody传值到后台接口
            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),key);
            //创建retrofit
            UploadApi uploadApi = HttpManager.getInstance().getUploadApi();
            retrofit2.Call<ResponseBody> call = uploadApi.uploadFile(key_file,part);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.i("onResponse",response.body().toString());
                    //更新到数据服务器 逻辑服务器

                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.i("onFailure",t.getMessage());
                }
            });
        }else{
            Toast.makeText(this,"找不到本地文件",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 处理图片预览
     * @param bean
     */
    private void previewImage(ChatMsgBean bean,View v){
        if(photoWin == null){
            chatAllImages = new ArrayList<>();
            imgFragmentList = new ArrayList<>();
            int currentPos=0;
            for(ChatMsgBean item : msgList){
                if(item.getMsgType() == Message.MSG_TYPE_IMAGE){
                    chatAllImages.add(item.getContent());
                    imgFragmentList.add(ImageFragment.getInstance(item.getContent()));
                    if(item.getContent().equals(bean.getContent())){
                        currentPos = chatAllImages.size()-1;
                    }
                }
            }

            View view = LayoutInflater.from(this).inflate(R.layout.layout_preview_image,null);
            photoWin = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoWin.setBackgroundDrawable(new BitmapDrawable());
            photoWin.setFocusable(true);
            photoWin.setOutsideTouchable(true);
            TextView txtNum = view.findViewById(R.id.txt_num);
            txtNum.setText((currentPos+1)+"/"+imgFragmentList.size());
            ViewPager viewPager = view.findViewById(R.id.viewpager_imgs);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    updateImageSelect(txtNum,position);
                    return imgFragmentList.get(position);
                }

                @Override
                public int getCount() {
                    return imgFragmentList.size();
                }
            });
            photoWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    photoWin = null;
                }
            });
            photoWin.showAtLocation(v, Gravity.CENTER,0,0);
        }
    }

    private void updateImageSelect(TextView txt,int pos){
        txt.setText((pos+1)+"/"+imgFragmentList.size());
    }


}
