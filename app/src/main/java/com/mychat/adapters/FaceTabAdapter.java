package com.mychat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.module.vo.FaceTabVo;

import java.util.List;

public class FaceTabAdapter extends RecyclerView.Adapter {
    List<FaceTabVo> list;
    private TabClick tabClick;

    public FaceTabAdapter(List<FaceTabVo> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_facetab_item,parent,false);
        TabVH tabVH = new TabVH(view);
        return tabVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TabVH tabVH = (TabVH) holder;
        FaceTabVo faceTabVo = list.get(position);
        tabVH.imgIcon.setImageResource(faceTabVo.getFaceId());
        tabVH.imgIcon.setTag(faceTabVo);
        tabVH.imgIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(tabClick != null){
                    FaceTabVo tabVo = (FaceTabVo) v.getTag();
                    tabClick.onTabClick(tabVo.getPostion());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addTabOnClickListener(TabClick tabClick){
        this.tabClick = tabClick;
    }

    class TabVH extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        public TabVH(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
        }
    }

    public interface TabClick{
        void onTabClick(int postion);
    }
}
