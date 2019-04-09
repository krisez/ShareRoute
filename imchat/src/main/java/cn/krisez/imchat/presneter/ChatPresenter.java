package cn.krisez.imchat.presneter;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.client.WebSocketTransfer;
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
        MessageManager.addReceiver(0,s -> {
            WebSocketTransfer bean = new Gson().fromJson(s, WebSocketTransfer.class);
            if (bean.type == 0) {
                MessageBean msg = new Gson().fromJson(bean.json, MessageBean.class);
                mView.insert(msg);
                updateRead(msg.from, msg.to);
            }
        });
    }

    @Override
    public void onDestroy() {
        MessageManager.removeReceiver(0);
        super.onDestroy();
    }

    private void updateRead(String from, String to) {
        Looper.prepare();
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).updateAllRead(from, to)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                Log.i("IMPresenter", "onSuccess: " + result.extra);
            }

            @Override
            public void onFailed(String s) {
                Log.e("IMPresenter", "onFailed: " + s);
            }
        });
        Looper.loop();
    }
}
