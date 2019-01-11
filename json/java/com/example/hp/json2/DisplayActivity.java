package com.example.hp.json2;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    ListView calist;
    static InputStream is=null;
    static String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        calist=findViewById(R.id.calist);
        Displayadapter dis=new Displayadapter();
        dis.execute();
    }

    private class Displayadapter extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ArrayList<Model> arrayList=new ArrayList<Model>();
            DefaultHttpClient httpClient= new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://192.168.2.31/insert/display.php");

            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line=reader.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                is.close();
                json = sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jo=new JSONObject(json);
                JSONArray jsonArray=jo.getJSONArray("Details");
                for (int i=0;i<jsonArray.length();i++) {
                    Model student = new Model();
                    JSONObject c = jsonArray.getJSONObject(i);
                    String no = c.getString("no");
                    String name = c.getString("name");
                    String email = c.getString("email");
                    student.setNo(no);
                    student.setName(name);
                    student.setEmail(email);
                    arrayList.add(student);
                    CustomAdapter adapter = new CustomAdapter(DisplayActivity.this, arrayList);
                    calist.setAdapter((ListAdapter) adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);
        }
    }
}
