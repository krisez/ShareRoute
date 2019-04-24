package cn.krisez.imchat.presneter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.db.IMMsgRxDbManager;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.services.IMMsgService;
import cn.krisez.imchat.services.MessageListener;
import cn.krisez.imchat.ui.IChatView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import io.reactivex.disposables.Disposable;

public class ChatPresenter extends Presenter {
    private IChatView mView;
    private ServiceConnection connection;

    public ChatPresenter(IBaseView view, Context context) {
        super(view, context);
        mView = (IChatView) view;
    }

    @Override
    public void onCreate() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IMMsgService.MsgBinder mBinder = (IMMsgService.MsgBinder) service;
                mBinder.setOnMessageListener(msg -> {
                    mView.insert(msg);
                    updateRead(msg.from, msg.to);
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    @Override
    public void onDestroy() {
        mContext.unbindService(connection);
        super.onDestroy();
    }

    public void bindServices(String id){
        mContext.bindService(new Intent(mContext, IMMsgService.class).putExtra("from",id),connection,0);
    }

    private void updateRead(String from, String to) {
        Disposable disposable = IMMsgRxDbManager.getInstance(mContext).updateMsg(from, to).subscribe(query ->
                NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).updateAllRead(from, to)).handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        Log.i("ChatPresenter", "onSuccess: " + result.extra);
                    }

                    @Override
                    public void onFailed(String s) {
                        Log.e("ChatPresenter", "onFailed: " + s);
                    }
                }));
    }
}
