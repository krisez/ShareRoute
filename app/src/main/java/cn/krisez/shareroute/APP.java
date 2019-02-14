package cn.krisez.shareroute;

import android.app.Application;
import android.content.Context;

import cn.krisez.network.NetWorkUtils;
import cn.krisez.shareroute.utils.Const;
import cn.krisez.shareroute.utils.SPUtil;


public class APP extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        String url  = AppConfig.HOST  /*+ ":" + AppConfig.PORT*/ ;
        NetWorkUtils.INSTANCE().url(url);
        Const.uploadLocation = SPUtil.isAutoUploadLocation();
    }

    public static Context getContext(){
        return sContext;
    }
}
