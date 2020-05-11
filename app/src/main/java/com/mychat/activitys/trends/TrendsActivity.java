package com.mychat.activitys.trends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mychat.R;
import com.mychat.adapters.TrendsPublishAdapter;
import com.mychat.apps.GlideEngine;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.module.HttpManager;
import com.mychat.module.apis.UploadApi;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.vo.SaveDataBean;
import com.mychat.module.vo.TrendsVo;
import com.mychat.persenters.trends.TrendsPersenter;
import com.mychat.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class TrendsActivity extends BaseActivity<TrendsStract.Persenter> implements TrendsPublishAdapter.OpenPhoto, TrendsStract.View {
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @BindView(R.id.edit_word)
    EditText editWord;
    @BindView(R.id.recy_imgs)
    RecyclerView recyImgs;
    @BindView(R.id.txt_send)
    TextView txtSend;

    TrendsPublishAdapter trendsPublishAdapter;
    List<TrendsVo> list;

    /**
     * 当前本地图片选择器的内容
     */
    List<LocalMedia> selectList;
    Map<String,String> upLoadImgs;

    @Override
    protected int getLayout() {
        return R.layout.activity_trends_publish;
    }

    @Override
    protected void initView() {

    }

    /**
     * 初始化界面数据
     */
    @Override
    protected void initData() {
        selectList = new ArrayList<>();
        list = new ArrayList<>();
        //判断上一次是否有保存的草稿信息  从sp中去获取 json
        String username = SpUtils.getInstance().getString("username");
        if(!TextUtils.isEmpty(username)){
            txtUsername.setText(username);
            //获取sp中保存的草稿
            String json = SpUtils.getInstance().getString(username);
            if(!TextUtils.isEmpty(json)){
                SaveDataBean bean = new Gson().fromJson(json,SaveDataBean.class);
                if(!TextUtils.isEmpty(bean.getWord())){
                    editWord.setText(bean.getWord());
                }
                //图片的处理
                for(SaveDataBean.ImgsBean item : bean.getImgs()){
                    TrendsVo trendsVo = new TrendsVo();
                    trendsVo.setPath(item.getPath());
                    trendsVo.setType(1);
                    list.add(trendsVo);
                }
            }
        }
        //添加加号按钮
        list.add(getAddTrendsItem());
        trendsPublishAdapter = new TrendsPublishAdapter(this,list);
        recyImgs.setLayoutManager(new GridLayoutManager(this,3));
        recyImgs.setAdapter(trendsPublishAdapter);
        trendsPublishAdapter.addOnClickListener(this);

    }

    @Override
    protected TrendsStract.Persenter createPersenter() {
        return new TrendsPersenter();
    }


    /**
     * 加号按钮
     * @return
     */
    private TrendsVo getAddTrendsItem(){
        TrendsVo trendsVo = new TrendsVo();
        trendsVo.setType(0);
        return trendsVo;
    }

    /**
     * 打开本地相机相册
     */
    @Override
    public void addImageClick() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)
                .selectionMedia(selectList)
                .maxSelectNum(9)
                .imageSpanCount(4)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .rotateEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 删除本地选中图片的记录
     * @param pos
     */
    @Override
    public void deleteImage(int pos) {
        if(pos < selectList.size()){
            selectList.remove(pos);
        }
    }

    /**
     * 处理本地相机相册获取图片返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PictureConfig.CHOOSE_REQUEST:
                selectList = PictureSelector.obtainMultipleResult(data);
                if(selectList.size() == 0) return;
                updateList(selectList);
                break;
        }
    }

    /**
     * 更新列表数据
     * @param list
     */
    private void updateList(List<LocalMedia> list){
        this.list.clear();
        for(LocalMedia item : list){
            TrendsVo trendsVo = new TrendsVo();
            trendsVo.setType(1);
            trendsVo.setPath(item.getPath());
            this.list.add(trendsVo);
        }
        //每次数据更新的时候，在列表最后添加加号
        this.list.add(getAddTrendsItem());
        trendsPublishAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            String content = editWord.getText().toString();
            //判断当前的文本是否有输入内容，列表是否有图片数据
            if(!TextUtils.isEmpty(content) || this.list.size()>1){
                openSaveDialog();
                //不执行父类点击事件
                return true;
            }
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 打开提示保存草稿的弹框
     */
    private void openSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否保存草稿？");
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String saveData = getSaveData();
                String username = SpUtils.getInstance().getString("username");
                //保存草稿到本地
                if(!TextUtils.isEmpty(username)){
                    SpUtils.getInstance().setValue(username,saveData);
                }
                finish();
            }
        });
    }

    /**
     * 组装需要保存的草稿数据
     * @return
     */
    private String getSaveData(){
        JSONObject jsonObject = new JSONObject();
        String word = editWord.getText().toString();
        try {
            //放入输入的文本内容
            jsonObject.put("word",word);
            //放入图片
            JSONArray imgs = new JSONArray();
            for(TrendsVo item : list){
                JSONObject img = new JSONObject();
                img.put("path",item.getPath());
                imgs.put(img);
            }
            jsonObject.put("imgs",imgs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @OnClick(R.id.txt_send)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.txt_send:
                checkSendTrends();
                break;
        }
    }

    /**
     * 检查发送动态数据
     */
    private void checkSendTrends(){
        String content = editWord.getText().toString();
        if(TextUtils.isEmpty(content) && list.size() == 1){
            Toast.makeText(this,"请正确输入你要发布的内容",Toast.LENGTH_SHORT).show();
            return;
        }
        upLoadImgs = new HashMap<String, String>();
        /**
         * 图片的上传
         */
        if(list.size() > 1) {
            for (int i = 0; i < list.size() - 1; i++) {
                String currentThreadId = "";
                int finalI = i;
                String finalCurrentThreadId = currentThreadId;  //奇怪的操作
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadImage(finalCurrentThreadId, list.get(finalI).getPath());
                    }
                });
                currentThreadId = String.valueOf(thread.getId());
                thread.start();
            }
        }else{
            sendTrends();
        }

    }

    /**
     * 发布动态返回结果
     * @param result
     */
    @Override
    public void sendTrendsReturn(PublishTrendsBean result) {
        if(result.getErr() == 30000){
            //关闭发布动态的页面
            finish();
        }
    }

    /********************************图片上传********************************/
    private void uploadImage(final String threadid,String path){
        String img_format = "image/jpg";
        String key = SpUtils.getInstance().getString("username");
        //sd卡图片文件
        File file = new File(path);
        if(file.exists()){
            //创建一个RequestBody 封装文件格式以及文件内容
            RequestBody requestFile = MultipartBody.create(MediaType.parse(img_format),file);
            String filename="head.jpg";
            try{
                filename = URLEncoder.encode(file.getName(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //创建一个MultipartBody.Part 封装的文件数据（文件流） file参数是给后台接口读取文件用，file.getName() 保存到后台的文件名字
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", filename,requestFile);
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
                    try {
                        String result = response.body().string();
                        //更新到数据服务器 逻辑服务器 把得到的图片的外网路径回传到详情页面，由详情页面处理用户数据的接口更新
                        JSONObject jsonObj = new JSONObject(result);
                        String imgUrl = jsonObj.getJSONObject("data").getString("url");
                        upLoadImgs.put(threadid,imgUrl);
                        //判断上传是否完成
                        checkUpLoadOver();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
     * 判断上传是否完成
     */
    private void checkUpLoadOver(){
        if(upLoadImgs.size() == list.size()-1){
            sendTrends();
        }
    }

    /**
     * 发送数据
     */
    private void sendTrends(){
        String content = editWord.getText().toString();
        StringBuilder stringBuilder = new StringBuilder();
        for(String value : upLoadImgs.values()){
            stringBuilder.append(value);
            stringBuilder.append("$");
        }
        //删除尾部多余的$
        if(stringBuilder.length() > 0) stringBuilder.deleteCharAt(stringBuilder.length()-1);
        persenter.sendTrends(content,stringBuilder.toString());
    }
}
