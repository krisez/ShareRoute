package cn.krisez.shareroute.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.shareroute.APP;
import cn.krisez.shareroute.bean.User;

public class SPUtil {

    public static void saveUser(User user){
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("user",Context.MODE_PRIVATE).edit();
        if(null==user){
            editor.clear();
            editor.apply();
            return;
        }
        editor.putString("user",user.toString());
        editor.apply();
    }

    public static User getUser(){
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("user","");
        return new Gson().fromJson(json,User.class);
    }

    public static void setOtherInfo(String id){
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("other_info",Context.MODE_PRIVATE).edit();
        editor.putString("info_id",id);
        editor.apply();
    }

    public static String getOtherInfo(){
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("other_info",Context.MODE_PRIVATE);
        return sharedPreferences.getString("info_id","");
    }

}
