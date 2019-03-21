package cn.krisez.imchat.client;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import cn.krisez.imchat.MessageReceiver;

public class ImClient extends WebSocketClient {

    private static ImClient sImClient = null;

    private ImClient(URI serverUri) {
        super(serverUri);
    }

    public static ImClient getInstance(String id) {
        if (sImClient == null) {
            synchronized (ImClient.class) {
                if (sImClient == null) {
                    try {
                        String sUrl = "ws://192.168.137.1:932/";
                        sImClient = new ImClient(new URI(sUrl));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!sImClient.isOpen()){
            sImClient.connect();
        }
        return sImClient;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("ImClient", "onOpen:连接成功" + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        mReceiver.receiver(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        sImClient = null;
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    private MessageReceiver mReceiver;
    public void setMsgReceiver(MessageReceiver msgReceiver){
        this.mReceiver = msgReceiver;
    }
}
