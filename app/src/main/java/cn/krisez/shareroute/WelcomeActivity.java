package cn.krisez.shareroute;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.krisez.kotlin.ui.activity.LoginActivity;
import cn.krisez.kotlin.ui.activity.MainActivity;
import cn.krisez.shareroute.utils.SPUtil;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView textView = findViewById(R.id.tv_welcome);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView,"rotation",0f,1800f);
        animator.setDuration(2000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != SPUtil.getUser()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
