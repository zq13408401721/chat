package com.mychat;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mychat.adapters.FaceTabAdapter;
import com.mychat.fragments.FaceFragment;
import com.mychat.module.FaceTabVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @BindView(R.id.list_chat)
    RecyclerView listChat;
    @BindView(R.id.edit_chat)
    EditText editChat;
    @BindView(R.id.img_face)
    ImageView imgFace;
    @BindView(R.id.layout_bottom)
    ConstraintLayout layoutBottom;
    @BindView(R.id.list_face_tab)
    RecyclerView listFaceTab;
    @BindView(R.id.viewpager_face)
    ViewPager viewpagerFace;
    @BindView(R.id.layout_faces)
    ConstraintLayout layoutFaces;


    List<FaceTabVo> tabList;
    FaceTabAdapter faceTabAdapter;
    List<FaceFragment> faceFragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initSmailTab();
        initFaceList();
    }

    /**
     * 初始化表情tab导航
     */
    private void initSmailTab(){
        tabList = SmileyParser.getInstance(MyApp.myApp).getFaceTabList();
        faceTabAdapter = new FaceTabAdapter(tabList);
        listFaceTab.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        listFaceTab.setAdapter(faceTabAdapter);
    }

    private void initFaceList(){
        faceFragments = new ArrayList<>();
        int size = SmileyParser.getInstance(MyApp.myApp).getFaceListSize();
        for(int i=0; i<size; i++){
            FaceFragment faceFragment = FaceFragment.getInstance(i);
            faceFragments.add(faceFragment);
        }
        viewpagerFace.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return faceFragments.get(position);
            }

            @Override
            public int getCount() {
                return faceFragments.size();
            }
        });

    }


    @OnClick({R.id.txt_back, R.id.img_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                finish();
                break;
            case R.id.img_face:
                showFaceTab();
                break;
        }
    }


    private void showFaceTab(){
        layoutFaces.setVisibility(layoutFaces.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }







}
