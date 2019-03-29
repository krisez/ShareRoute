package cn.krisez.shareroute;

import android.app.Application;
import android.content.Context;

import cn.krisez.imchat.ChatModuleManager;
import cn.krisez.imchat.client.ImClient;
import cn.krisez.imchat.db.DbUtils;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.shareroute.utils.Const;
import cn.krisez.shareroute.utils.SPUtil;


public class APP extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        String url = AppConfig.HOST + ":" + AppConfig.PORT;
        NetWorkUtils.INSTANCE().url(url);
        if (SPUtil.getUser() != null) {
            ChatModuleManager.connect(SPUtil.getUser().id);
        }
        ChatModuleManager.initMsgManager(this);

    }

    public static Context getContext() {
        return sContext;
    }
}
