package cn.krisez.imchat.presneter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.ui.IConversationView;
import cn.krisez.imchat.utils.MsgParseUtils;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;

public class ConversationPresenter extends Presenter {

    private IConversationView mChatView;

    public ConversationPresenter(IBaseView view, Context context) {
        super(view, context);
        mChatView = (IConversationView) view;
    }

    @Override
    public void onCreate() {

    }

    public void getChatList(String userId, String time, String msgId) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).chatList(userId, time, msgId))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        List<MessageBean> list = new Gson().fromJson(result.extra, new TypeToken<List<MessageBean>>() {
                        }.getType());
                        Map<String, List<MessageBean>> map = MsgParseUtils.parse(list, mContext);
                        mChatView.chatList(map);
                    }

                    @Override
                    public void onFailed(String s) {
                        mChatView.showTips(s);
                    }
                });
    }

    public void updateAllRead(String from, String to) {
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
