package com.demo.parag.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgUser;
    private TextView txtName,txtEmail,txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        findViews();
        GetProfileTask getProfileTask=new GetProfileTask();
        getProfileTask.execute();

        imgUser.setOnClickListener(this);


    }


    private void findViews() {

        imgUser = findViewById(R.id.imgUser);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent ();
        intent.setType ("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (Intent.createChooser (intent, "Select Picture"), 3);
    }


    class GetProfileTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            JSONObject jsonObject=formJson();
            String responseString=placeWebCall(Configuration.URL,jsonObject);

            return responseString;
        }

        @Override
        protected void onPostExecute(String string) {

            try {

                JSONObject jsonObject=new JSONObject(string);
                Log.d("test","test :"+jsonObject.toString());

                JSONObject childjsonObject=jsonObject.getJSONObject("user");
                txtName.setText(childjsonObject.getString("name"));
                txtPhone.setText(childjsonObject.getString("mobile"));
                txtEmail.setText(childjsonObject.getString("email"));

                imgUser.setImageURI(Uri.parse(jsonObject.getString("avatar")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject formJson() {

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("task","get_user_profile");
            SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            int userID=sharedpreferences.getInt("userID",-99);
            jsonObject.put("user_id",userID);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private String placeWebCall(String url, JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String responseString=null;

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("reqObject", jsonObject.toString());
            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
            HttpResponse resp = null;
            resp = client.execute(httpPost);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                resp.getEntity().writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Convert response into string
            responseString = out.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int PICK_IMAGE_REQUEST=1;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.imgUser);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



