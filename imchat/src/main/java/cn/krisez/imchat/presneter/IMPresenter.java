package cn.krisez.imchat.presneter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.bean.AddFriendBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.client.WebSocketTransfer;
import cn.krisez.imchat.db.IMMsgRxDbManager;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.net.Api;
import cn.krisez.imchat.ui.IIMView;
import cn.krisez.imchat.utils.MsgParseUtils;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class IMPresenter extends Presenter {

    private IIMView mIIMView;

    public IMPresenter(IBaseView view, Context context) {
        super(view, context);
        mIIMView = (IIMView) view;
    }

    @Override
    public void onCreate() {
    }

    public void getChatList(String userId) {
        Disposable d = IMMsgRxDbManager.getInstance(mContext).queryMsg().subscribe(queryMsgBean ->
                NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).chatList(userId, queryMsgBean.time, queryMsgBean.index + ""))
                        .handler(new ResultHandler() {
                            @Override
                            public void onSuccess(Result result) {
                                List<MessageBean> list = new Gson().fromJson(result.extra, new TypeToken<List<MessageBean>>() {
                                }.getType());
                                if(!list.isEmpty()){
                                    mIIMView.chatList(MsgParseUtils.mergeMap(queryMsgBean.map, list, mContext));
                                    IMMsgRxDbManager.getInstance(mContext).insertMsg(list);
                                }else{
                                    mIIMView.chatList(queryMsgBean.map);
                                }
                            }

                            @Override
                            public void onFailed(String s) {
                                mIIMView.chatList(queryMsgBean.map);
                                mIIMView.showTips(s);
                            }
                        }));

    }

    public void updateAllRead(String from, String to) {
        Disposable disposable = IMMsgRxDbManager.getInstance(mContext).updateMsg(from, to).subscribe(query ->
                NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).updateAllRead(from, to)).handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        Log.i("IMPresenter", "onSuccess: " + result.extra);
                    }

                    @Override
                    public void onFailed(String s) {
                        Log.e("IMPresenter", "onFailed: " + s);
                    }
                }));
    }

    /**
     * @param id
     * @param type 0,查找自己的好友；1，查找用户进行好友添加；2，查询好友请求
     */
    public void friends(String id, int type) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).friends(id, type)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                Log.d("IMPresenter", "onSuccess:" + result.extra);
                mIIMView.getFriendsList(new Gson().fromJson(result.extra, new TypeToken<List<UserBean>>() {
                }.getType()),type);
            }

            @Override
            public void onFailed(String s) {
                mIIMView.showTips(s);
            }
        });
    }

    /**
     * @param ida 申请人（请求添加好友的人）
     * @param idb 处理人（用户）
     * @param deal 1,同意；-3不同意
     */
    public void dealRequest(String ida,String idb,int deal){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(Api.class).dealFriendRequest(ida,idb,deal)).handler(new ResultHandler() {
            @Override
            public void onSuccess(Result result) {
                Log.d("IMPresenter", "onSuccess:" + result.extra);
                mIIMView.showTips(result.extra);
            }

            @Override
            public void onFailed(String s) {
                mIIMView.showTips(s);
            }
        });
    }

    public void requestAdd(String friendId, String selfId) {
        MessageManager.send(new WebSocketTransfer(66, new AddFriendBean(selfId, friendId).toString()).toString());
    }


}
