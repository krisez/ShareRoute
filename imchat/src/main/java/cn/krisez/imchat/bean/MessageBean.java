package cn.krisez.imchat.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class MessageBean {
    public int index;
    public String from;
    public String to;
    public String type;
    public String content;
    public String time;
    public String fileUrl;
    public String address;
    public String isRead;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
