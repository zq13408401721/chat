package com.mychat.interfaces.own;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.mychat.module.bean.UserDetailsBean;

public interface UserConstact {

    interface DetailsView extends IBaseView{
        void getDetailsReturn(UserDetailsBean result);
    }

    interface DetailsPersenter extends IBasePersenter<DetailsView>{
        void getDetails();
    }

}
