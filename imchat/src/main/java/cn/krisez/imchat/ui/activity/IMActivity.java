package cn.krisez.imchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.krisez.framework.base.BaseActivity;
import cn.krisez.framework.base.Presenter;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.FmPagerAdapter;
import cn.krisez.imchat.ui.fragment.AddFriendFragment;
import cn.krisez.imchat.ui.fragment.ConversationFragment;
import cn.krisez.imchat.ui.fragment.FriendFragment;

public class IMActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    @Override
    protected View newView() {
        return View.inflate(this, R.layout.activity_im,null);
    }

    @Override
    protected Presenter presenter() {
        return null;
    }

    @Override
    protected void init(Bundle bundle) {
        setUpToolbar();
        showBackIconAndClick();
        setRefreshEnable(false);
        List<Fragment> list = new ArrayList<>();
        list.add(new ConversationFragment());
        list.add(new FriendFragment());
        list.add(new AddFriendFragment());
        mViewPager = findViewById(R.id.im_viewpager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mBottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPager.setAdapter(new FmPagerAdapter(getSupportFragmentManager(),list));
        mViewPager.setOffscreenPageLimit(3);
        mBottomNavigationView = findViewById(R.id.im_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.im_bottom_conversations) {
            mViewPager.setCurrentItem(0);
            return true;
        } else if (id == R.id.im_bottom_friends) {
            mViewPager.setCurrentItem(1);
            return true;
        } else if (id == R.id.im_bottom_add_friends) {
            mViewPager.setCurrentItem(2);
            return true;
        }
        return false;
    }
}
