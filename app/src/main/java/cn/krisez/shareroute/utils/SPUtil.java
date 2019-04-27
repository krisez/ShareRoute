package cn.krisez.shareroute.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cn.krisez.shareroute.APP;
import cn.krisez.shareroute.bean.User;

public class SPUtil {

    public static void saveUser(User user) {
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        if (null == user) {
            editor.clear();
            editor.apply();
            return;
        }
        editor.putString("user", user.toString());
        editor.apply();
    }

    public static User getUser() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("user", "");
        return new Gson().fromJson(json, User.class);
    }

    public static void setOtherInfo(String id) {
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("other_info", Context.MODE_PRIVATE).edit();
        editor.putString("info_id", id);
        editor.apply();
    }

    public static String getOtherInfo() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("other_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("info_id", "");
    }

    public static void saveUserPassword(String pw) {
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("user_pw", Context.MODE_PRIVATE).edit();
        editor.putString("pw", pw + "SR");
        editor.apply();
    }

    public static String getUserPassword() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("user_pw", Context.MODE_PRIVATE);
        return sharedPreferences.getString("pw", "");
    }

    public static void setEmetgency(String contact) {
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE).edit();
        editor.putString("emergency", contact);
        editor.apply();
    }

    public static String getEmergency() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        return sharedPreferences.getString("emergency", "");
    }

    public static void setAccessLocate(boolean checked) {
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE).edit();
        editor.putBoolean("accessLocate", checked);
        editor.apply();
    }

    public static boolean isAccessLocate() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("accessLocate", false);
    }

    public static void saveUpdateAppOpreation(){
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE).edit();
        editor.putLong("updateApp",System.currentTimeMillis());
        editor.apply();
    }

    public static boolean checkUpdate() {
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        long time = sharedPreferences.getLong("updateApp", 0L);
        return System.currentTimeMillis() - time > 24 * 60 * 60 * 1000;
    }
}
