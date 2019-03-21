package cn.krisez.imchat.presneter;

import android.content.Context;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.net.Api;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;

public class ChatPresenter extends Presenter {

    public ChatPresenter(IBaseView view, Context context) {
        super(view, context);
    }

    @Override
    public void onCreate() {

    }

    public void getChatList(){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).chatList("")).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {

            }

            @Override
            public void onFailed(String s) {

            }
        });
    }
}
