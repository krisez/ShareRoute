package cn.krisez.imchat.ui;

import java.util.List;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.imchat.bean.ChatBean;

public interface IChatView extends IBaseView {
    void showTips(String tips);
    void  chatList(List<ChatBean> list);
}
