package com.demo.parag.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilEmail;
    private TextInputEditText edtEmail;
    private Button btnSubmit;
    String responseString;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(getResources().getString(R.string.login));

        findViews();
        btnSubmit.setOnClickListener(this);
        edtEmail.addTextChangedListener(edtEmailWatcher);

    }

    private void findViews() {
        tilEmail = findViewById(R.id.tilEmail);
        edtEmail = findViewById(R.id.edtEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
        startActivity(intent);
        finish();

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSubmit:

                String email = edtEmail.getText().toString();

                if (email.length() <= 0) {

                    tilEmail.setError(getResources().getString(R.string.empty_error));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    tilEmail.setError(getResources().getString(R.string.invalide_error));
                }
                final JSONObject jsonObject=new JSONObject();
                try {

                    jsonObject.put("task","login");

                    jsonObject.put("email",email);
                    Log.d("test","test :"+jsonObject.toString());
                    //jsonObject.toString();
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
    }
    TextWatcher edtEmailWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i2 > 0) {

                tilEmail.setError(null);
                tilEmail.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

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

                    Toast.makeText(LoginActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse=new JSONObject(newRespStr);
            Log.d("email","email"+jsonResponse.getString("code"));

            if(jsonResponse.getInt("code") == 200){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(LoginActivity.this,"Login Detail sucessfull..", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,OTPActivity.class);
                        intent.putExtra("email",edtEmail.getText().toString());
                        startActivity(intent);
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Toast.makeText(LoginActivity.this,jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
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
