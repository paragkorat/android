package com.demo.parag.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class OTPActivity extends Activity implements View.OnClickListener {
    private TextInputLayout tilOTP;
    private TextInputEditText edtOTP;
    private Button btnVerify;
    String responseString;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_otp);

        findviews();
        btnVerify.setOnClickListener(this);

    }

    private void findviews() {
        edtOTP=findViewById(R.id.edtOTP);
        btnVerify=findViewById(R.id.btnVerify);
        tilOTP=findViewById(R.id.tilOTP);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnVerify:

                String otp = edtOTP.getText().toString();

                if (otp.length() <= 0) {

                    tilOTP.setError(getResources().getString(R.string.empty_error));
                }
                final JSONObject jsonObject=new JSONObject();
                try {

                    jsonObject.put("task","verify_otp");
                    String email=getIntent().getStringExtra("email");
                    jsonObject.put("email",email);
                    jsonObject.put("otp",otp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //send this json to server
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        placeWebCall(Configuration.URL,jsonObject);
                    }
                });
                thread.start();
                break;
        }
    };

    private void placeWebCall(String url,JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("reqObject", jsonObject.toString());
            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
            HttpResponse resp = null;
            resp = client.execute(httpPost);

            ByteArrayOutputStream out = new ByteArrayOutputStream ();
            try {
                resp.getEntity ().writeTo (out);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            //Convert response into string

            responseString = out.toString();

           /* String delStr = "SMTP Error: Could not connect to SMTP host.";
            final String newStr= responseString.replace(delStr, "");*/

            final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(OTPActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse=new JSONObject(newRespStr);

            if(jsonResponse.getInt("code") == 200){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedpreferences.edit();
                        try {
                            editor.putInt("userID",jsonResponse.getInt("user_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                        Toast.makeText(OTPActivity.this,"Login sucessfull..", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(OTPActivity.this,ProfileActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Toast.makeText(OTPActivity.this,jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
