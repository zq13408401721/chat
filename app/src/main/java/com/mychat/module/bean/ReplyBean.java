package com.mychat.module.bean;

public class ReplyBean {

    /**
     * err : 200
     * errmsg :
     * data : {"replyid":7,"replyuid":"0621f985-0f2b-4fc5-93a0-e8ff9e6f0f68","replyUsername":"yun7","toReplyUid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","toReplyUsername":"yun6","content":"你说的都有理","time":1.589507959999E9}
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
        /**
         * replyid : 7
         * replyuid : 0621f985-0f2b-4fc5-93a0-e8ff9e6f0f68
         * replyUsername : yun7
         * toReplyUid : d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc
         * toReplyUsername : yun6
         * content : 你说的都有理
         * time : 1.589507959999E9
         */

        private int replyid;
        private String replyuid;
        private String replyUsername;
        private String toReplyUid;
        private String toReplyUsername;
        private String content;
        private double time;

        public int getReplyid() {
            return replyid;
        }

        public void setReplyid(int replyid) {
            this.replyid = replyid;
        }

        public String getReplyuid() {
            return replyuid;
        }

        public void setReplyuid(String replyuid) {
            this.replyuid = replyuid;
        }

        public String getReplyUsername() {
            return replyUsername;
        }

        public void setReplyUsername(String replyUsername) {
            this.replyUsername = replyUsername;
        }

        public String getToReplyUid() {
            return toReplyUid;
        }

        public void setToReplyUid(String toReplyUid) {
            this.toReplyUid = toReplyUid;
        }

        public String getToReplyUsername() {
            return toReplyUsername;
        }

        public void setToReplyUsername(String toReplyUsername) {
            this.toReplyUsername = toReplyUsername;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }
}
