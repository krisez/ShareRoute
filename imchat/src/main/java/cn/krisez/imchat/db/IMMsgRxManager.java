package cn.krisez.imchat.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.krisez.imchat.bean.MessageBean;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

public class IMMsgRxManager implements IDBRxManager {

    private static IMMsgRxManager sManager;
    private BriteDatabase db;
    private IMMsgRxManager(){
        db = DbUtils.getInstance();
    }

    public static synchronized IMMsgRxManager getInstance(){
        if(sManager==null){
            synchronized (IMMsgRxManager.class){
                if(sManager==null){
                    sManager = new IMMsgRxManager();
                }
            }
        }
        return sManager;
    }

    //服务器返回有新的消息
    //自己发送的消息
    @Override
    public Observable<Boolean> insertMsg(final MessageBean msg) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            ContentValues values = new ContentValues();
            values.put("_id", msg.index);
            values.put("fromId", msg.from);
            values.put("toId", msg.to);
            values.put("type", msg.type);
            values.put("content", msg.content);
            values.put("time", msg.time);
            values.put("fileUrl", msg.fileUrl);
            values.put("address", msg.address);
            values.put("read", msg.isRead);
            db.insert(DbConstant.MSG_TABLE, SQLiteDatabase.CONFLICT_REPLACE, values);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //查询语句
    @Override
    public void queryMsg() {
        Observable<SqlBrite.Query> users = db.createQuery(DbConstant.MSG_TABLE, "SELECT * FROM ?",DbConstant.MSG_TABLE);
        users.subscribe();
    }

    //删除消息记录，服务器不做操作
    @Override
    public void deleteMsg(ArrayList<Integer> _id) {

    }
}
