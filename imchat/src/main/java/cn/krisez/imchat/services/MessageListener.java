package cn.krisez.imchat.services;

import cn.krisez.imchat.bean.MessageBean;

public interface MessageListener {
    void message(MessageBean msg);
}
