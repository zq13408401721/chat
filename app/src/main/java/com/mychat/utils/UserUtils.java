package com.mychat.utils;

public class UserUtils {

    /**
     * 性别
     * @param sex
     * @return
     */
    public static String parseSex(int sex){
        if(sex == 0){
            return "无";
        }else if(sex == 1){
            return "男";
        }
        return "女";
    }

}
