package cn.krisez.imchat.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.*;

import cn.krisez.framework.base.BaseFragment;
import cn.krisez.framework.base.Presenter;
import cn.krisez.framework.widget.DividerDecoration;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.ConversationAdapter;
import cn.krisez.imchat.bean.ConversationBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.presneter.IMPresenter;
import cn.krisez.imchat.ui.IIMView;
import cn.krisez.imchat.ui.activity.ChatActivity;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class ConversationFragment extends BaseFragment implements IIMView {
    private IMPresenter mPresenter;
    private ConversationAdapter mAdapter;
    private List<ConversationBean> mList = new ArrayList<>();
    private Map<String, List<MessageBean>> mMap = new HashMap<>();

    @Override
    protected View newView() {
        return View.inflate(getContext(), R.layout.fragment_conversation, null);
    }

    @Override
    protected Presenter presenter() {
        return mPresenter = new IMPresenter(this, getContext());
    }


    @Override
    protected void init(View view, Bundle bundle) {
        setRefreshEnable(true);
        mAdapter = new ConversationAdapter(getContext(), R.layout.item_conversation, mList);
        RecyclerView recyclerView = view.findViewById(R.id.conversation_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerDecoration(getContext(), DividerDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_conversation_empty, (ViewGroup) recyclerView.getParent());

        mAdapter.setOnItemClickListener((a, v, p) -> {
            ConversationBean conversationBean = mList.get(p);
            String key = conversationBean.fromId.equals(SharePreferenceUtils.obj(getContext()).getUserId()) ? conversationBean.toId : conversationBean.fromId;
            String data = new Gson().toJson(mMap.get(key));
            if (!"0".equals(conversationBean.no)) {
                mPresenter.updateAllRead(conversationBean.fromId, conversationBean.toId);
            }
            conversationBean.no = "0";
            a.refreshNotifyItemChanged(p);
            ChatActivity.chatStart(getContext(), data, key, conversationBean.name);
        });
    }

    @Override
    public void showTips(String tips) {
        toast(tips);
        disableRefresh();
    }

    @Override
    public void chatList(Map<String, List<MessageBean>> map) {
        mList.clear();
        this.mMap = map;
        for (Map.Entry<String, List<MessageBean>> entry : map.entrySet()) {
            int count = 0;
            ConversationBean conversation = new ConversationBean();
            for (MessageBean bean : entry.getValue()) {
                if (bean.to.equals(SharePreferenceUtils.obj(getContext()).getUserId())) {
                    if (null == bean.isRead || bean.isRead.equals("0")) {
                        count++;
                    }
                    conversation.headUrl = bean.headUrl;
                    conversation.fromId = bean.from;
                    conversation.toId = bean.to;
                    conversation.name = bean.name;
                }
            }
            MessageBean msg = entry.getValue().get(entry.getValue().size() - 1);
            conversation.lastContent = msg.content;
            conversation.time = msg.time;
            conversation.no = String.valueOf(count);
            mList.add(conversation);
        }
        mAdapter.setNewData(mList);
        disableRefresh();
    }

    @Override
    public void getFriendsList(List<UserBean> list,int type) {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getChatList(SharePreferenceUtils.obj(getActivity()).getUserId());
    }

    @Override
    public void onResume() {
        onRefresh();
        super.onResume();
    }
}
