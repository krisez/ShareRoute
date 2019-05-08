package cn.krisez.imchat.manager;

import org.java_websocket.WebSocket;

import java.nio.ByteBuffer;

import cn.krisez.imchat.receiver.MessageReceiver;
import cn.krisez.imchat.client.ImClient;

public class MessageManager {

    public static void send(String text) {
        if(instance().getReadyState() != WebSocket.READYSTATE.NOT_YET_CONNECTED){
            instance().send(text);
        }
    }

    public static void addReceiver(int tag,MessageReceiver receiver) {
        instance().addMsgReceiver(tag,receiver);
    }

    public static ImClient instance(){
        return ImClient.getInstance();
    }

    public static void removeReceiver(int tag) {
        instance().removeMsgReceiver(tag);
    }
}
