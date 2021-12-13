package com.example.study_with_me.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.study_with_me.R;

public class SplachActivity extends Activity {
    Animation anim_FadeIn;
    Animation anim_ball;
    ConstraintLayout constraintLayout;
    TextView studyApplication;
    TextView doIt;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash);

        constraintLayout = findViewById(R.id.constraintLayout);
        studyApplication = findViewById(R.id.studyApplication);
        doIt = findViewById(R.id.doIt);

        anim_FadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_splash_fade);
        anim_ball = AnimationUtils.loadAnimation(this, R.anim.anim_splash_ball);

        anim_FadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplachActivity.this, LoginActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        studyApplication.startAnimation(anim_FadeIn);
        doIt.startAnimation(anim_ball);
    }
}
