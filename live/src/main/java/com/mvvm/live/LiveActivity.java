package com.mvvm.live;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class LiveActivity extends AppCompatActivity {

    private static final int REQ_PERMISSION_CODE  = 0x1000;

    @BindView(R2.id.live_view)
    TXCloudVideoView liveView;
    @BindView(R2.id.room_number)
    TextView txtRoomNumber;
    @BindView(R2.id.live_view_1)
    TXCloudVideoView liveViewOne;
    @BindView(R2.id.btn_camera)
    Button btnCamera;
    @BindView(R2.id.btn_audio)
    Button btnAudio;
    @BindView(R2.id.btn_video)
    Button btnVideo;
    @BindView(R2.id.btn_log)
    Button btnLog;
    @BindView(R2.id.layout_default)
    ConstraintLayout layoutDefault;

    private int mGrantedCount = 0;          // 权限个数计数，获取Android系统权限
    private String mUserId;  //用户userid
    private String mRoomId;   //房价ID
    private List<String> mRemoteUidList; //用户Id列表
    private List<TXCloudVideoView> mRemoteViewList;  //用户画面列表
    private TRTCCloud mTRTCCloud; //SDK核心类
    private boolean mIsFrontCamera = true; //默认是否是前置摄像头



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        initData();
        if(checkPermission()){
            initView();
            enterRoom();
        }
    }

    private void initData(){
        Intent intent = getIntent();
        if(null != intent){
            if(intent.getStringExtra(Constant.USER_ID) != null){
                mUserId = intent.getStringExtra(Constant.USER_ID);
            }
            if(intent.getStringExtra(Constant.ROOM_ID) != null){
                mRoomId = intent.getStringExtra(Constant.ROOM_ID);
            }
        }
    }

    private void initView(){
        if(!TextUtils.isEmpty(mRoomId)){
            txtRoomNumber.setText(mRoomId);
        }
        mRemoteUidList = new ArrayList<>();
        mRemoteViewList = new ArrayList<>();
        mRemoteViewList.add(liveViewOne);
    }

    /**
     * 进入房间
     */
    private void enterRoom(){
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(this));
        //初始化配置参数
        TRTCCloudDef.TRTCParams  trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateUserSig.SDKAPPID;
        trtcParams.userId = mUserId;
        trtcParams.roomId = Integer.parseInt(mRoomId);
        //设置用户签名 正式环境有后台提供
        trtcParams.userSig = GenerateUserSig.genTestUserSig(trtcParams.userId);
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor; //设置当前的用户是主播还是观看

        //进入通话
        mTRTCCloud.enterRoom(trtcParams,TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        //开启本地声音采集并上行
        mTRTCCloud.startLocalAudio();
        //开启本地画面采集并上行
        mTRTCCloud.startLocalPreview(mIsFrontCamera,liveView);

        /**
         * 设置默认美颜效果（美颜效果：自然，美颜级别：5, 美白级别：1）
         * 美颜风格.三种美颜风格：0 ：光滑  1：自然  2：朦胧
         * 视频通话场景推荐使用“自然”美颜效果
         */
        TXBeautyManager beautyManager = mTRTCCloud.getBeautyManager();
        beautyManager.setBeautyStyle(Constant.BEAUTY_STYLE_NATURE);
        beautyManager.setBeautyLevel(5);
        beautyManager.setWhitenessLevel(1);

        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
        encParam.videoFps = Constant.VIDEO_FPS;
        encParam.videoBitrate = Constant.RTC_VIDEO_BITRATE;
        //设置视频的方向
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        mTRTCCloud.setVideoEncoderParam(encParam);

    }

    /**
     * 离开房间
     */
    private void exitRoom(){
        //关闭声音
        mTRTCCloud.stopLocalAudio();
        //关闭本地预览画面
        mTRTCCloud.stopLocalPreview();
        //退出房间
        mTRTCCloud.exitRoom();
        //销毁实例
        if(mTRTCCloud != null){
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }

    @OnClick(R2.id.ic_back)
    public void backClick(View view) {
        finish();
    }

    @OnClick(R2.id.live_view_1)
    public void liveView1Click(View view){

    }

    @OnClick(R2.id.btn_camera)
    public void btnCameraClick(View view){
        switchCamera();
    }

    @OnClick(R2.id.btn_audio)
    public void btnAudioClick(View view){
        muteAudio();
    }

    @OnClick(R2.id.btn_video)
    public void btnVideoClick(View view){
        muteVideo();
    }

    @OnClick(R2.id.btn_log)
    public void btnLogClick(View view){
    }

    /**
     * 本地视频预览控制开启或关闭
     */
    private void muteVideo(){
        boolean isSelected = btnVideo.isSelected();
        if(!isSelected){
            mTRTCCloud.stopLocalPreview();
            btnVideo.setBackground(getResources().getDrawable(R.mipmap.rtc_camera_off));
            layoutDefault.setVisibility(View.VISIBLE);
        }else{
            mTRTCCloud.startLocalPreview(mIsFrontCamera,liveView);
            btnVideo.setBackground(getResources().getDrawable(R.mipmap.rtc_camera_on));
            layoutDefault.setVisibility(View.GONE);
        }
        btnVideo.setSelected(!isSelected);
    }

    /**
     * 本地声音控制 开启或关闭
     */
    private void muteAudio(){
        boolean isSelected = btnAudio.isSelected();
        if(!isSelected){
            mTRTCCloud.stopLocalAudio();
            btnAudio.setBackground(getResources().getDrawable(R.mipmap.rtc_mic_off));
        }else{
            mTRTCCloud.startLocalAudio();
            btnAudio.setBackground(getResources().getDrawable(R.mipmap.rtc_mic_on));
        }
        btnAudio.setSelected(!isSelected);
    }

    /**
     * 切换摄像头
     */
    private void switchCamera(){
        mTRTCCloud.switchCamera();
        boolean isSelected = btnCamera.isSelected();
        mIsFrontCamera = !isSelected;
        btnCamera.setSelected(!isSelected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitRoom();
    }

    //////////////////////////////////    Android动态权限申请   ////////////////////////////////////////

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(LiveActivity.this,
                        permissions.toArray(new String[0]),
                        REQ_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_CODE) {
            for (int ret : grantResults) {
                if (PackageManager.PERMISSION_GRANTED == ret) mGrantedCount++;
            }
            if (mGrantedCount == permissions.length) {
                initView();
                enterRoom(); //首次启动，权限都获取到，才能正常进入通话
            } else {
                Toast.makeText(this, getString(R.string.rtc_permisson_error_tip), Toast.LENGTH_SHORT).show();
            }
            mGrantedCount = 0;
        }
    }

    /**
     * 核心类监听
     */
    private class TRTCCloudImplListener extends TRTCCloudListener{

        private WeakReference<LiveActivity> mContext;

        public TRTCCloudImplListener(LiveActivity activity){
            super();
            mContext = new WeakReference<>(activity);
        }


        @Override
        public void onUserVideoAvailable(String uid, boolean available) {
            //super.onUserVideoAvailable(s, b);
            int index = mRemoteUidList.indexOf(uid);
            if(available){
                if(index != -1)  return;  //如果用户已经加入就不用重复加入
                mRemoteUidList.add(uid);
                refreshRemoteVideoViews();
            }else{
                if(index != -1) return; //画面已经关闭
                //关闭对应的用户视频
                mTRTCCloud.stopRemoteView(uid);
                //清除用户
                mRemoteUidList.remove(index);
                //刷新画面显示
                refreshRemoteVideoViews();
            }
        }

        /**
         * 开启视频失败
         * @param errCode
         * @param errMsg
         * @param bundle
         */
        @Override
        public void onError(int errCode, String errMsg, Bundle bundle) {
            //super.onError(errCode, errMsg, bundle);
            LiveActivity activity = mContext.get();
            if(activity != null){
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode+ "]" , Toast.LENGTH_SHORT).show();
                if(errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL){
                    activity.exitRoom();
                }
            }
        }

        /**
         * 刷新直播画面显示
         */
        private void refreshRemoteVideoViews(){
            for(int i=0; i<mRemoteViewList.size(); i++){
                if(i < mRemoteViewList.size()){
                    String uid = mRemoteUidList.get(i);
                    mRemoteViewList.get(i).setVisibility(View.VISIBLE);
                    //开启显示用户uid的视频画面
                    mTRTCCloud.startRemoteView(uid,mRemoteViewList.get(i));
                }else{
                    mRemoteViewList.get(i).setVisibility(View.GONE);
                }
            }
        }
    }

}
