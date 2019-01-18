package cn.krisez.shareroute.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cn.krisez.shareroute.R;
import cn.krisez.shareroute.base.BackHandleFragment;
import cn.krisez.shareroute.inter.BackHandleInterface;

public class LoadFragment extends AppCompatActivity implements BackHandleInterface {
    String cls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_fragment);

        Intent intent = getIntent();
        cls = intent.getStringExtra("cls");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cls);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        switch (cls) {
            case "set":
                //FManager.fmReplace(getSupportFragmentManager(), new SettingFragment(), R.id.load_fragment);
                break;
            case "about":
                //mAboutAppFragment = new AboutAppFragment();
               // FManager.fmReplace(getSupportFragmentManager(),new AboutAppFragment(), R.id.load_fragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(backHandleFragment == null || !backHandleFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }
    private BackHandleFragment backHandleFragment;

    @Override
    public void onSelectedFragment(BackHandleFragment backHandleFragment) {
        this.backHandleFragment = backHandleFragment;
    }
}
