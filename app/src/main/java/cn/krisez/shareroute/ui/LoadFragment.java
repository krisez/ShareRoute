package cn.krisez.shareroute.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import cn.krisez.kotlin.ui.fragment.AboutAndQuestionFragment;
import cn.krisez.kotlin.ui.fragment.SettingFragment;
import cn.krisez.shareroute.R;
import cn.krisez.shareroute.utils.Const;
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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        switch (cls) {
            case "set":
                FManager.fmReplace(getSupportFragmentManager(), new SettingFragment(), R.id.load_fragment);
                break;
            case "aq":
                FManager.fmReplace(getSupportFragmentManager(), new AboutAndQuestionFragment(), R.id.load_fragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(!Const.downLoadFile){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "请等待更新完成,谢谢~", Toast.LENGTH_SHORT).show();
        }
    }
}
