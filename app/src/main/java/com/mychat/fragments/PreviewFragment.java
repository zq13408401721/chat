package com.mychat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mychat.R;

import java.util.ArrayList;
import java.util.List;

public class PreviewFragment extends DialogFragment implements ImageFragment.SingleClick {

    ViewPager viewPagerImgs;
    TextView txtNum;
    List<ImageFragment> fragments;
    int curPos;
    public PreviewFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(PreviewFragment.STYLE_NO_TITLE,R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_preview_image,null);
        return view;
    }

    /**
     * 全屏处理
     */
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    public static PreviewFragment newInstance(ArrayList<String> list, int curPos){
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imgs",list);
        bundle.putInt("pos",curPos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerImgs = view.findViewById(R.id.viewpager_imgs);
        txtNum = view.findViewById(R.id.txt_num);
        fragments = new ArrayList<>();
        curPos = getArguments().getInt("pos");
        List<String> imgs = getArguments().getStringArrayList("imgs");
        for(String url:imgs){
            ImageFragment fragment = ImageFragment.getInstance(url);
            fragment.addSingleClickListener(this);
            fragments.add(fragment);
        }
        updateImageSelect(txtNum,curPos);
        viewPagerImgs.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                updateImageSelect(txtNum,position);
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    private void updateImageSelect(TextView txt,int pos){
        txt.setText((pos+1)+"/"+fragments.size());
    }

    @Override
    public void click() {
        super.dismiss();
    }
}
