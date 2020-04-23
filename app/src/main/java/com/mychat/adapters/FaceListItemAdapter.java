package com.mychat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.module.FaceListItemVo;

import java.util.List;

public class FaceListItemAdapter extends RecyclerView.Adapter {
    List<FaceListItemVo> list;
    private ListClick listClick;

    public FaceListItemAdapter(List<FaceListItemVo> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_facelist_item,parent,false);
        ListVH listVH = new ListVH(view);
        return listVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListVH listVH = (ListVH) holder;
        FaceListItemVo itemVo = list.get(position);
        listVH.imgIcon.setImageResource(itemVo.getFaceId());
        listVH.txtName.setText(itemVo.getName());
        listVH.imgIcon.setTag(itemVo);
        listVH.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceListItemVo tmp = (FaceListItemVo) v.getTag();
                if(listClick != null){
                    listClick.onListClick(tmp.getPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addTabOnClickListener(ListClick tabClick){
        this.listClick = tabClick;
    }

    class ListVH extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView txtName;
        public ListVH(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_face);
            txtName = itemView.findViewById(R.id.txt_facename);
        }
    }

    public interface ListClick{
        void onListClick(int postion);
    }
}
