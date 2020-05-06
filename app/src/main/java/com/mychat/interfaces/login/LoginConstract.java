package com.mychat.interfaces.login;


import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.mychat.module.bean.UserInfoBean;

public interface LoginConstract {

    interface View extends IBaseView {
        void loginReturn(UserInfoBean result);
    }

    interface Persenter extends IBasePersenter<View> {
        void login(String username, String password);
    }

}
