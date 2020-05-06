package com.mychat.module.bean;

public class UserInfoBean {

    /**
     * err : 200
     * errmsg :
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Inl1biIsInBhc3N3b3JkIjoiMTIzNDU2IiwidWlkIjoiMTE0Njc4OTUtYzE2Mi00ZmQ1LWE4MDgtZmNhYmU0NDZjYjA4IiwiaWF0IjoxNTg4NzI2OTkxfQ.skIHWbB5CuEGgOrNpCegllDwd4AvZtWUPXr6reuRyyM","username":"yun","avater":"http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1589201615&t=4abf9992c4d95bad33c05eaf370b257e","phone":null}
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
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Inl1biIsInBhc3N3b3JkIjoiMTIzNDU2IiwidWlkIjoiMTE0Njc4OTUtYzE2Mi00ZmQ1LWE4MDgtZmNhYmU0NDZjYjA4IiwiaWF0IjoxNTg4NzI2OTkxfQ.skIHWbB5CuEGgOrNpCegllDwd4AvZtWUPXr6reuRyyM
         * username : yun
         * avater : http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1589201615&t=4abf9992c4d95bad33c05eaf370b257e
         * phone : null
         */

        private String token;
        private String username;
        private String avater;
        private Object phone;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }
    }
}
