package cn.krisez.shareroute.bean;


import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class User {
    public String id;
    public String name;
    public String mobile;
    public String avatar;
    public String realName;

    public String getMobile() {
        StringBuilder sb = new StringBuilder(mobile);
        sb.replace(3,7,"****");
        return sb.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
