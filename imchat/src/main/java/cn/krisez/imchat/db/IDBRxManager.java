package cn.krisez.imchat.db;

import cn.krisez.imchat.bean.MessageBean;
import io.reactivex.Observable;

import java.util.ArrayList;

public interface IDBRxManager {
    Observable<Boolean> insertMsg(MessageBean msg);
    void queryMsg();
    void deleteMsg(ArrayList<Integer> _id);
}
