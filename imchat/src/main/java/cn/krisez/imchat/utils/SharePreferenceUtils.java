package cn.krisez.imchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import cn.krisez.imchat.bean.UserBean;

public class SharePreferenceUtils {

    private Context mContext;

    private SharePreferenceUtils(Context context){
        this.mContext = context;
    }

    public static SharePreferenceUtils obj(Context context){
        return new SharePreferenceUtils(context);
    }

    public void saveUser(String user){
        SharedPreferences.Editor editor =mContext.getSharedPreferences("im",Context.MODE_PRIVATE).edit();
        editor.putString("user",user);
        editor.apply();
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("im",Context.MODE_PRIVATE);
        UserBean userBean = new Gson().fromJson(sharedPreferences.getString("user",""),UserBean.class);
        return userBean.id;
    }

    public String getUserMobile(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("im",Context.MODE_PRIVATE);
        UserBean userBean = new Gson().fromJson(sharedPreferences.getString("user",""),UserBean.class);
        return userBean.mobile;
    }
}
