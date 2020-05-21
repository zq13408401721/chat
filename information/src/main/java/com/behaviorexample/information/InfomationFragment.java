package com.behaviorexample.information;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfomationFragment extends Fragment {

    ImageView head;
    TextView txtUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_infomation,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        head = view.findViewById(R.id.img_head);
        txtUsername = view.findViewById(R.id.txt_username);
        String head = SpUtils.getInstance(getContext()).getString("avater");
        if(!TextUtils.isEmpty(head)){

        }

        String username = SpUtils.getInstance(getContext()).getString("username");
        if(!TextUtils.isEmpty(username)){
            txtUsername.setText(username);
        }


    }
}
