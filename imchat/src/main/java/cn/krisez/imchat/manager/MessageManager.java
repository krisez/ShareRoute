package cn.krisez.imchat.manager;

import java.nio.ByteBuffer;

import cn.krisez.imchat.MessageReceiver;
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

    public void setReceiver(MessageReceiver receiver) {
        mReceiver = receiver;
    }

    //外部获取IMClient
    private boolean isFirst = true;
    public static void connect(String id) {
        ImClient.getInstance(id).connect();
    }

    public static ImClient instance(){
        return ImClient.getInstance("");
    }
}
