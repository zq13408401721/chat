package com.mychat.activitys.web;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mychat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initData();
    }

    private void initData(){
        String url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }else{
            Toast.makeText(this,"没有相关的参数",Toast.LENGTH_SHORT).show();
        }
    }
}
