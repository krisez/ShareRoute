package cn.krisez.imchat.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.krisez.framework.base.BaseFragment;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.AddFriendAdapter;
import cn.krisez.imchat.bean.AddFriendSection;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.presneter.IMPresenter;
import cn.krisez.imchat.ui.IIMView;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class AddFriendFragment extends BaseFragment implements IIMView {
    private IMPresenter mPresenter;
    private AddFriendAdapter mAdapter;
    private List<AddFriendSection> mList = new ArrayList<>();

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
        setRefreshEnable(true);
        mAdapter = new AddFriendAdapter(R.layout.item_section_set_title, mList);
        RecyclerView recyclerView = v.findViewById(R.id.add_result_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        initListener(v);
        onRefresh();
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
            int id = view.getId();
            if (id == R.id.request_add_friend_btn) {
                mList.get(position).t.request = true;
                mPresenter.requestAdd(mList.get(position).t.id, selfId);
                adapter.refreshNotifyItemChanged(position);
            }else if(id==R.id.friend_request_agree_btn){
                mList.get(position).t.request = true;
                mPresenter.dealRequest(mList.get(position).t.id,selfId,1);
                adapter.refreshNotifyItemChanged(position);
            }else if(id==R.id.friend_request_refuse_btn){
                mList.get(position).t.request = true;
                mPresenter.dealRequest(mList.get(position).t.id,selfId,-3);
                adapter.refreshNotifyItemChanged(position);
            }
        });
    }

    @Override
    public void showTips(String tips) {
        toast(tips);
    }

    private List<AddFriendSection> temp = new ArrayList<>();
    @Override
    public void getFriendsList(List<UserBean> list, int type) {
        mList.clear();
        if (type == 1) {
            mList.addAll(temp);
            mList.add(new AddFriendSection(true,"查询结果"));
            for (int i = 0; i < list.size(); i++) {
                mList.add(new AddFriendSection(list.get(i),1));
            }
        } else if (type == 2){
            temp.clear();
            mList.add(new AddFriendSection(true,"好友请求"));
            for (int i = 0; i < list.size(); i++) {
                mList.add(new AddFriendSection(list.get(i),2));
            }
            temp.addAll(mList);
        }
        mAdapter.notifyDataSetChanged();
        disableRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.friends(SharePreferenceUtils.obj(getContext()).getUserId(), 2);
    }

    @Override
    public void chatList(Map<String, List<MessageBean>> map) {

    }
}
