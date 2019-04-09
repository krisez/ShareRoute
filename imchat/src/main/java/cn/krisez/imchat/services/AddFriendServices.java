package cn.krisez.imchat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.AddFriendBean;
import cn.krisez.imchat.manager.MessageManager;

public class AddFriendServices extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AddFriendServices", "onStartCommand:" + "??");
        MessageManager.addReceiver(66, json -> {
            AddFriendBean addFriend = new Gson().fromJson(json, AddFriendBean.class);
            Intent i = new Intent(this, (Class<?>) intent.getSerializableExtra("cls"));
            PendingIntent pi = PendingIntent.getActivities(this,66,new Intent[]{i},0);
            Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_icon).setContentTitle("好友请求").setContentText(addFriend.ida + "请求添加好友").setTicker("有消息").setContentIntent(pi);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, builder.build());
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MessageManager.removeReceiver(66);
        super.onDestroy();
    }
}
