package com.mychat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mychat.apps.MyApp;
import com.mychat.R;
import com.mychat.SmileyParser;
import com.mychat.common.Constant;
import com.mychat.common.Message;
import com.mychat.module.vo.ChatMsgBean;
import com.mychat.utils.SpUtils;
import com.mychat.widgets.ImgViewLoad;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    String uid;
    List<ChatMsgBean> list;
    Context context;

    ChatItemClickListener chatItemClickListener;

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
        if(chatMsgBean.getMsgType() == Message.MSG_TYPE_WORD){  //聊天的图文混排显示
            CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(chatMsgBean.getContent());
            chatVH.txtContent.setText(chat);
            chatVH.imgContent.setVisibility(View.GONE);
            chatVH.txtContent.setVisibility(View.VISIBLE);
        }else if(chatMsgBean.getMsgType() == Message.MSG_TYPE_NORMALSMALE){  //带有动画的图文混排
            CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(chatVH.txtContent,chatMsgBean.getContent(),SmileyParser.FACE_TYPE_1);
            chatVH.txtContent.setText(chat);
            chatVH.imgContent.setVisibility(View.GONE);
            chatVH.txtContent.setVisibility(View.VISIBLE);
        }else if(chatMsgBean.getMsgType() == Message.MSG_TYPE_BIGSMAILE){  //聊天的大表情显示
            CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(chatVH.txtContent,chatMsgBean.getContent(),SmileyParser.FACE_TYPE_2);
            chatVH.txtContent.setText(chat);
            chatVH.imgContent.setVisibility(View.GONE);
            chatVH.txtContent.setVisibility(View.VISIBLE);
        }else if(chatMsgBean.getMsgType() == Message.MSG_TYPE_IMAGE){
            chatVH.imgViewLoad.onCreatedView();
            chatVH.imgContent.setVisibility(View.VISIBLE);
            chatVH.txtContent.setVisibility(View.GONE);
            chatVH.imgViewLoad.setUrl(chatMsgBean.getContent());
            if(chatMsgBean.getStatus() == Message.MSG_STATUS_ASYNC){
                chatVH.imgViewLoad.setProgressBarVisible(View.VISIBLE);
            }else{
                chatVH.imgViewLoad.setProgressBarVisible(View.GONE);
            }
            //Glide.with(context).load(chatMsgBean.getContent()).into(chatVH.imgContent);
            chatVH.imgContent.setTag(chatMsgBean);
            chatVH.imgContent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(chatItemClickListener != null){
                        ChatMsgBean bean = (ChatMsgBean) v.getTag();
                        chatItemClickListener.click(bean,v);
                    }
                }
            });
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

    /**
     * 聊天内容的点击
     * @param listener
     */
    public void addChatItemClickListener(ChatItemClickListener listener){
        chatItemClickListener = listener;
    }

    class ChatVH extends RecyclerView.ViewHolder{

        ImageView imgHeader;
        TextView txtName;
        TextView txtContent;
        ConstraintLayout imgContent;
        ImgViewLoad imgViewLoad;

        public ChatVH(@NonNull View itemView) {
            super(itemView);
            imgHeader = itemView.findViewById(R.id.img_header);
            txtName = itemView.findViewById(R.id.txt_name);
            txtContent = itemView.findViewById(R.id.txt_content);
            imgContent = itemView.findViewById(R.id.layout_img);
            imgViewLoad = itemView.findViewById(R.id.widgetImg);
        }
    }

    public interface ChatItemClickListener{
        void click(ChatMsgBean bean,View view);
    }

}
