package cn.krisez.imchat.ui;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.imchat.bean.MessageBean;

public interface IChatView extends IBaseView {
    void insert(MessageBean messageBean);
}