package com.mychat.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.activitys.login.LoginActivity;
import com.mychat.fragments.trends.TrendsFragment;
import com.mychat.utils.SpUtils;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    protected ItemClickHandler itemClickHandler;
    protected View.OnClickListener clickListener;

    protected List<T> mDatas;
    protected Context mContext;


    public BaseAdapter(List<T> data, Context context){
        mDatas = data;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在ConstarinatLayout中RecylerView使用下面这种用法，保证item能满屏显示
        View view = LayoutInflater.from(mContext).inflate(getLayout(),parent,false);
        BaseViewHolder holder = new BaseViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickHandler != null){
                    itemClickHandler.itemClick(holder.getLayoutPosition(),holder);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T t = mDatas.get(position);
        bindData((BaseViewHolder) holder,t);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 刷新替换所有列表数据
     * @param list
     */
    public void updata(List<T> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 把数据添加到尾巴上
     * @param list
     */
    public void addDataFooter(List<T> list){
        mDatas.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 分页加载数据的刷新
     */
    public void refreshList(List<T> list){
        mDatas.addAll(0,list);
        notifyDataSetChanged();
    }

    public abstract int getLayout();

    //hodler是对应的item的管理的，T当前item所对应的数据
    public abstract void bindData(BaseViewHolder holder,T t);

    /**
     * 创建基类的ViewHolder
     */
    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        private SparseArray items;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            items = new SparseArray();
        }

        /**
         * 获取item中的元素
         * @param id
         * @return
         */
        public View getView(int id){
            View view = (View) items.get(id);
            if(view == null){
                view = itemView.findViewById(id);
                items.append(id,view);
            }
            return view;
        }
    }

    //设置回调接口的监听
    public void setOnItemClickHandler(ItemClickHandler clickHandler){
        this.itemClickHandler = clickHandler;
    }

    //设置接口回调监听，响应事件发生在条目中的某个组件上
    public void setOnClickListener(View.OnClickListener listener){
        this.clickListener = listener;
    }

    /**
     * 定义回调接口
     */
    public interface ItemClickHandler<T>{
        void itemClick(int position, BaseViewHolder holder);

        //不定参数 type表示业务类型
        void itemClick(int type,T...args);
    }

    /**
     * 判断用户是否登录
     */
    protected boolean checkUserLogin(){
        String username = SpUtils.getInstance().getString("username");
        if(!TextUtils.isEmpty(username)){
            return true;
        }else {
            //如果没有登录，直接调整到登录页面
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        }
        return false;
    }

}
