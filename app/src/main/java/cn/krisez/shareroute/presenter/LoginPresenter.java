package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.os.Bundle;

import cn.krisez.framework.base.HandleType;
import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.kotlin.ui.activity.MainActivity;
import cn.krisez.kotlin.ui.views.ILoginView;
import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.bean.User;
import cn.krisez.shareroute.utils.MD5Utils;
import cn.krisez.shareroute.utils.SPUtil;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

/**
 * @describe 用于登录和注册操作的分离
 */
public class LoginPresenter extends Presenter {

    private ILoginView mView;

    public LoginPresenter(IBaseView view, Context context) {
        super(view, context);
        mView = (ILoginView) view;
    }

    @Override
    public void onCreate() {

    }

    public void login(@NotNull String phone, @NotNull String password) {
        String encodePw = MD5Utils.encode(password);
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).login(phone,encodePw)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                User user = new Gson().fromJson(result.extra, User.class);
                SPUtil.saveUser(user);
                SPUtil.saveUserPassword(encodePw);
                mView.loginSuccess();
            }

            @Override
            public void onFailed(String s) {
                mView.showTips(s);
            }
        });
    }

    public void register(@NotNull String phone) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).register(phone))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mView.registerSuccessful();
                        User user = new Gson().fromJson(result.extra,User.class);
                        SPUtil.saveUser(user);
                    }

                    @Override
                    public void onFailed(String s) {
                        mView.error(s);
                    }
                });
    }

    public void sendSMDCode(String mobile){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).sendSMS(mobile))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mView.code(result.extra);
                    }

                    @Override
                    public void onFailed(String s) {

                    }
                });
    }

    public void updatePw(String pw) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).updatePw(SPUtil.getUser().id,MD5Utils.encode(pw)))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        SPUtil.saveUserPassword(MD5Utils.encode(pw));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("class", MainActivity.class);
                        mView.handle(HandleType.INTENT, bundle);
                    }

                    @Override
                    public void onFailed(String s) {
                        mView.error(s);
                    }
                });
    }
}
