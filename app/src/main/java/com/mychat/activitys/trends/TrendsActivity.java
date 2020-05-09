package com.mychat.activitys.trends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mychat.R;
import com.mychat.adapters.TrendsPublishAdapter;
import com.mychat.apps.GlideEngine;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.module.vo.TrendsVo;

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




}
