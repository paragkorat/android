package com.demo.parag.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        Thread thread=new Thread(runnable);
        thread.start();
    }

    private void waitForFew() {
        try {
            Thread.sleep(3000);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void goToLogin() {

        Intent menuIntent = new Intent(this, LoginActivity.class);
        startActivity(menuIntent);
    }

    private void goToHome() {

        Intent menuIntent = new Intent(this, HomeActivity.class);
        startActivity(menuIntent);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            waitForFew();

            SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            final int userID=sharedpreferences.getInt("userID",-99);

            if(userID==-99){
                Log.d("user id","user_id :"+userID);
                goToLogin();
            }else{
                goToHome();
            }
        }
    };

}
