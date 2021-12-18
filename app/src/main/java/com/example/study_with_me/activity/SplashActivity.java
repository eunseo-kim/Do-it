package com.example.study_with_me.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.study_with_me.R;

public class SplashActivity extends Activity {
    Animation anim_FadeIn;
    Animation anim_ball;
    TextView studyApplication;
    TextView doIt;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash);

        initialize();

        studyApplication = findViewById(R.id.studyApplication);
        doIt = findViewById(R.id.doIt);

        anim_FadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_splash_fade);
        anim_ball = AnimationUtils.loadAnimation(this, R.anim.anim_splash_ball);

        studyApplication.startAnimation(anim_FadeIn);
        doIt.startAnimation(anim_ball);
    }
    private void initialize() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 1560);
    }
}
