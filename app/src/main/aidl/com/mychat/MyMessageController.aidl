// MyMessageController.aidl
package com.mychat;

import com.mychat.module.msgs.Msg;

// Declare any non-default types here with import statements

interface MyMessageController {

    //获取msg对象
    List<Msg>  msgList();

    //添加msg对象
    void addMsgInOut(inout Msg msg);


}
