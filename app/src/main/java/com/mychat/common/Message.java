package com.mychat.common;

public class Message {

    public static final int MSG_TYPE_WORD = 10000; //默认的文本消息
    public static final int MSG_TYPE_IMAGE = 10001; //图片消息
    public static final int MSG_TYPE_BIGSMAILE = 10002; //聊天中的大表情
    public static final int MSG_TYPE_NORMALSMALE = 10003; //正常的图文混排的表情

    public static final int MSG_SELF = 100; //自己发的消息
    public static final int MSG_OTHER = 101; //别人发的消息

    public static final int MSG_STATUS_NORMAL = 201; //正常的消息
    public static final int MSG_STATUS_ASYNC = 202; //异步消息


}
