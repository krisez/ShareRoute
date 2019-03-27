package cn.krisez.imchat.manager;

import java.nio.ByteBuffer;

import cn.krisez.imchat.receiver.MessageReceiver;
import cn.krisez.imchat.client.ImClient;

public class MessageManager {

    private MessageReceiver mReceiver;

    public static void send(String text) {
        instance().send(text);
    }

    public static void send(byte[] data) {
        instance().send(data);
    }

    public static void send(ByteBuffer buffer) {
        instance().send(buffer);
    }

    public static void setReceiver(MessageReceiver receiver) {
        instance().setMsgReceiver(receiver);
    }

    //外部获取IMClient
    private boolean isFirst = true;

    public static ImClient instance(){
        return ImClient.getInstance("");
    }
}
