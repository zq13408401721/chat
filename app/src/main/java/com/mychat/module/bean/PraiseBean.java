package com.mychat.module.bean;

public class PraiseBean {

    /**
     * err : 200
     * errmsg :
     * data : {}
     * obj : {"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"}
     */

    private int err;
    private String errmsg;
    private DataBean data;
    private ObjBean obj;

    public int getTrendsid() {
        return trendsid;
    }

    public void setTrendsid(int trendsid) {
        this.trendsid = trendsid;
    }

    private int trendsid;

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class DataBean {
    }

    public static class ObjBean {
        /**
         * uid : d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc
         * username : yun6
         */

        private String uid;
        private String username;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
