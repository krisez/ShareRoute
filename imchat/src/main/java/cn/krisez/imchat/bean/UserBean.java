package cn.krisez.imchat.bean;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class UserBean {
    public String id;
    public String name;
    public String mobile;
    public String avatar;
    public String realName;
    public boolean request = false;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
