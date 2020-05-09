package com.mychat.module.vo;

import java.util.List;

public class SaveDataBean {

    /**
     * word : xxxxxxxx
     * imgs : [{"path":"xxxx/xx.jpg"},{"path":"xxxxx/xx1.jpg"}]
     */

    private String word;
    private List<ImgsBean> imgs;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<ImgsBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgsBean> imgs) {
        this.imgs = imgs;
    }

    public static class ImgsBean {
        /**
         * path : xxxx/xx.jpg
         */

        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
