package com.mychat.module.bean;

public class DiscussBean {

    /**
     * err : 成功
     * errmsg :
     * data : {}
     */

    private String err;
    private String errmsg;
    private DataBean data;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
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
