package com.mychat.common;

import com.mychat.apps.MyApp;
import com.mychat.module.vo.FaceListItemVo;

import java.io.File;

public class Constant {

    public static final int FACE_SMALL_W = 40; //dp为单位
    public static final int FACE_SMALL_H = 40;

    public static final int FACE_BIG_W = 80;
    public static final int FACE_BIG_H = 80;


    public static final String self_avater = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3620016507,562397260&fm=111&gp=0.jpg";

    public static final String other_avater = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1728577482,2585850803&fm=26&gp=0.jpg";

    public static FaceListItemVo curItemVo;

    public static final String BASE_UPLOAD_IMAGE_URL = "http://yun918.cn/study/public/";


    //网络缓存的地址
    public static final String PATH_DATA = MyApp.myApp.getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/chat";

}
