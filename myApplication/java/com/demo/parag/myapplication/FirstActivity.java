package com.demo.parag.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);



        //Button btn=findViewById(R.id.btnShowText);
        //Button btn=findViewById(R.id.btnHideText);


    }
    public void showClick (View button){

        EditText userName=findViewById(R.id.userName);
        String uName=userName.getText().toString();

        EditText email=findViewById(R.id.email);
        String mail=email.getText().toString();

        EditText mobileNumber=findViewById(R.id.mobileNumber);
        String mNumber=mobileNumber.getText().toString();

        EditText password=findViewById(R.id.password);
        String pwd=password.getText().toString();

        EditText address=findViewById(R.id.address);
        String adr=address.getText().toString();

        EditText Date=findViewById(R.id.date);
        String date=Date.getText().toString();

        EditText Time=findViewById(R.id.time);
        String time=Time.getText().toString();

        TextView txtView=findViewById(R.id.txtLabel);

        txtView.setText(uName);
        txtView.setText(txtView.getText().toString()+mail);
        txtView.setText(txtView.getText().toString()+mNumber);
        txtView.setText(txtView.getText().toString()+pwd);
        txtView.setText(txtView.getText().toString()+adr);
        txtView.setText(txtView.getText().toString()+date);
        txtView.setText(txtView.getText().toString()+time);



    }
    public void hideClick (View button){

        TextView txtView=findViewById(R.id.txtLabel);
        txtView.setText("");


    }

}

