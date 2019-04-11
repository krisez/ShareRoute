package cn.krisez.framework.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.krisez.framework.R;

public abstract class BaseFragment extends Fragment implements IBaseView, SwipeRefreshLayout.OnRefreshListener {

    public SwipeRefreshLayout mSwipeRefreshLayout;
    private Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout = v.findViewById(R.id.base_layout);
        View view = newView();
        assert view != null;
        mSwipeRefreshLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.black, R.color.blue);

        mPresenter = presenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        init(v,savedInstanceState);
    }

    protected void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void toast(String s) {
        Toast.makeText(getActivity(), s+"", Toast.LENGTH_SHORT).show();
    }

    protected abstract View newView();

    protected abstract Presenter presenter();

    protected abstract void init(View v,Bundle bundle);

    @Override
    public void handle(HandleType type, Bundle bundle) {
        switch (type) {
            case INTENT:
                startActivity(new Intent(getActivity(), (Class<?>) bundle.getSerializable("class")));
                break;
            case REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case NOTIFICATION:
                break;
            case OTHER:
                break;
        }
    }

    @Override
    public void error(String e) {
        toast(e);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public void disableRefresh(){
        handle(HandleType.REFRESH,null);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
