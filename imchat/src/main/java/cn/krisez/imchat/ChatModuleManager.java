package cn.krisez.imchat;

import android.content.Context;
import android.content.Intent;

import cn.krisez.imchat.client.ImClient;
import cn.krisez.imchat.ui.ChatsActivity;
import cn.krisez.imchat.utils.SharePreferenceUtils;

/**
 * 外部对本module的访问
 */
public class ChatModuleManager {
    //外部打开聊天界面
    public static void open(Context context, String id) {
        Intent intent = new Intent(context, ChatsActivity.class);
        SharePreferenceUtils.obj(context).saveUser(id);
        context.startActivity(intent);
    }
    public static void connect(String id) {
        ImClient.getInstance(id).connect();
    }
}
