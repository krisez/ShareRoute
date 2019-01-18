package cn.krisez.shareroute.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Objects;

import cn.krisez.shareroute.R;
import cn.krisez.shareroute.presenter.Presenter;
import cn.krisez.shareroute.widget.RefreshView;


public abstract class BaseActivity extends AppCompatActivity implements RefreshView.RefreshListener, IBaseView {

    public LinearLayout mLinearLayout;
    private Presenter mPresenter;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mLinearLayout = findViewById(R.id.base_layout);
        View view = newView();
        assert view != null;
        mLinearLayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

       /* mRefresh.setRefreshEnabled(false);
        mRefresh.refreshing();
        mRefresh.setRefreshListener(this);
        mRefresh.setRefreshMaxHeight(mLinearLayout.dp(40));
        mRefresh.setRefreshViewHeight(mLinearLayout.dp(40));*/

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
        //mLinearLayout.setRefreshEnabled(enable);
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

    public int barBottomY() {
        return Objects.requireNonNull(getSupportActionBar()).getHeight() + getStatusBarHeight();

    }

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.d("asdasd", "get status bar height fail");
            e1.printStackTrace();
            return 75;
        }
    }

    @Override
    public void errorOP(String s) {
        toast(s);
        /*if (mLinearLayout.getRefreshState() == RefreshView.REFRESHING) {
            mLinearLayout.finishRefresh(false);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
