package com.mychat.module.bean;

public class DetailsUpdateBean {

    /**
     * err : 200
     * errmsg :
     * data : 成功
     */

    private int err;
    private String errmsg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
