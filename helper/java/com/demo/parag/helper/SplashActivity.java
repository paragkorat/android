package com.demo.parag.helper;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.jetbrains.annotations.Nullable;

public class SplashActivity extends AppCompatActivity{
    private ImageView logo;
    private static int splashTimeOut=5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_splash)); //status bar or the time bar at the top
        }
        setContentView(R.layout.activity_splash);


        logo = findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        logo.startAnimation(myanim);

    /*    Thread thread=new Thread(runnable);
        thread.start();
    }

    private void waitForFew() {
        try {
            Thread.sleep(1000*splashTimeOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void goToRegistration() {
        Intent menuIntent = new Intent(SplashActivity.this, .class);
        startActivity(menuIntent);

    }


    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            waitForFew();
            goToRegistration();
            Animation myanim = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.mysplashanimation);
            logo.startAnimation(myanim);
        }
    };
*/
    }


}
