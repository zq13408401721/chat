package com.mychat.module.bean;

public class UserDetailsBean {

    /**
     * err : 200
     * errmsg :
     * data : {"username":"yun7","nickname":null,"sex":0,"avater":null,"level":null,"phone":null,"sign":null}
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
         * username : yun7
         * nickname : null
         * sex : 0
         * avater : null
         * level : null
         * phone : null
         * sign : null
         */

        private String username;
        private Object nickname;
        private int sex;
        private Object avater;
        private Object level;
        private Object phone;
        private Object sign;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public Object getAvater() {
            return avater;
        }

        public void setAvater(Object avater) {
            this.avater = avater;
        }

        public Object getLevel() {
            return level;
        }

        public void setLevel(Object level) {
            this.level = level;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }
    }
}
