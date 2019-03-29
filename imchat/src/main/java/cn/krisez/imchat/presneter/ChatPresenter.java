package cn.krisez.imchat.presneter;

import android.content.Context;
import android.util.Log;
import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.ui.IChatView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ChatPresenter extends Presenter {

    private IChatView mChatView;

    public ChatPresenter(IBaseView view, Context context) {
        super(view, context);
        mChatView = (IChatView) view;
    }

    @Override
    public void onCreate() {

    }

    public void getChatList(String userId, String time,String msgId) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).chatList(userId, time,msgId))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        Log.d("ChatPresenter", "onSuccess:" + result.extra);
                        List<MessageBean> list = new Gson().fromJson(result.extra, new TypeToken<List<MessageBean>>(){}.getType());
                        //todo 分门别类
                        List<ConversationBean> chatBeans = new ArrayList<>();
                        mChatView.chatList(chatBeans);
                    }

                    @Override
                    public void onFailed(String s) {
                        mChatView.showTips(s);
                    }
                });
    }
}
