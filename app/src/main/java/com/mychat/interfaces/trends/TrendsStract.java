package com.mychat.interfaces.trends;

import com.mychat.interfaces.IBasePersenter;
import com.mychat.interfaces.IBaseView;
import com.mychat.module.bean.DiscussBean;
import com.mychat.module.bean.PraiseBean;
import com.mychat.module.bean.PublishTrendsBean;
import com.mychat.module.bean.ReplyBean;
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

        void sendReplyReturn(ReplyBean replyBean);

        void sendDiscussReturn(DiscussBean discussBean);

        //点赞返回
        void sendPraiseReturn(PraiseBean praiseBean);
    }

    //动态类别的p层
    interface TrendsListPersenter extends IBasePersenter<TrendsListView>{
        void queryTrends(int page,int size,int trendsid);

        /**
         * 回复数据的接口
         * @param trendsid
         * @param discussid
         * @param targettype
         * @param targetuid
         * @param content
         */
        void sendReply(int trendsid,int discussid,int targettype,String targetuid,String content);

        /**
         * 评论
         * @param trendsid
         * @param content
         */
        void sendDiscuss(int trendsid,String trendsuid,String content);

        /**
         * 点赞或取消点赞
         * @param trendsid
         * @param type 0 点赞 1取消点赞
         */
        void sendPraise(int trendsid,String trendsuid,int type);
    }

}
