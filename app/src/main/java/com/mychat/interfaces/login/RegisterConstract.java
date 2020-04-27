package com.mychat.interfaces.login;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;

public interface RegisterConstract {

    interface View extends IBaseView {
        //void getVerifyReturn(VerifyBean result);
    }

    interface Persenter extends IBasePersenter<View> {
        void getVerify();
    }

}
