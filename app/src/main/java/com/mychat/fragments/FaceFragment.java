package com.mychat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mychat.R;

public class FaceFragment extends Fragment {

    /**
     * 初始化fragment
     * @return
     */
    public static FaceFragment getInstance(){
        FaceFragment faceFragment = new FaceFragment();
        return faceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_face,container,false);
        return view;
    }
}
