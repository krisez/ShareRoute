package cn.krisez.imchat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import cn.krisez.imchat.client.ImClient;
import cn.krisez.imchat.db.DbUtils;
import cn.krisez.imchat.ui.activity.IMActivity;
import cn.krisez.imchat.utils.SharePreferenceUtils;

/**
 * 外部对本module的访问
 */
public class ChatModuleManager {
    //外部打开聊天界面
    public static void open(Context context, String id) {
        Intent intent = new Intent(context, IMActivity.class);
        SharePreferenceUtils.obj(context).saveUser(id);
        context.startActivity(intent);
    }
    public static void connect(String id) {
        ImClient.getInstance(id);
    }

    /**
     * 初始化IM信息 得到applicationContext
     * 数据库
     */
    public static void initMsgManager(Application application){
        DbUtils.init(application);
    }

    /**
     * 关闭im
     */
    public static void close() {
        ImClient.getInstance("").close();
    }
}
