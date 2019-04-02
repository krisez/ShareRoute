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
    public String name;
    public String headUrl;

    public MessageBean() {
    }

    public MessageBean(int index, String from, String to, String type, String content, String time, String fileUrl, String address, String isRead, String name, String headUrl) {
        this.index = index;
        this.from = from;
        this.to = to;
        this.type = type;
        this.content = content;
        this.time = time;
        this.fileUrl = fileUrl;
        this.address = address;
        this.isRead = isRead;
        this.name = name;
        this.headUrl = headUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
