package com.mychat.fragments.trends;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
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

    int page=1,size=10,trendsid=0; //初始化页码，每页数量，当前的动态id

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
        recyTrends.setLayoutManager(new GridLayoutManager(context,3));
        recyTrends.setAdapter(trendsAdapter);

        //刷新的监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
        //加载更多的监听
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

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
        }
    }
}
