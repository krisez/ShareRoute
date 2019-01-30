package cn.krisez.shareroute;

import android.app.Application;
import android.content.Context;

import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;


public class APP extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        String url  = AppConfig.HOST + ":" + AppConfig.PORT + "/";
        NetWorkUtils.INSTANCE().url(url);
    }

    public static Context getContext(){
        return sContext;
    }
}
