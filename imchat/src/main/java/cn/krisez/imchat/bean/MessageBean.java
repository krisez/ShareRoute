package cn.krisez.imchat.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class MessageBean {
    public String from;
    public String to;
    public String time;
    public String content;
    public String type;
    public String fileUrl;
    public String address;
    public String idread;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
