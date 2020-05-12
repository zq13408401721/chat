package com.mychat.interfaces.trends;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.bean.TrendsBean;

/**
 * 动态的契约类
 */
public interface TrendsStract {

    //发布动态的view层接口
    interface View extends IBaseView{
        //发布动态返回的接口
        void sendTrendsReturn(PublishTrendsBean result);
    }

    //发布动态的P层接口
    interface Persenter extends IBasePersenter<View>{
        //发布动态的接口
        void sendTrends(String content,String resources);
    }

    //动态列表的view层
    interface TrendsListView extends IBaseView{
        void queryTrendsReturn(TrendsBean trendsBean);
    }

    //动态类别的p层
    interface TrendsListPersenter extends IBasePersenter<TrendsListView>{
        void queryTrends(int page,int size,int trendsid);
    }

}
