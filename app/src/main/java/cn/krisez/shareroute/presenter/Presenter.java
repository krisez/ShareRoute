package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.content.Intent;

import cn.krisez.shareroute.base.IBaseView;

/**
 * Created by Krisez on 2017-12-13.
 */

public abstract class Presenter {
    private IBaseView mView;
    private Context mContext;

    public Presenter(IBaseView view, Context context) {
        mView = view;
        mContext = context;
    }

    public abstract void onCreate();

    abstract void onStart();//暂时没用到

    abstract void onStop();

    abstract  void onPause();//暂时没用到

   public void onDestroy(){
       mContext = null;
       mView = null;
   }

   abstract void attachIncomingIntent(Intent intent);//暂时没用到
}
