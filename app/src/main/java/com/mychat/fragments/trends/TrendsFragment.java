package com.mychat.fragments.trends;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.activitys.trends.TrendsActivity;
import com.mychat.adapters.TrendsAdapter;
import com.mychat.base.BaseAdapter;
import com.mychat.base.BaseFragment;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.module.bean.DiscussBean;
import com.mychat.module.bean.PraiseBean;
import com.mychat.module.bean.ReplyBean;
import com.mychat.module.bean.TrendsBean;
import com.mychat.persenters.trends.TrendsPagerPersenter;
import com.mychat.utils.SpUtils;
import com.mychat.utils.SystemUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TrendsFragment extends BaseFragment<TrendsStract.TrendsListPersenter> implements TrendsStract.TrendsListView {

    public static final int TYPE_DISCUSS = 100; //评论
    public static final int TYPE_REPLY = 200; //评论
    public static final int TYPE_PRAISE = 300; //点赞



    @BindView(R.id.img_trends)
    ImageView imgTrends;

    @BindView(R.id.layout_trends_title)
    ConstraintLayout layoutTrendsTitle;
    @BindView(R.id.recy_trends)
    RecyclerView recyTrends;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    int page=0,size=10,trendsid=0; //初始化页码，每页数量，当前的动态id
    boolean ismore=true;  //是否有更多数据

    List<TrendsBean.DataBean> list; //动态的集合数据
    TrendsAdapter trendsAdapter; //动态的适配器

    Dialog dialog; //输入窗口
    int curTrendsId;


    @Override
    protected int getLayout() {
        return R.layout.fragment_trends;
    }

    @Override
    protected void initView() {

        list = new ArrayList<>();
        trendsAdapter = new TrendsAdapter(list,context);
        recyTrends.setLayoutManager(new LinearLayoutManager(context));
        recyTrends.setAdapter(trendsAdapter);
        //动态条目的点击监听
        trendsAdapter.setOnItemClickHandler(new BaseAdapter.ItemClickHandler() {
            @Override
            public void itemClick(int position, BaseAdapter.BaseViewHolder holder) {
                //打开动态的详情页面


            }
            //不定参数
            @Override
            public void itemClick(int type,Object[] args) {
                if(type == TYPE_DISCUSS){
                    if(args.length > 0) {
                        curTrendsId = (int) args[0];
                        //评论
                        openDiscussWindow(curTrendsId,0,0,null);
                    }
                }else if(type == TYPE_PRAISE){
                    if(args.length >= 2){
                        sendPraise((int)args[0],(int)args[1]);
                    }
                }else if(type == TYPE_REPLY){
                    //打开动态的评论或者回复的输入框
                    if(args.length >= 4){
                        openDiscussWindow((int)args[0],(int)args[1],(int)args[2],String.valueOf(args[3]));
                    }
                }

            }
        });

        /**
         * 刷新的监听
         * 刷新列表 page=0 trendsid=0表示初始化加载数据
         * page=0 trendsid>0 表示下拉刷新数据
         *
         */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                if(list.size() > 0){
                    trendsid = list.get(0).getId();
                }else{
                    trendsid = 0;
                }
                persenter.queryTrends(page,size,trendsid);
            }
        });
        /**
         * 加载更多的监听
         * page>0 trendsid > 0
         */
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(list.size() == 0){
                    //如何列表没有数据 直接关闭
                    refreshLayout.finishLoadMore();
                    return;
                }
                if(ismore){
                    page ++;
                    trendsid = list.get(list.size()-1).getId();
                    persenter.queryTrends(page,size,trendsid);
                }else{
                    Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void initData() {
        //初始化加载动态数据
        persenter.queryTrends(page,size,trendsid);
    }

    @Override
    protected TrendsStract.TrendsListPersenter createPersenter() {
        return new TrendsPagerPersenter();
    }

    @OnClick(R.id.img_trends)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_trends:
                openPublish();
                break;
        }
    }

    private void openPublish() {
        Intent intent = new Intent(context, TrendsActivity.class);
        startActivity(intent);
    }

    //接收动态获取返回的接口
    @Override
    public void queryTrendsReturn(TrendsBean trendsBean) {
        if(trendsBean.getErr() == 200){
            //trendsBean
            if(trendsBean.getData() != null && trendsBean.getData().size() > 0){
                //page为1刷新当前页面的数据
                if(page == 0){
                    refreshLayout.finishRefresh();
                    //向列表的头中插入数据
                    trendsAdapter.refreshList(trendsBean.getData());
                }else{
                    //数据上拉加载更多，判断数据是否已经加载完成
                    if(trendsBean.getData().size() < size){
                        ismore = false;
                    }
                    refreshLayout.finishLoadMore();
                    //向列表的尾部插入数据
                    trendsAdapter.addDataFooter(trendsBean.getData());
                }
            }
        }else if(trendsBean.getErr() == 30002){
            Toast.makeText(context, trendsBean.getErrmsg(), Toast.LENGTH_SHORT).show();
            //处理上拉加载数据，没有更多数据的返回结果
            if(page != 0){
                refreshLayout.finishLoadMore();
                ismore = false;
            }else{
                refreshLayout.finishRefresh();
            }
        }
    }

    /**
     * 回复数据返回
     * @param replyBean
     */
    @Override
    public void sendReplyReturn(ReplyBean replyBean) {
        if(dialog != null) dialog.dismiss();
        dialog = null;
        if(replyBean.getErr() == 200){
            //回复成功把回复的数据添加到对一个的回复集合
            TrendsBean.DataBean.DiscussBean discussBean = new TrendsBean.DataBean.DiscussBean();
            discussBean.setId(replyBean.getData().getReplyid());
            discussBean.setDiscussuid(replyBean.getData().getReplyuid());
            discussBean.setDiscussusername(replyBean.getData().getReplyUsername());
            discussBean.setTargetuid(replyBean.getData().getToReplyUid());
            discussBean.setTargetusername(replyBean.getData().getToReplyUsername());
            discussBean.setContent(replyBean.getData().getContent());
            int i;
            for(i=0; i<list.size(); i++){
                if(list.get(i).getId() == curTrendsId){
                    list.get(i).getDiscuss().add(discussBean);
                    break;
                }
            }
            //局部刷新列表条目的某条数据
            trendsAdapter.notifyItemChanged(i);
        }
    }

    /**
     * 评论数据返回
     * @param discussBean
     */
    @Override
    public void sendDiscussReturn(DiscussBean discussBean) {
        if(dialog != null) dialog.dismiss();
        dialog = null;





    }

    /*****************************************评论回复输窗口**********************************/

    /**
     * 打开输入框
     */
    private void openDiscussWindow(int trendsid,int discussid,int targettype,String targetuid){
        if(dialog == null){
            curTrendsId = trendsid;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_input_window,null);
            dialog = new AlertDialog.Builder(context).setView(view).create();
            EditText txtInput = view.findViewById(R.id.txt_input);
            TextView txtSend = view.findViewById(R.id.txt_send);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager m = activity.getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            dialog.getWindow().setAttributes(p); //设置生效
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog = null;
                }
            });

            //输入文本框的提示信息显示
            if(discussid == 0 && targetuid == null){
                //评论
                txtInput.setHint("评论");
            }else{
                //回复
                txtInput.setHint("回复xxx:");
            }

            //设置打开软键盘
            SystemUtils.setKeyBroad(activity,true,txtInput);
            //发送的监听
            txtSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String word = txtInput.getText().toString();
                    if(TextUtils.isEmpty(word)){
                        return;
                    }
                    //向后退回复接口传递数据
                    if(discussid == 0 && targetuid == null){
                        persenter.sendDiscuss(trendsid,word);
                    }else{
                        persenter.sendReply(trendsid,discussid,targettype,targetuid,word);
                    }
                }
            });
        }

    }


    /****************************点赞操作**************************/

    /**
     * 点赞返回
     * @param praiseBean
     */
    @Override
    public void sendPraiseReturn(PraiseBean praiseBean) {
        List<TrendsBean.DataBean.PraiseBean> praiseList=null;
        for(TrendsBean.DataBean item:list){
            if(item.getId() == praiseBean.getData().getTrendsid()){
                praiseList = item.getPraise();
            }
        }
        String username = SpUtils.getInstance().getString("username");
        //40003表示取消点赞成功
        if(praiseBean.getErr() == 40003){
            //成功取消点赞
            if(praiseList != null){
                for(TrendsBean.DataBean.PraiseBean item:praiseList){
                    if(item.getUsername().equals(username)){
                        praiseList.remove(item); //删除取消点赞的用户数据
                        break;
                    }
                }
                trendsAdapter.notifyDataSetChanged();
            }
        }else if(praiseBean.getErr() == 200){
            if(praiseList != null) {
                //成添加点赞
                TrendsBean.DataBean.PraiseBean userinfo = new TrendsBean.DataBean.PraiseBean();
                userinfo.setUid(praiseBean.getData().getUid());
                userinfo.setUsername(praiseBean.getData().getUsername());
                praiseList.add(userinfo);
                trendsAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 发送点赞或取消点赞
     * @param trendsid
     * @param type  0 点赞 1取消点赞
     */
    private void sendPraise(int trendsid,int type){
        if(trendsid > 0){
            persenter.sendPraise(trendsid,type);
        }
    }


}
