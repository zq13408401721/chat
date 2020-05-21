package com.behaviorexample.imgpreview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 做为图片显示的SDK功能的封装
 */
public class PreviewActivity extends AppCompatActivity {

    ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imgPreview = findViewById(R.id.img_preview);
        initData();
    }

    private void initData(){
        String url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            Glide.with(this).load(url).into(imgPreview);
        }
    }
}
