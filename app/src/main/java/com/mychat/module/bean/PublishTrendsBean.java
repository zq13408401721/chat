package com.mychat.module.bean;

public class PublishTrendsBean {

    /**
     * err : 30000
     * errmsg : 动态创建成功
     * data : {}
     */

    private int err;
    private String errmsg;
    private DataBean data;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
