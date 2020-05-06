package com.mychat.persenters.login;

import com.mychat.base.BasePersenter;
import com.mychat.common.CommonSubscriber;
import com.mychat.interfaces.login.LoginConstract;
import com.mychat.module.HttpManager;
import com.mychat.module.bean.UserInfoBean;
import com.mychat.utils.RxUtils;

public class LoginPersenter extends BasePersenter<LoginConstract.View> implements LoginConstract.Persenter {
    @Override
    public void login(String username, String password) {
        addSubscribe(HttpManager.getInstance().getChatApi().login(username,password)
        .compose(RxUtils.rxScheduler())
        .subscribeWith(new CommonSubscriber<UserInfoBean>(mView) {
            @Override
            public void onNext(UserInfoBean userInfoBean) {
                mView.loginReturn(userInfoBean);
            }
        }));
    }
}
