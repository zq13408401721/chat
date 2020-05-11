package com.mychat.persenters.trends;

import com.mychat.base.BasePersenter;
import com.mychat.common.CommonSubscriber;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.module.HttpManager;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.utils.RxUtils;

public class TrendsPersenter extends BasePersenter<TrendsStract.View> implements TrendsStract.Persenter {
    @Override
    public void sendTrends(String content, String resources) {
        addSubscribe(HttpManager.getInstance().getChatApi().sendTrends(content,resources)
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<PublishTrendsBean>(mView) {
                    @Override
                    public void onNext(PublishTrendsBean result) {
                        mView.sendTrendsReturn(result);
                    }
                }));
    }
}
