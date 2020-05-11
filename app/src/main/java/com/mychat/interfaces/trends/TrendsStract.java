package com.mychat.interfaces.trends;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.mychat.module.bean.PublishTrendsBean;

/**
 * 动态的契约类
 */
public interface TrendsStract {

    interface View extends IBaseView{
        //发布动态返回的接口
        void sendTrendsReturn(PublishTrendsBean result);
    }

    interface Persenter extends IBasePersenter<View>{
        //发布动态的接口
        void sendTrends(String content,String resources);
    }

}
