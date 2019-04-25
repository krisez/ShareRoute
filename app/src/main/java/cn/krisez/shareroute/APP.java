package cn.krisez.shareroute;

import android.app.Application;
import android.content.Context;

import cn.krisez.imchat.ChatModuleManager;
import cn.krisez.network.NetWorkUtils;


public class APP extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
//        String url = AppConfig.HOST;
        String url = AppConfig.HOST + ":" + AppConfig.PORT;
        NetWorkUtils.INSTANCE().url(url);
        ChatModuleManager.initMsgManager(this);

    }

    public static Context getContext() {
        return sContext;
    }
}
