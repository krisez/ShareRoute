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

public class SocketService extends Service {

    private MyBinder myBinder;

    public class MyBinder extends Binder {
        private String fromId;

        public String getFromId() {
            return fromId;
        }

        public void setFromId(String fromId){
            fromId = fromId;
        }
    }

    @Override
    public void onCreate() {
        myBinder = new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SocketService", "onStartCommand:" + "服务启动成功");
        MessageManager.addReceiver(66, json -> {
            AddFriendBean addFriend = new Gson().fromJson(json, AddFriendBean.class);
            Intent i = new Intent(this, (Class<?>) intent.getSerializableExtra("cls"));
            PendingIntent pi = PendingIntent.getActivities(this,66,new Intent[]{i},0);
            Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_icon).setContentTitle("好友请求").setContentText(addFriend.ida + "请求添加好友").setTicker("有消息").setContentIntent(pi);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(66, builder.build());
        });
        MessageManager.addReceiver(0, json -> {
            MessageBean bean = new Gson().fromJson(json, MessageBean.class);
            Disposable d = IMMsgRxDbManager.getInstance(this).insertMsg(bean).subscribe(b -> {
                if (!bean.from.equals(myBinder.fromId)) {
                    Intent i = new Intent(this, IMActivity.class);
                    i.putExtra("user", intent.getStringExtra("user"));
                    PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[]{i}, 0);
                    Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_icon).setContentTitle("消息通知").setContentText(bean.name + ":" + bean.content).setTicker("有消息").setContentIntent(pi);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }
            });
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        MessageManager.removeReceiver(0);
        super.onDestroy();
    }
}
