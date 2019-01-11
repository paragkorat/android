package com.demo.parag.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LifecycleActivity extends AppCompatActivity implements View.OnClickListener {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("1st Activity");

        setContentView(R.layout.activity_lifecycle);
        next=findViewById(R.id.nxtbtn);
        next.setOnClickListener(this);

    }

    public void onClick(View view){


        if(view.getId()==R.id.nxtbtn){
            Intent intent=new Intent(LifecycleActivity.this,SecondActivity.class);
            startActivity(intent);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"1st act - on start",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"1st act - on resume",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(),"1st act - on pause",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),"1st act - on stop",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"1st act - on restart",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"1st act - on destroy",Toast.LENGTH_SHORT).show();

    }

}
