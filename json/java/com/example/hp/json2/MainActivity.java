package com.example.hp.json2;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText name,email;
    Button sub;
    static InputStream is=null;
    static String json="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        sub=findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_LONG).show();

                InsertData insertData = new InsertData();
                insertData.execute();
            }
        });
    }
    private class InsertData extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            ArrayList<NameValuePair> arrayList =new ArrayList<NameValuePair>();
            arrayList.add(new BasicNameValuePair("name", name.getText().toString()));
            arrayList.add(new BasicNameValuePair("email",email.getText().toString()));
            DefaultHttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.2.23/data/insert.php");
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Toast.makeText(MainActivity.this, "is="+is, Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader b=new BufferedReader(new InputStreamReader(is,"ISO-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = b.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }
    }
}