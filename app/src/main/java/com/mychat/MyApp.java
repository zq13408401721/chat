package com.mychat;

import android.app.Application;

public class MyApp extends Application {

    public static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }
}
