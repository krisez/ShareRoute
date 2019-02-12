package cn.krisez.shareroute.bean;


import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class User {
    public String id;
    public String name;
    public String mobile;
    public String avatar;
    public String realName;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
