package cn.krisez.framework.base;

import android.content.Context;

import android.os.Bundle;

/**
 * Created by Krisez on 2017-12-13.
 */

public abstract class Presenter {
    private IBaseView mView;
    protected Context mContext;

    public Presenter(IBaseView view, Context context) {
        mView = view;
        mContext = context;
    }

    public abstract void onCreate();

    public void onStart() {
    }//暂时没用到

    public void onStop() {
    }

    public void onPause() {
    }//暂时没用到

    public void onDestroy() {
        mContext = null;
        mView = null;
    }
}
