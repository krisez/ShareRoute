package cn.krisez.imchat.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.krisez.framework.base.BaseFragment;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.FriendAdapter;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.presneter.IMPresenter;
import cn.krisez.imchat.ui.IIMView;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class FriendFragment extends BaseFragment implements IIMView {
    private IMPresenter mPresenter;
    private FriendAdapter mAdapter;

    @Override
    protected View newView() {
        return View.inflate(getContext(), R.layout.fragment_friend, null);
    }

    @Override
    protected Presenter presenter() {
        return mPresenter = new IMPresenter(this, getContext());
    }

    @Override
    protected void init(View v, Bundle bundle) {
        setRefreshEnable(true);
        mAdapter = new FriendAdapter(getContext(),R.layout.item_friend,new ArrayList<>());
        RecyclerView recyclerView = v.findViewById(R.id.friends_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mPresenter.friends(SharePreferenceUtils.obj(getContext()).getUserId(),0);
    }

    @Override
    public void onRefresh() {
        mPresenter.friends(SharePreferenceUtils.obj(getContext()).getUserId(),0);
    }

    @Override
    public void showTips(String tips) {
        toast(tips);
        disableRefresh();
    }

    @Override
    public void getFriendsList(List<UserBean> list) {
        mAdapter.setNewData(list);
        disableRefresh();
    }

    @Override
    public void chatList(Map<String, List<MessageBean>> map) {

    }
}
