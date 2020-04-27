package com.mychat.interfaces.login;


import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;

public interface LoginConstract {

    interface View extends IBaseView {
        //void loginReturn(UserBean result);
    }

    interface Persenter extends IBasePersenter<View> {
        void login(String nickname, String password);
    }

}
