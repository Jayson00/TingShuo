package com.hjm.tingshuo.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


import com.gyf.barlibrary.ImmersionBar;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.R;

import butterknife.BindView;

public class SplashActivity extends AppCompatActivity {

    ImageView imSplash;
    AlphaAnimation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        imSplash = findViewById(R.id.im_splash);
        animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(3000);
        imSplash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.cancel();
            }
        });
    }

}
