package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.content.Intent;

import cn.krisez.kotlin.activity.ILoginView;
import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.base.IBaseView;
import cn.krisez.shareroute.bean.User;
import cn.krisez.shareroute.utils.SPUtil;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

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

    public void login(@NotNull String phone,@NotNull String password){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).login(phone,password)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                User user = new Gson().fromJson(result.extra, User.class);
                SPUtil.saveUserId(user.id);
                mView.showTips(user.realName);
            }

            @Override
            public void onFailed(String s) {
                mView.error(s);
            }
        });
    }

    public void register(@NotNull String phone) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).register(phone))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mView.registerSuccessful();
                    }

                    @Override
                    public void onFailed(String s) {
                        mView.error(s);
                    }
                });
    }
}
