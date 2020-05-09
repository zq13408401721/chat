package com.mychat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.module.vo.TrendsVo;

import java.util.List;

public class TrendsPublishAdapter extends RecyclerView.Adapter {

    private Context context;
    List<TrendsVo> list;

    OpenPhoto openPhoto;

    public TrendsPublishAdapter(Context context, List<TrendsVo> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == 0){
            //如果类型是0 表示当前的条目是加号
            view = LayoutInflater.from(context).inflate(R.layout.layout_trends_publish_item_add,parent,false);
            viewHolder = new VHAdd(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.layout_trends_publish_item_normal,parent,false);
            viewHolder = new VHNormal(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHAdd){
            VHAdd vhAdd = (VHAdd) holder;
            vhAdd.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开本地相机相册功能
                    if(getItemCount() >= 9){
                        Toast.makeText(context,"最大只能添加9张图片",Toast.LENGTH_SHORT).show();
                    }else{
                        if(openPhoto != null){
                            openPhoto.addImageClick();
                        }
                    }
                }
            });
        }else if(holder instanceof VHNormal){
            VHNormal vhNormal = (VHNormal) holder;
            vhNormal.txtClose.setTag(position);
            vhNormal.txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除当前的这条数据
                    int pos = (int) v.getTag();
                    if(pos < getItemCount()){
                        list.remove(pos);
                        notifyDataSetChanged();
                        if(openPhoto != null){
                            openPhoto.deleteImage(pos);
                        }
                    }
                }
            });
            //点击列表中的单张图片，进行预览
            vhNormal.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    /**
     * 设置接口回调的监听
     * @param openPhoto
     */
    public void addOnClickListener(OpenPhoto openPhoto){
        this.openPhoto = openPhoto;
    }

    //定义打开本地相机相册功能回调接口
    public interface OpenPhoto{
        void addImageClick();
        //删除上次选中图片的记录
        void deleteImage(int pos);
    }


    /**
     * 加号的item
     */
    class VHAdd extends RecyclerView.ViewHolder{

        ImageView imgAdd;
        public VHAdd(@NonNull View itemView) {
            super(itemView);
            imgAdd = itemView.findViewById(R.id.img_add);
        }
    }

    /**
     * 正常图片显示的条目
     */
    class VHNormal extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtClose;
        public VHNormal(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txtClose = itemView.findViewById(R.id.txt_close);
        }
    }

}
