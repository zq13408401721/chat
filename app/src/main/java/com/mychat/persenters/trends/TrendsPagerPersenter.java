package com.mychat.persenters.trends;

import com.mychat.base.BasePersenter;
import com.mychat.common.CommonSubscriber;
import com.mychat.interfaces.trends.TrendsStract;
import com.mychat.interfaces.trends.TrendsStract.TrendsListPersenter;
import com.mychat.module.HttpManager;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.bean.ReplyBean;
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

    /**
     * 回复接口
     * @param trendsid
     * @param discussid
     * @param targettype
     * @param targetuid
     * @param content
     */
    @Override
    public void sendReply(int trendsid, int discussid, int targettype, String targetuid, String content) {
        addSubscribe(HttpManager.getInstance().getChatApi().sendReply(trendsid,discussid,targettype,targetuid,content)
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<ReplyBean>(mView) {
                    @Override
                    public void onNext(ReplyBean result) {
                        mView.sendReplyReturn(result);
                    }
                }));
    }

}
