package cn.krisez.shareroute.bean;

import com.google.gson.Gson;

public class VersionBean {
    public String versionCode;
    public String versionName;
    public String url;

    public VersionBean(String versionCode, String versionName, String url) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.url = url;
    }

    public VersionBean() {
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
