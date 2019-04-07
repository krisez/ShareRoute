package cn.krisez.imchat.client;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 0，msg
 * 66,好友请求
 */
public class WebSocketTransfer {
    public int type;
    public String json;

    public WebSocketTransfer(int type, String json) {
        this.type = type;
        this.json = json;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
