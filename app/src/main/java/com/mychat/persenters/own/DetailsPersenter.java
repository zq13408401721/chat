package com.mychat.persenters.own;

import com.mychat.base.BasePersenter;
import com.mychat.common.CommonSubscriber;
import com.mychat.interfaces.own.UserConstact;
import com.mychat.module.HttpManager;
import com.mychat.module.bean.DetailsUpdateBean;
import com.mychat.module.bean.UserDetailsBean;
import com.mychat.module.bean.UserInfoBean;
import com.mychat.utils.RxUtils;

import java.util.Map;

public class DetailsPersenter extends BasePersenter<UserConstact.DetailsView> implements UserConstact.DetailsPersenter {
    @Override
    public void getDetails() {
        addSubscribe(HttpManager.getInstance().getChatApi().getUserDetails()
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<UserDetailsBean>(mView) {
                    @Override
                    public void onNext(UserDetailsBean result) {
                        mView.getDetailsReturn(result);
                    }
                }));
    }

    /**
     * 更新用户信息
     * @param map
     */
    @Override
    public void updateDetails(Map<String, String> map) {
        addSubscribe(HttpManager.getInstance().getChatApi().updateUserDetails(map)
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<DetailsUpdateBean>(mView) {
                    @Override
                    public void onNext(DetailsUpdateBean result) {
                        mView.updateDetailsReturn(result);
                    }
                }));
    }
}
