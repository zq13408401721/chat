package com.mychat.fragments.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mychat.R;
import com.mychat.apps.GlideEngine;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WidgetFragment extends Fragment {
    @BindView(R.id.img_photo)
    ImageView imgPhoto;

    PhotoSelect photoSelect;

    /**
     * 初始化fragment
     *
     * @return
     */
    public static WidgetFragment getInstance(int pos) {
        WidgetFragment widgetFragment = new WidgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        //bundle.putInt("facetype",faceType);
        widgetFragment.setArguments(bundle);
        return widgetFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_widget, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @OnClick(R.id.img_photo)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.img_photo:
                openPhoto();
                break;
        }
    }

    /**
     * pictureselector打开本地相册
     */
    private void openPhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                .maxSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // onResult Callback
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if(selectList.size() == 0) return;
                if(photoSelect != null){
                    photoSelect.selected(selectList);
                }
                break;
            default:
                break;
        }
    }

    public void addPhotoSelectListener(PhotoSelect photoSelect){
        this.photoSelect = photoSelect;
    }


    public interface PhotoSelect{
        void selected(List<LocalMedia> list);
    }

}
