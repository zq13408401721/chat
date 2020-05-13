package com.mychat.fragments.trends;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.activitys.trends.TrendsActivity;
import com.mychat.adapters.TrendsAdapter;
import com.mychat.base.BaseFragment;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.module.bean.TrendsBean;
import com.mychat.persenters.trends.TrendsPagerPersenter;
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
}
