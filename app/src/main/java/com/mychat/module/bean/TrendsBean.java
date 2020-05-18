package com.mychat.module.bean;

import java.util.List;

public class TrendsBean {

    /**
     * err : 200
     * errmsg :
     * data : [{"id":94,"uid":"cec60303-5fb4-4fc2-bf29-1eee2ae91c31","address":null,"mobile":null,"time":2147483647,"content":"你好啊","resources":"","username":"weiwenhui","avater":"http://yun918.cn/study/public/uploadfiles/weiwenhui/mmexport1572608332601.jpg","discussNum":1,"praise":[{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"},{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"},{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"}],"discuss":[{"content":"呵呵","id":12,"discussuid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","discussusername":"yun6","targetuid":"","targetusername":""}]},{"id":93,"uid":"cec60303-5fb4-4fc2-bf29-1eee2ae91c31","address":null,"mobile":null,"time":2147483647,"content":"","resources":"http://yun918.cn/study/public/uploadfiles/weiwenhui/mmexport1577273780925.jpg","username":"weiwenhui","avater":"http://yun918.cn/study/public/uploadfiles/weiwenhui/mmexport1572608332601.jpg","discussNum":0,"praise":[],"discuss":[]},{"id":92,"uid":"6406ec6e-03d4-4829-bbff-ec7fb9146fd5","address":null,"mobile":null,"time":2147483647,"content":"今晚吃的撑了","resources":"http://yun918.cn/study/public/uploadfiles/nihao/12dab37f8831e9b499976be52b32ede8.jpeg","username":"yanglutong","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":91,"uid":"df2ec41d-65a9-46e1-a4c9-ceda6cb8720b","address":null,"mobile":null,"time":2147483647,"content":"嗯","resources":"","username":"zhangdenghui","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":90,"uid":"df2ec41d-65a9-46e1-a4c9-ceda6cb8720b","address":null,"mobile":null,"time":2147483647,"content":"嗯","resources":"","username":"zhangdenghui","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":89,"uid":"df2ec41d-65a9-46e1-a4c9-ceda6cb8720b","address":null,"mobile":null,"time":2147483647,"content":"天不刮风","resources":"","username":"zhangdenghui","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":88,"uid":"df2ec41d-65a9-46e1-a4c9-ceda6cb8720b","address":null,"mobile":null,"time":2147483647,"content":"天不刮风","resources":"","username":"zhangdenghui","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":87,"uid":"6406ec6e-03d4-4829-bbff-ec7fb9146fd5","address":null,"mobile":null,"time":2147483647,"content":"今天好","resources":"http://yun918.cn/study/public/uploadfiles/nihao/ding.jpg","username":"yanglutong","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":86,"uid":"c14bf104-eb53-4d1d-b00e-1dd5a3c98336","address":null,"mobile":null,"time":2147483647,"content":"","resources":"http://yun918.cn/study/public/uploadfiles/chen/wx_camera_1587167729832.jpg","username":"chen","avater":null,"discussNum":0,"praise":[],"discuss":[]},{"id":85,"uid":"c14bf104-eb53-4d1d-b00e-1dd5a3c98336","address":null,"mobile":null,"time":2147483647,"content":"","resources":"http://yun918.cn/study/public/uploadfiles/chen/wx_camera_1587167729832.jpg","username":"chen","avater":null,"discussNum":0,"praise":[],"discuss":[]}]
     */

    private int err;
    private String errmsg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 94
         * uid : cec60303-5fb4-4fc2-bf29-1eee2ae91c31
         * address : null
         * mobile : null
         * time : 2147483647
         * content : 你好啊
         * resources :
         * username : weiwenhui
         * avater : http://yun918.cn/study/public/uploadfiles/weiwenhui/mmexport1572608332601.jpg
         * discussNum : 1
         * praise : [{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"},{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"},{"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","username":"yun6"}]
         * discuss : [{"content":"呵呵","id":12,"discussuid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","discussusername":"yun6","targetuid":"","targetusername":""}]
         */

        private int id;
        private String uid;
        private Object address;
        private Object mobile;
        private int time;
        private String content;
        private String resources;
        private String username;
        private String avater;
        private int discussNum;
        private List<PraiseBean> praise;
        private List<DiscussBean> discuss;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getResources() {
            return resources;
        }

        public void setResources(String resources) {
            this.resources = resources;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        public int getDiscussNum() {
            return discussNum;
        }

        public void setDiscussNum(int discussNum) {
            this.discussNum = discussNum;
        }

        public List<PraiseBean> getPraise() {
            return praise;
        }

        public void setPraise(List<PraiseBean> praise) {
            this.praise = praise;
        }

        public List<DiscussBean> getDiscuss() {
            return discuss;
        }

        public void setDiscuss(List<DiscussBean> discuss) {
            this.discuss = discuss;
        }

        public static class PraiseBean {
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

        public static class DiscussBean {
            /**
             * content : 呵呵
             * id : 12
             * discussuid : d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc
             * discussusername : yun6
             * targetuid :
             * targetusername :
             */

            private String content;
            private int id;
            private String discussuid;
            private String discussusername;
            private String targetuid;
            private String targetusername;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getTargetuid() {
                return targetuid;
            }

            public void setTargetuid(String targetuid) {
                this.targetuid = targetuid;
            }

            public String getTargetusername() {
                return targetusername;
            }

            public void setTargetusername(String targetusername) {
                this.targetusername = targetusername;
            }
        }
    }
}
