package com.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mychat.anim.gif.AnimatedGifDrawable;
import com.mychat.anim.gif.AnimatedImageSpan;
import com.mychat.utils.SpannableUtils;

/**
 * https://www.jianshu.com/p/050ffa5b762c
 */
public class MainActivity extends AppCompatActivity {

    private TextView txtSpan1;
    private TextView txtSpan2;
    private TextView txtSpan3;
    private TextView txtFace;
    private Button btnTalk;

    SmileyParser smileyParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        btnTalk = findViewById(R.id.btn_talk);
        txtSpan1 = findViewById(R.id.txt_span1);
        txtSpan2 = findViewById(R.id.txt_span2);
        txtSpan3 = findViewById(R.id.txt_span3);
        txtFace = findViewById(R.id.txt_face);
        smileyParser = SmileyParser.getInstance(MyApp.myApp);

        btnTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData(){
        String span1_str = "[图标]这是富文本显示内容";
        SpannableString content = SpannableUtils.getSpannableByWord(this,span1_str);
        if(content != null){
            txtSpan1.setText(content);
        }

        String span2_str = "<百度>这是富文本显示内容";
        SpannableString content2 = SpannableUtils.getUrlSpanByWord(this,span2_str);
        if(content2 != null){
            txtSpan2.setText(content2);
            txtSpan2.setMovementMethod(LinkMovementMethod.getInstance());
            txtSpan2.setHighlightColor(Color.parseColor("#FF0000"));
        }

        //表情图标的显示
        String talk = "[拜拜]下次见[爱你]";
        CharSequence charSequence = smileyParser.replace(talk);
        txtSpan3.setText(charSequence);

        String str = "xx图标";
        AnimatedImageSpan imgSpan = new AnimatedImageSpan(new AnimatedGifDrawable(this.getResources().openRawResource(R.raw.aini), new AnimatedGifDrawable.UpdateListener() {
            @Override
            public void update() {
                txtFace.postInvalidate();
            }
        }));
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(imgSpan,0,2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txtFace.setText(spannableString);
    }

}
