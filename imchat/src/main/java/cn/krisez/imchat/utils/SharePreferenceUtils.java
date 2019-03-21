package cn.krisez.imchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharePreferenceUtils {

    private Context mContext;

    private SharePreferenceUtils(Context context){
        this.mContext = context;
    }

    public static SharePreferenceUtils obj(Context context){
        return new SharePreferenceUtils(context);
    }

    public void saveUser(String id){
        SharedPreferences.Editor editor = mContext.getSharedPreferences("im_id", Context.MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.apply();
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("im_id",Context.MODE_PRIVATE);
        return sharedPreferences.getString("id","");
    }
}
