package cn.krisez.imchat.presneter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.AddFriendBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.client.WebSocketTransfer;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.receiver.MessageReceiver;
import cn.krisez.imchat.ui.IIMView;
import cn.krisez.imchat.utils.MsgParseUtils;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;

public class IMPresenter extends Presenter {

    private IIMView mIIMView;

    public IMPresenter(IBaseView view, Context context) {
        super(view, context);
        mIIMView = (IIMView) view;
    }

    @Override
    public void onCreate() {
        MessageManager.setReceiver(s -> {
            WebSocketTransfer bean = new Gson().fromJson(s, WebSocketTransfer.class);
            if (bean.type == 66) {
                AddFriendBean addFriend = new Gson().fromJson(bean.json, AddFriendBean.class);
                Log.d("IMPresenter", "receiver:" + addFriend.ida + "请求添加好友");
            }
        });
    }

    public void getChatList(String userId, String time, String msgId) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).chatList(userId, time, msgId))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        List<MessageBean> list = new Gson().fromJson(result.extra, new TypeToken<List<MessageBean>>() {
                        }.getType());
                        Map<String, List<MessageBean>> map = MsgParseUtils.parse(list, mContext);
                        mIIMView.chatList(map);
                    }

                    @Override
                    public void onFailed(String s) {
                        mIIMView.showTips(s);
                    }
                });
    }

    public void updateAllRead(String from, String to) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).updateAllRead(from, to)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                Log.i("IMPresenter", "onSuccess: " + result.extra);
            }

            @Override
            public void onFailed(String s) {
                Log.e("IMPresenter", "onFailed: " + s);
            }
        });
    }

    /**
     *
     * @param id
     * @param type 0,查找自己的好友；1，查找用户进行好友添加
     */
    public void friends(String id,int type){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).friends(id,type)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                mIIMView.getFriendsList(new Gson().fromJson(result.extra,new TypeToken<List<UserBean>>(){}.getType()));
            }

            @Override
            public void onFailed(String s) {
                mIIMView.showTips(s);
            }
        });
    }

    public void requestAdd(String friendId,String selfId){
        MessageManager.send(new WebSocketTransfer(66,new AddFriendBean(selfId,friendId).toString()).toString());
    }


}
