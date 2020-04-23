package com.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.TextView;

import com.mychat.utils.SpannableUtils;

/**
 * https://www.jianshu.com/p/050ffa5b762c
 */
public class MainActivity extends AppCompatActivity {

    private TextView txtSpan1;
    private TextView txtSpan2;
    private TextView txtSpan3;

    SmileyParser smileyParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        txtSpan1 = findViewById(R.id.txt_span1);
        txtSpan2 = findViewById(R.id.txt_span2);
        txtSpan3 = findViewById(R.id.txt_span3);
        smileyParser = new SmileyParser(this);
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
    }

}
