package com.mychat.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.module.vo.TrendsVo;

import java.util.List;

public class TrendsPublishAdapter extends RecyclerView.Adapter {

    private Context context;
    List<TrendsVo> list;

    public TrendsPublishAdapter(Context context, List<TrendsVo> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
