package cn.krisez.imchat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.AddFriendBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.db.IMMsgRxDbManager;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.ui.activity.IMActivity;
import io.reactivex.disposables.Disposable;

public class IMMsgService extends Service {

    private NotificationManager manager;
    private MsgBinder myBinder;
    private String fromId;


    public class MsgBinder extends Binder {

        private MessageListener listener;

        public void setOnMessageListener(MessageListener listener){
            this.listener = listener;
        }

    }

    @Override
    public void onCreate() {
        myBinder = new MsgBinder();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("IMMsgService", "onStartCommand:" + "服务启动成功");
        MessageManager.addReceiver(66, json -> {
            AddFriendBean addFriend = new Gson().fromJson(json, AddFriendBean.class);
            Intent i = new Intent(this, (Class<?>) intent.getSerializableExtra("cls"));
            PendingIntent pi = PendingIntent.getActivities(this,66,new Intent[]{i},0);
            Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_icon).setContentTitle("好友请求").setContentText(addFriend.ida + "请求添加好友").setTicker("有消息").setContentIntent(pi);
            manager.notify(66, builder.build());
        });
        MessageManager.addReceiver(0, json -> {
            MessageBean bean = new Gson().fromJson(json, MessageBean.class);
            Disposable d = IMMsgRxDbManager.getInstance(this).insertMsg(bean).subscribe(b -> {
                if (!bean.from.equals(fromId)) {
                    Intent i = new Intent(this, IMActivity.class);
                    i.putExtra("user", intent.getStringExtra("user"));
                    PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[]{i}, 0);
                    Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_icon).setContentTitle("消息通知").setContentText(bean.name + ":" + bean.content).setTicker("有消息").setContentIntent(pi);
                    manager.notify(0, builder.build());
                }else{
                    myBinder.listener.message(bean);
                }
            });
        });
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        fromId = intent.getStringExtra("from");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        myBinder.listener = null;
        fromId = "";
        Log.d("IMMsgService", "onUnbind:" + "??");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("IMMsgService", "onDestroy:" + "???");
        MessageManager.removeReceiver(0);
        MessageManager.removeReceiver(66);
        super.onDestroy();
    }
}
