package com.mychat.module.bean;

public class DiscussBean {

    /**
     * err : 成功
     * errmsg :
     * data : {"discussid":89,"discussuid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","discussusername":"yun6","content":"测试评论是否能提交"}
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
         * discussid : 89
         * discussuid : d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc
         * discussusername : yun6
         * content : 测试评论是否能提交
         */

        private int discussid;
        private String discussuid;
        private String discussusername;
        private String content;

        public int getDiscussid() {
            return discussid;
        }

        public void setDiscussid(int discussid) {
            this.discussid = discussid;
        }

        public String getDiscussuid() {
            return discussuid;
        }

        public void setDiscussuid(String discussuid) {
            this.discussuid = discussuid;
        }

        public String getDiscussusername() {
            return discussusername;
        }

        public void setDiscussusername(String discussusername) {
            this.discussusername = discussusername;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
