package cn.krisez.shareroute.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.krisez.shareroute.APP;

public class SPUtil {

    public static void saveUserId(String id){
        SharedPreferences.Editor editor = APP.getContext().getSharedPreferences("user",Context.MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.apply();
    }

    public static String getUserId(){
        SharedPreferences sharedPreferences = APP.getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("id","");
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
