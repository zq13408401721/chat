package com.mychat.apps;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.mychat.services.IMService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {

    public static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        //initUM();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

       /* IMService imService = new IMService();
        imService.init();*/
    }

    private void initUM(){
        //以清单文件中的频道设置为准 s1=null
        //UMConfigure.init(this, "5e01a81b570df38f46000ec5", null, UMConfigure.DEVICE_TYPE_PHONE, null);
        //另一种设置方式  需要在清单文件中配置友盟app-key
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        //手动统计模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        UMConfigure.setLogEnabled(true);
        String channel = getChannel(this);
        Toast.makeText(this, channel, Toast.LENGTH_LONG).show();
    }

    private String getChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("CHANNEL_NAME");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }
}
