package com.mychat.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mychat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragment extends Fragment {

    @BindView(R.id.img_big)
    PhotoView imgBig;


    public static ImageFragment getInstance(String url){
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_preview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        String url  = getArguments().getString("url");
        if(!TextUtils.isEmpty(url)){
            Glide.with(getContext()).load(url).into(imgBig);
        }
    }

}
