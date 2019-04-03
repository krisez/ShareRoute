package cn.krisez.imchat.ui.fragment;

import android.os.Bundle;
import android.view.View;

import cn.krisez.framework.base.BaseFragment;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.R;

public class FriendFragment extends BaseFragment {
    @Override
    protected View newView() {
        return View.inflate(getContext(), R.layout.fragment_friend,null);
    }

    @Override
    protected Presenter presenter() {
        return null;
    }

    @Override
    protected void init(View v, Bundle bundle) {
        setRefreshEnable(false);
    }

    @Override
    public void onRefresh() {

    }
}
