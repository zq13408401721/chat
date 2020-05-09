package com.mychat.activitys.trends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

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
import com.mychat.module.vo.SaveDataBean;
import com.mychat.module.vo.TrendsVo;
import com.mychat.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendsActivity extends BaseActivity implements TrendsPublishAdapter.OpenPhoto {
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @BindView(R.id.edit_word)
    EditText editWord;
    @BindView(R.id.recy_imgs)
    RecyclerView recyImgs;

    TrendsPublishAdapter trendsPublishAdapter;
    List<TrendsVo> list;

    /**
     * 当前本地图片选择器的内容
     */
    List<LocalMedia> selectList;

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
    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
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
                .selectionMode(PictureConfig.SINGLE)
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
            if(!TextUtils.isEmpty(content) || this.list.size()>0){
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
}
