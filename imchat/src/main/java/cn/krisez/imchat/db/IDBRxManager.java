package cn.krisez.imchat.db;

import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDBRxManager {
    Observable<Boolean> insertMsg(MessageBean msg);
    Map<String, List<MessageBean>> queryMsg();
    void deleteMsg(ArrayList<Integer> _id);
}
