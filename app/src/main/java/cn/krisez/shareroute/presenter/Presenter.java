package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import cn.krisez.kotlin.activity.MainActivity;
import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.base.HandleType;
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

    public void updatePw(String pw){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).updatePw(pw))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("class", MainActivity.class);
                        mView.handle(HandleType.INTENT,bundle);
                    }

                    @Override
                    public void onFailed(String s) {

                    }
                });
    }
}
