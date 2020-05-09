package com.mychat.module.vo;

public class TrendsVo {

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private int type; //0 无用(加号)  1有用
    private String path; //图片的本地路径

}
