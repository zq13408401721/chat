package com.mychat.module.vo;

public class FaceTabVo {

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    private int faceId;  //tab id
    private int postion; //tab对应的下标


}
