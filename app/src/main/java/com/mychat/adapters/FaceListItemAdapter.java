package com.mychat.adapters;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.MyApp;
import com.mychat.R;
import com.mychat.SmileyParser;
import com.mychat.common.Constant;
import com.mychat.module.FaceListItemVo;
import com.mychat.utils.DpTools;

import java.io.InputStream;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_facelist_item, parent, false);
        ListVH listVH = new ListVH(view);
        ViewGroup.LayoutParams param = listVH.imgIcon.getLayoutParams();
        if(param == null){
            param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //区分大图和小图
        if(viewType == SmileyParser.FACE_TYPE_1) {
            listVH.txtName.setVisibility(View.GONE);
            param.width = DpTools.dp2px(MyApp.myApp, Constant.FACE_SMALL_W);
            param.height = DpTools.dp2px(MyApp.myApp,Constant.FACE_SMALL_H);
        }else{
            listVH.txtName.setVisibility(View.VISIBLE);
            param.width = DpTools.dp2px(MyApp.myApp, Constant.FACE_BIG_W);
            param.height = DpTools.dp2px(MyApp.myApp,Constant.FACE_BIG_H);
        }
        listVH.imgIcon.setLayoutParams(param);
        return listVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListVH listVH = (ListVH) holder;
        FaceListItemVo itemVo = list.get(position);
        InputStream inputStream = MyApp.myApp.getResources().openRawResource(itemVo.getFaceId());
        BitmapDrawable drawable = new BitmapDrawable(inputStream);
        if(itemVo.getFaceType() == SmileyParser.FACE_TYPE_1){
            drawable.setBounds(0,0,40,40);
        }else{
            drawable.setBounds(0,0,60,60);
        }
        listVH.imgIcon.setImageDrawable(drawable);
        listVH.imgIcon.setTag(itemVo);
        listVH.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceListItemVo tmp = (FaceListItemVo) v.getTag();
                if(listClick != null){
                    listClick.onListClick(tmp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.list.get(position).getFaceType();
    }

    public void addListOnClickListener(ListClick listClick){
        this.listClick = listClick;
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
        void onListClick(FaceListItemVo itemVo);
    }
}
