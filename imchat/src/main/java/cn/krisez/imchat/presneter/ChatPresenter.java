package cn.krisez.imchat.presneter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.ui.IChatView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;

public class ChatPresenter extends Presenter {
    private IChatView mView;

    public ChatPresenter(IBaseView view, Context context) {
        super(view, context);
        mView = (IChatView) view;
    }

    @Override
    public void onCreate() {
        MessageManager.setReceiver(msg -> {
            MessageBean bean = new Gson().fromJson(msg, MessageBean.class);
            mView.insert(bean);
            updateRead(bean.from, bean.to);
        });
    }

    private void updateRead(String from, String to) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).updateAllRead(from, to)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                Log.i("ConversationPresenter", "onSuccess: " + result.extra);
            }

            @Override
            public void onFailed(String s) {
                Log.e("ConversationPresenter", "onFailed: " + s);
            }
        });
    }
}
