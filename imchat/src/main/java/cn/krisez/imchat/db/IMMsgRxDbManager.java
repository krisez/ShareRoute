package cn.krisez.imchat.db;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.utils.MsgParseUtils;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IMMsgRxDbManager implements IDBRxManager {

    private BriteDatabase db;
    private Context mContext;

    private IMMsgRxDbManager(Context context) {
        db = DbUtils.getInstance();
        this.mContext = context;
    }

    public static IMMsgRxDbManager getInstance(Context context){
        return new IMMsgRxDbManager(context);
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
    public Map<String, List<MessageBean>> queryMsg() {
        Cursor cursor = db.query("select * from " + DbConstant.MSG_TABLE);
        List<MessageBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String from = cursor.getString(cursor.getColumnIndex("fromId"));
            String to = cursor.getString(cursor.getColumnIndex("toId"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String fileUrl = cursor.getString(cursor.getColumnIndex("fileUrl"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String read = cursor.getString(cursor.getColumnIndex("read"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String headUrl = cursor.getString(cursor.getColumnIndex("name"));
            MessageBean msg = new MessageBean(_id,from,to,type,content,time,fileUrl,address,read,name,headUrl);
            list.add(msg);
        }
        return MsgParseUtils.parse(list, mContext);
    }

    //删除消息记录，服务器不做操作
    @Override
    public void deleteMsg(ArrayList<Integer> _id) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try{
            for (int i = 0; i < _id.size(); i++) {
                db.delete(DbConstant.MSG_TABLE,"_id = ?",_id.get(i)+"");
            }
        }finally {
            transaction.markSuccessful();
        }
    }

}
