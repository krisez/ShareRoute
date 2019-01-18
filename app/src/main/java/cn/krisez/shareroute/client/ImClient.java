package cn.krisez.shareroute.client;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import cn.krisez.shareroute.event.MessageEvent;

public class ImClient extends WebSocketClient {

    private static ImClient sImClient = null;

    private ImClient(URI serverUri) {
        super(serverUri);
    }

    public static ImClient getInstance(String uri){
            if(sImClient==null){
                synchronized (ImClient.class){
                    if (sImClient == null) {
                        try {
                            sImClient = new ImClient(new URI(uri));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
        return sImClient;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("ImClient", "onOpen:连接成功" + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        EventBus.getDefault().post(new MessageEvent(message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
