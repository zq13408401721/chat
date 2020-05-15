package com.mychat.module.bean;

import java.util.List;

public class TrendsBean {

    /**
     * err : 200
     * errmsg :
     * data : [{"id":2,"uid":"0621f985-0f2b-4fc5-93a0-e8ff9e6f0f68","address":"上海","mobile":"iphoneX","time":0,"content":"优衣库","resources":"","username":"yun7","avater":"http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1589201615&t=4abf9992c4d95bad33c05eaf370b257e","discuss":[{"discussuid":"27b6391f-bd12-4a15-91ef-f3a0bc6b5277","discussusername":"lizhijun321321","targetuid":"","targetusername":"","conent":"服务遗留"}]},{"id":1,"uid":"d5eb8c5a-a078-4f6f-97aa-c2fa7e04ccbc","address":"北京","mobile":"华为p30","time":0,"content":"名创优品","resources":"","username":"yun6","avater":"http://yun918.cn/study/public/uploadfiles/yun6/Screenshot_2020-03-04-12-58-43.png","discuss":[{"discussuid":"470d3885-2dd4-4541-b99e-be640dfe4b71","discussusername":"xiaohuya","targetuid":"","targetusername":"","conent":"666"},{"discussuid":"0ed9000d-fcd0-4fc6-9eed-c46d00585056","discussusername":"xueling","targetuid":"470d3885-2dd4-4541-b99e-be640dfe4b71","targetusername":"xiaohuya","conent":"价格便宜"},{"discussuid":"62638f07-8d91-447d-98bf-a3c37f31a3d1","discussusername":"zhangqiankun","targetuid":"","targetusername":"","conent":"质量不错"}]}]
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
         * id : 2
         * uid : 0621f985-0f2b-4fc5-93a0-e8ff9e6f0f68
         * address : 上海
         * mobile : iphoneX
         * time : 0
         * content : 优衣库
         * resources :
         * username : yun7
         * avater : http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1589201615&t=4abf9992c4d95bad33c05eaf370b257e
         * discuss : [{"discussuid":"27b6391f-bd12-4a15-91ef-f3a0bc6b5277","discussusername":"lizhijun321321","targetuid":"","targetusername":"","conent":"服务遗留"}]
         */

        private int id;
        private String uid;
        private String address;
        private String mobile;
        private int time;
        private String content;
        private String resources;
        private String username;
        private String avater;
        private List<DiscussBean> discuss;

        public int getDiscussNum() {
            return discussNum;
        }

        public void setDiscussNum(int discussNum) {
            this.discussNum = discussNum;
        }

        private int discussNum;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
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

        public List<DiscussBean> getDiscuss() {
            return discuss;
        }

        public void setDiscuss(List<DiscussBean> discuss) {
            this.discuss = discuss;
        }

        public static class DiscussBean {
            /**
             * discussuid : 27b6391f-bd12-4a15-91ef-f3a0bc6b5277
             * discussusername : lizhijun321321
             * targetuid :
             * targetusername :
             * conent : 服务遗留
             */

            private String discussuid;
            private String discussusername;
            private String targetuid;
            private String targetusername;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            private int id;

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

            public String getContent() {
                return content;
            }

            public void setContent(String conent) {
                this.content = content;
            }
        }
    }
}
