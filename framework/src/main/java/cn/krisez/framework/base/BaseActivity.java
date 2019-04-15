package cn.krisez.framework.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.krisez.framework.R;


public abstract class BaseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IBaseView {

    public SwipeRefreshLayout mSwipeRefreshLayout;
    private Presenter mPresenter;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mSwipeRefreshLayout = findViewById(R.id.base_layout);
        View view = newView();
        assert view != null;
        mSwipeRefreshLayout.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red,R.color.black,R.color.blue);

        mPresenter = presenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        init(savedInstanceState);
    }

    protected void showBackIconAndClick(){
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    protected void setTitle(String title){
        mToolbar.setTitle(title);
    }

    protected void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    protected void setUpToolbar(){
        mAppBarLayout = findViewById(R.id.toolbar_layout);
        mAppBarLayout.setVisibility(View.VISIBLE);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(mToolbar);
    }

    protected void hideToolbar(){
        if(mAppBarLayout == null){
            return;
        }
        mAppBarLayout.setVisibility(View.VISIBLE);
    }

    protected abstract View newView();

    protected abstract Presenter presenter();

    protected abstract void init(Bundle bundle);

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String s) {
        toast(s);
    }

    @Override
    public void handle(HandleType type,Bundle bundle) {
        switch (type){
            case INTENT:
                startActivity(new Intent(this, (Class<?>) bundle.getSerializable("class")));
                finish();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public void disableRefresh(){
        handle(HandleType.REFRESH,null);

    }
}
