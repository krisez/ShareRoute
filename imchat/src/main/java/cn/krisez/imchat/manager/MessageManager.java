package cn.krisez.imchat.manager;

import java.nio.ByteBuffer;

import cn.krisez.imchat.receiver.MessageReceiver;
import cn.krisez.imchat.client.ImClient;

public class MessageManager {

    public static void send(String text) {
        instance().send(text);
    }

    public static void send(byte[] data) {
        instance().send(data);
    }

    public static void send(ByteBuffer buffer) {
        instance().send(buffer);
    }

    public static void addReceiver(int tag,MessageReceiver receiver) {
        instance().addMsgReceiver(tag,receiver);
    }

    private static ImClient instance(){
        return ImClient.getInstance();
    }

    public static void removeReceiver(int tag) {
        instance().removeMsgReceiver(tag);
    }
}
