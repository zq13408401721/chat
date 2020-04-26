package com.mychat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mychat.MyApp;
import com.mychat.R;
import com.mychat.SmileyParser;
import com.mychat.adapters.FaceListItemAdapter;
import com.mychat.module.FaceListItemVo;

import java.util.List;

public class FaceFragment extends Fragment {

    RecyclerView recyFace;
    List<FaceListItemVo> faceList;
    FaceListItemAdapter faceListItemAdapter;
    public FaceListItemAdapter.ListClick listClick;
    /**
     * 初始化fragment
     *
     * @return
     */
    public static FaceFragment getInstance(int pos, FaceListItemAdapter.ListClick click) {
        FaceFragment faceFragment = new FaceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        //bundle.putInt("facetype",faceType);
        faceFragment.setArguments(bundle);
        faceFragment.listClick = click;
        return faceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_face, container, false);
        recyFace = view.findViewById(R.id.recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int pos = getArguments().getInt("pos");
        //int faceType = getArguments().getInt("facetype");
        faceList = SmileyParser.getInstance(MyApp.myApp).getFaceItemListByPos(pos);
        faceListItemAdapter = new FaceListItemAdapter(faceList);
        if(faceList.size() > 0){
            FaceListItemVo itemVo = faceList.get(0);
            if(itemVo.getFaceType() == SmileyParser.FACE_TYPE_1){
                recyFace.setLayoutManager(new GridLayoutManager(getContext(),10));
            }else{
                recyFace.setLayoutManager(new GridLayoutManager(getContext(),5));
            }
        }
        recyFace.setAdapter(faceListItemAdapter);
        faceListItemAdapter.addListOnClickListener(this.listClick);
    }
}
