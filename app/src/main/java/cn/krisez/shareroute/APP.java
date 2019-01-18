package cn.krisez.shareroute;

import android.app.Application;
import android.content.Context;

import cn.krisez.network.NetWorkUtils;


public class APP extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        NetWorkUtils.INSTANCE().url("http://192.168.137.1:8080/");
    }

    public static Context getContext(){
        return sContext;
    }
}
