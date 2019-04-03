package cn.krisez.imchat.ui;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;

import java.util.List;
import java.util.Map;

public interface IConversationView extends IBaseView {
    void showTips(String tips);
    void chatList(Map<String, List<MessageBean>> map);
}
