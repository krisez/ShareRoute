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
        NetWorkUtils.INSTANCE().url("http://192.168.137.1:8080/");
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).login("")).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public static Context getContext(){
        return sContext;
    }
}
