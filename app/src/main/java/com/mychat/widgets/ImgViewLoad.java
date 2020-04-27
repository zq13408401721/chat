package com.mychat.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.mychat.R;
import com.mychat.utils.DpTools;

public class ImgViewLoad extends ConstraintLayout {

    Context context;
    int imgWidth,imgHeight;
    String scaleType;

    ImageView imgResource;
    ProgressBar progressBar;


    public ImgViewLoad(Context context) {
        super(context);
        init(context,null);
    }

    public ImgViewLoad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ImgViewLoad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ImgViewLoad);
        imgWidth = typedArray.getInteger(R.styleable.ImgViewLoad_img_width,0);
        imgHeight = typedArray.getInteger(R.styleable.ImgViewLoad_img_height,0);
        scaleType = typedArray.getString(R.styleable.ImgViewLoad_img_scaletype);
        typedArray.recycle();
    }

    /**
     * 添加到界面
     */
    public void onCreatedView(){
        imgResource = findViewById(R.id.img_resourse);
        progressBar = findViewById(R.id.progress);
        if(imgWidth > 0 && imgHeight > 0){
            ViewGroup.LayoutParams layoutParams = imgResource.getLayoutParams();
            if(layoutParams == null) layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = DpTools.dp2px(context,imgWidth);
            layoutParams.height = DpTools.dp2px(context,imgHeight);
        }
    }

    public void setUrl(String url){
        Glide.with(context).load(url).into(imgResource);
    }

    public void setProgressBarVisible(int visible){
        progressBar.setVisibility(visible);
    }
}
