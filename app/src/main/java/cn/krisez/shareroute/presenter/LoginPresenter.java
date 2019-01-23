package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.content.Intent;

import cn.krisez.kotlin.activity.ILoginView;
import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.base.IBaseView;

/**
 * @describe 用于登录和注册操作的分离
 */
public class LoginPresenter extends Presenter{

    private ILoginView mView;

    public LoginPresenter(IBaseView view, Context context) {
        super(view, context);
        mView = (ILoginView) view;
    }

    @Override
    public void onCreate() {

    }

    @Override
    void onStart() {

    }

    @Override
    void onStop() {

    }

    @Override
    void onPause() {

    }

    @Override
    void attachIncomingIntent(Intent intent) {

    }

    public void login(String phone,String password){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).login(phone,password)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {

            }

            @Override
            public void onFailed(String s) {

            }
        });
    }
}
