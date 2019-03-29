package cn.krisez.imchat.ui;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.imchat.bean.ConversationBean;

import java.util.List;

public interface IChatView extends IBaseView {
    void showTips(String tips);
    void chatList(List<ConversationBean> list);
}
