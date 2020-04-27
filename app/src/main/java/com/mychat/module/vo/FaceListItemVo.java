package com.mychat.module.vo;

public class FaceListItemVo {

    private int faceId;  //表情的id
    private String name; //表情的名字
    private String tag; //表情的标记
    private int position;

    public int getFaceType() {
        return faceType;
    }

    public void setFaceType(int faceType) {
        this.faceType = faceType;
    }

    private int faceType; //表情的类型 图文混排 /  常规

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
