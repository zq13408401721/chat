package com.mychat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mychat.R;
import com.mychat.common.Constant;
import com.mychat.common.Message;
import com.mychat.module.ChatMsgBean;
import com.mychat.utils.SpUtils;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    String uid;
    List<ChatMsgBean> list;
    Context context;
    public ChatAdapter(List<ChatMsgBean> list,Context context){
        this.list = list;
        uid = SpUtils.getInstance().getString("uid");
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutid;
        if(viewType == Message.MSG_SELF){
            layoutid = R.layout.layout_chat_right_item;
        }else{
            layoutid = R.layout.layout_chat_left_item;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutid,parent,false);
        ChatVH chatVH = new ChatVH(view);
        return chatVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMsgBean chatMsgBean = list.get(position);
        ChatVH chatVH = (ChatVH) holder;
        //分支判断当前的消息类型
        if(chatMsgBean.getMsgType() == Message.MSG_TYPE_WORD){
            chatVH.txtContent.setText(chatMsgBean.getContent());
            chatVH.imgContent.setVisibility(View.GONE);
            chatVH.txtContent.setVisibility(View.VISIBLE);
        }else if(chatMsgBean.getMsgType() == Message.MSG_TYPE_IMAGE){
            chatVH.imgContent.setVisibility(View.VISIBLE);
            chatVH.txtContent.setVisibility(View.GONE);
            Glide.with(context).load(chatMsgBean.getContent()).into(chatVH.imgContent);
        }
        //判断当前这条消息是否是自己
        String _uid = chatMsgBean.getFromUid();
        if(_uid.equals(uid)){
            chatVH.txtName.setVisibility(View.GONE);
            Glide.with(context).load(Constant.self_avater).into(chatVH.imgHeader);
        }else{
            chatVH.txtName.setText("other");
            chatVH.txtName.setVisibility(View.VISIBLE);
            Glide.with(context).load(Constant.other_avater).into(chatVH.imgHeader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String _uid = list.get(position).getFromUid();
        if(_uid.equals(uid)){
            return Message.MSG_SELF;
        }
        return Message.MSG_OTHER;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ChatVH extends RecyclerView.ViewHolder{

        ImageView imgHeader;
        TextView txtName;
        TextView txtContent;
        ImageView imgContent;

        public ChatVH(@NonNull View itemView) {
            super(itemView);
            imgHeader = itemView.findViewById(R.id.img_header);
            txtName = itemView.findViewById(R.id.txt_name);
            txtContent = itemView.findViewById(R.id.txt_content);
            imgContent = itemView.findViewById(R.id.img_content);
        }
    }
}
