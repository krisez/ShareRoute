package cn.krisez.shareroute.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cn.krisez.kotlin.ui.fragment.SettingFragment;
import cn.krisez.shareroute.R;
import cn.krisez.shareroute.utils.FManager;

public class LoadFragment extends AppCompatActivity{
    String cls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_fragment);

        Intent intent = getIntent();
        cls = intent.getStringExtra("cls");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        switch (cls) {
            case "set":
                FManager.fmReplace(getSupportFragmentManager(), new SettingFragment(), R.id.load_fragment);
                break;
        }
    }
}
