package cn.krisez.imchat.presneter;

import android.content.Context;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.ui.IConversationView;
import cn.krisez.imchat.utils.MsgParseUtils;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                        List<ConversationBean> chatBeans = new ArrayList<>();
                        for (Map.Entry<String,List<MessageBean>> entry:map.entrySet()) {
                            MessageBean msg = entry.getValue().get(entry.getValue().size()-1);
                            ConversationBean conversation = new ConversationBean(msg.from,msg.to
                                    ,msg.headUrl
                                    ,msg.name,msg.content,msg.time,entry.getValue().size()+"");
                            chatBeans.add(conversation);
                        }
                        mChatView.chatList(chatBeans);
                    }

                    @Override
                    public void onFailed(String s) {
                        mChatView.showTips(s);
                    }
                });
    }
}
