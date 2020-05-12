package com.mychat.persenters.trends;

import com.mychat.base.BasePersenter;
import com.mychat.common.CommonSubscriber;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.interfaces.trends.TrendsStract.TrendsListPersenter;
import com.mychat.module.HttpManager;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.bean.TrendsBean;
import com.mychat.utils.RxUtils;

/**
 * 获取动态列表的操作
 */
public class TrendsPagerPersenter extends BasePersenter<TrendsStract.TrendsListView> implements TrendsListPersenter {

    @Override
    public void queryTrends(int page, int size, int trendsid) {
        addSubscribe(HttpManager.getInstance().getChatApi().queryTrends(page,size,trendsid)
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<TrendsBean>(mView) {
                    @Override
                    public void onNext(TrendsBean result) {
                        mView.queryTrendsReturn(result);
                    }
                }));
    }

}
