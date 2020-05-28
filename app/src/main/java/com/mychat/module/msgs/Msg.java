package com.mychat.module.msgs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建msg这个对象类
 */
public class Msg implements Parcelable {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String title; //标题
    private String date; //日期 2020-5-28

    /**
     * 带参数的构造函数
     * @param title
     * @param date
     */
    public Msg(String title,String date){
        this.title = title;
        this.date = date;
    }

    protected Msg(Parcel in) {
        title = in.readString();
        date = in.readString();
    }

    public static final Creator<Msg> CREATOR = new Creator<Msg>() {
        @Override
        public Msg createFromParcel(Parcel in) {
            return new Msg(in);
        }

        @Override
        public Msg[] newArray(int size) {
            return new Msg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
    }

    public void readFromParcel(Parcel reply) {
        this.title = reply.readString();
        this.date = reply.readString();
    }
}
