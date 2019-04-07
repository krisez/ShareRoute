package cn.krisez.imchat.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class AddFriendBean {
    public String ida;
    public String idb;

    public AddFriendBean(String ida, String idb) {
        this.ida = ida;
        this.idb = idb;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
