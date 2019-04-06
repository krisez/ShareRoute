package cn.krisez.imchat.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;

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

public class AddFriendFragment extends BaseFragment implements IIMView {
    private IMPresenter mPresenter;
    private FriendAdapter mAdapter;
    private List<UserBean> mList = new ArrayList<>();

    @Override
    protected View newView() {
        return View.inflate(getContext(), R.layout.fragment_add_friend, null);
    }

    @Override
    protected Presenter presenter() {
        return mPresenter = new IMPresenter(this, getContext());
    }

    @Override
    protected void init(View v, Bundle bundle) {
        mAdapter = new FriendAdapter(getContext(), R.layout.item_add_friend, mList);
        RecyclerView recyclerView = v.findViewById(R.id.add_result_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        initListener(v);
    }

    private void initListener(View v) {
        EditText editText = v.findViewById(R.id.add_friend_et);
        String selfId = SharePreferenceUtils.obj(getContext()).getUserId();
        String selfMobile = SharePreferenceUtils.obj(getContext()).getUserMobile();
        v.findViewById(R.id.add_friend_btn).setOnClickListener(vv -> {
            String search = editText.getText().toString();
            if (!"".equals(search) && !selfId.equals(search) && !selfMobile.equals(search)) {
                mPresenter.friends(search, 1);
            } else {
                error("输入有误，请重新输入！");
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mList.get(position).request = true;
            mPresenter.requestAdd(mList.get(position).id, selfId);
            adapter.refreshNotifyItemChanged(position);
        });
    }

    @Override
    public void showTips(String tips) {
        toast(tips);
    }

    @Override
    public void getFriendsList(List<UserBean> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void chatList(Map<String, List<MessageBean>> map) {

    }
}
