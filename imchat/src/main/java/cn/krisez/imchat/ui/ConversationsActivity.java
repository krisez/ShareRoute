package cn.krisez.imchat.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.krisez.framework.base.BaseActivity;
import cn.krisez.framework.base.Presenter;
import cn.krisez.framework.utils.DensityUtil;
import cn.krisez.framework.widget.DividerDecoration;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.ConversationAdapter;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.presneter.ConversationPresenter;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class ConversationsActivity extends BaseActivity implements IConversationView {
    private ConversationPresenter mPresenter;
    private ConversationAdapter mAdapter;
    @Override
    protected View newView() {
        return View.inflate(this, R.layout.activity_conversation,null);
    }

    @Override
    protected Presenter presenter() {
        return mPresenter = new ConversationPresenter(this,this);
    }

    @Override
    protected void init(Bundle bundle) {
        setUpToolbar();
        showBackIconAndClick();
        setRefreshEnable(true);
        mAdapter = new ConversationAdapter(this, R.layout.item_conversation,new ArrayList<>());
        RecyclerView recyclerView = findViewById(R.id.chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerDecoration(this, DividerDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((a,v,p)-> {
                MessageBean message = new MessageBean();
                message.from = SharePreferenceUtils.obj(ConversationsActivity.this).getUserId();
                message.to = "1948881";
                message.type = "0";
                message.content = "eihhhh";

                message.time = DensityUtil.getTime();
                MessageManager.send(message.toString());
        });
        MessageManager.setReceiver(msg ->{
            //todo 完善处理接发消息,这里不是主线程
//            toast(msg);
            Log.d("ChatActivity", "receiver: " + msg);
        } );
    }

    @Override
    public void onRefresh() {
        mPresenter.getChatList(SharePreferenceUtils.obj(ConversationsActivity.this).getUserId(),"2019-01-01 00:00:00","0");
    }

    @Override
    public void showTips(String tips) {
        toast(tips);
        disableRefresh();
    }

    @Override
    public void chatList(List<ConversationBean> list) {
        mAdapter.setNewData(list);
        disableRefresh();
    }
}
