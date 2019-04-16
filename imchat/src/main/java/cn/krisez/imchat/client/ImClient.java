package cn.krisez.imchat.client;

import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.krisez.imchat.receiver.MessageReceiver;

public class ImClient extends WebSocketClient {

    private static ImClient sImClient = null;
    private static boolean isConnect = false;
    private Map<Integer, MessageReceiver> map = new HashMap<>();

    private ImClient(URI serverUri) {
        super(serverUri);
    }

    public static ImClient getInstance() {
        if (sImClient == null) {
            synchronized (ImClient.class) {
                if (sImClient == null) {
                    try {
                        Log.d("ImClient", "getInstance:" + ImConst.id);
                        String sUrl = "ws://krisez.cn:4632/" + ImConst.id;
                        sImClient = new ImClient(new URI(sUrl));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!isConnect) {
            sImClient.connect();
            isConnect = true;
        }
        return sImClient;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("ImClient", "onOpen:连接成功" + handshakedata.getHttpStatusMessage());
    }

    /**
     * type=0 消息
     * type=66 好友添加
     * @param message 服务端返回的message
     */
    @Override
    public void onMessage(String message) {
        Log.d("ImClient", "onMessage:" + message);
        WebSocketTransfer bean = new Gson().fromJson(message, WebSocketTransfer.class);
        if (bean.type == 0) {
            Objects.requireNonNull(map.get(0)).receiver(bean.json);
        } else {
            Objects.requireNonNull(map.get(bean.type)).receiver(bean.json);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        sImClient = null;
        isConnect = false;
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     *
     * @param tag 0 消息；-1服务；66好友服务监听
     * @param msgReceiver
     */
    public void addMsgReceiver(int tag, MessageReceiver msgReceiver) {
        map.put(tag, msgReceiver);
    }

    public void removeMsgReceiver(int tag) {
        map.remove(tag);
    }
}
