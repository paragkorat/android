package com.demo.parag.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VollyActivity extends AppCompatActivity {
    private TextView txtName,txtEmail,txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volly);
        getSupportActionBar().setTitle("volly");

        findViews();
        volleyWebCall();

    }

    private void findViews() {

        txtName = findViewById(R.id.txtNameV);
        txtEmail = findViewById(R.id.txtEmailV);
        txtPhone = findViewById(R.id.txtPhoneV);


    }

    private void volleyWebCall() {
        final JSONObject jsonObject=formJson();

        RequestQueue queue = Volley.newRequestQueue(this);



        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, Configuration.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.substring(3);
                Log.d("responseVolly",""+response);

                Log.d("test","test :"+jsonObject.toString());

                JSONObject childjsonObject= null;
                try {
                    JSONObject njsonObject=new JSONObject(response);

                    childjsonObject = njsonObject.getJSONObject("user");
                    Log.d("childTest",""+childjsonObject);
                    txtName.setText(childjsonObject.getString("name"));
                    txtPhone.setText(childjsonObject.getString("mobile"));
                    txtEmail.setText(childjsonObject.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", error.getMessage());

            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("reqObject",jsonObject.toString()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        queue.add(MyStringRequest);



    }

    private JSONObject formJson() {

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("task","get_user_profile");
            SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            int userID=sharedpreferences.getInt("userID",-99);
            jsonObject.put("user_id",354);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
