package com.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mychat.anim.gif.AnimatedGifDrawable;
import com.mychat.anim.gif.AnimatedImageSpan;
import com.mychat.apps.MyApp;
import com.mychat.common.Constant;
import com.mychat.utils.SpUtils;
import com.mychat.utils.SpannableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.jianshu.com/p/050ffa5b762c
 */
public class MainActivity extends AppCompatActivity {

    private TextView txtSpan1;
    private TextView txtSpan2;
    private TextView txtSpan3;
    private TextView txtSpan4;
    private TextView txtFace;
    private Button btnTalk;
    private Button btnPop;
    private ImageView img;

    PopupWindow popupWindow;
    List<String> list;

    SmileyParser smileyParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        SpUtils.getInstance().setValue("uid","100");

        Intent intent = new Intent();
        intent.setAction("chat_view");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView(){
        btnTalk = findViewById(R.id.btn_talk);
        txtSpan1 = findViewById(R.id.txt_span1);
        txtSpan2 = findViewById(R.id.txt_span2);
        txtSpan3 = findViewById(R.id.txt_span3);
        txtSpan4 = findViewById(R.id.txt_span4);
        txtFace = findViewById(R.id.txt_face);
        btnPop = findViewById(R.id.btn_pop);
        img = findViewById(R.id.img);
        smileyParser = SmileyParser.getInstance(MyApp.myApp);

        btnTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });

        if(Constant.curItemVo != null){

        }

        list = new ArrayList<>();
        for(int i=0; i<50; i++){
            list.add("item"+i);
        }

        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopwindow();
            }
        });

        Glide.with(this).load(R.mipmap.ic_launcher).into(img);


        String str = "小明回复lily:放学一起走！";
        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString spannableString1 = new SpannableString("小明");
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.BLUE);
        spannableString1.setSpan(colorSpan1,0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(MainActivity.this,"小明",Toast.LENGTH_SHORT).show();
            }
        },0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.append(spannableString1);
        sb.append("回复");
        SpannableString spannableString2 = new SpannableString("lily:");
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.BLUE);
        spannableString2.setSpan(colorSpan2,0,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(MainActivity.this,"lily",Toast.LENGTH_SHORT).show();
            }
        },0,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.append(spannableString2);
        sb.append("放学一起走！");
        txtSpan4.setText(sb);
        txtSpan4.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openPopwindow(){
        //if(popupWindow == null){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popwindow,null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            RecyclerView recy = view.findViewById(R.id.recy);
            DataAdapter dataAdapter = new DataAdapter();
            recy.setLayoutManager(new LinearLayoutManager(this));
            recy.setAdapter(dataAdapter);
            popupWindow.showAtLocation(btnPop, Gravity.CENTER,0,0);
       // }
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
        AnimatedGifDrawable animatedGifDrawable = new AnimatedGifDrawable();
        /*AnimatedImageSpan imgSpan = new AnimatedImageSpan(new AnimatedGifDrawable().onCreate(this.getResources().openRawResource(R.raw.aini), new AnimatedGifDrawable.UpdateListener() {
            @Override
            public void update() {
                txtFace.postInvalidate();
            }
        }););*/
        animatedGifDrawable.onCreate(this.getResources().openRawResource(R.raw.aini), new AnimatedGifDrawable.UpdateListener() {
            @Override
            public void update() {
                txtFace.postInvalidate();
            }
        });
        AnimatedImageSpan imgSpan = new AnimatedImageSpan(animatedGifDrawable);
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(imgSpan,0,2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txtFace.setText(spannableString);
    }


    class DataAdapter extends RecyclerView.Adapter{


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_chat_right_item,parent,false);
            VH vh = new VH(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            vh.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class VH extends RecyclerView.ViewHolder{
        TextView textView;
        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_name);
        }
    }

}
