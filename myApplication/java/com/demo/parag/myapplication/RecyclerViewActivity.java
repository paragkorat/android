package com.demo.parag.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {
    private JSONObject jsonObject = null;
    private Handler handler = new Handler();
    private User objUser;
    private ArrayList<User> allUser = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageView imgUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        getUserData();

        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter();

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout linearLayout;
            TextView txtName,txtEmail,txtDob,txtMobile,txtAddress;
            ImageView imgUser;
            public MyViewHolder(View linearLayout) {
                super(linearLayout);
                txtName=linearLayout.findViewById(R.id.txt_name);
                txtEmail=linearLayout.findViewById(R.id.txt_email);
                txtDob=linearLayout.findViewById(R.id.txt_dob);
                txtMobile=linearLayout.findViewById(R.id.txt_mobile);
                txtAddress=linearLayout.findViewById(R.id.txt_address);
                imgUser=linearLayout.findViewById(R.id.imageUser);


            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           // Log.d("myAdapter","adpater mehod");

            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);

            MyViewHolder vh = new MyViewHolder(linearLayout);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
           // Log.d("position","size:"+ allUser.get(position));

           // Log.d("onBind","onbind mehod");

            holder.txtName.setText(allUser.get(position).getName());
            holder.txtEmail.setText(allUser.get(position).getEmail());
            holder.txtDob.setText(allUser.get(position).getDOB());
            holder.txtMobile.setText(allUser.get(position).getMobile());
            holder.txtAddress.setText(allUser.get(position).getAddress());

            // bitmap=allUser.get(position).getUserImage();
          //   holder.imgUser.setImageBitmap(getBitmapFromURL(allUser.get(position).getUserImage()));

        }

        @Override
        public int getItemCount() {
         //  Log.d("countSize","size:"+ allUser.size());

            return allUser.size();

        }


    }

    public void getUserData() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                final String response = placeWebCall();
                allUser=new ArrayList<>();
                try {
                    jsonObject = new JSONObject(response);

                    final JSONArray userJson = jsonObject.getJSONArray("users");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //Log.d("userJson", String.valueOf(userJson.length()));

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject jsonObjectChild = userJson.getJSONObject(i);

                                    objUser = new User();
                                    objUser.setUserID(Integer.parseInt(jsonObjectChild.getString("user_id")));
                                    objUser.setName(jsonObjectChild.getString("name"));
                                    objUser.setEmail(jsonObjectChild.getString("email"));
                                    objUser.setMobile(jsonObjectChild.getString("mobile"));
                                    objUser.setAddress(jsonObjectChild.getString("address"));
                                    objUser.setUserImage(jsonObjectChild.getString("avatar"));

                                  if ((!TextUtils.isEmpty(jsonObjectChild.getString("latitude"))) && (!TextUtils.isEmpty(jsonObjectChild.getString("longitude"))))
                                    {

                                        objUser.setLatitude(Double.parseDouble(jsonObjectChild.getString("latitude")));
                                        objUser.setLongitude(Double.parseDouble(jsonObjectChild.getString("longitude")));

                                    }

                                    allUser.add(objUser);
                                    //Log.d("count","size:"+ allUser.size());

                                }
                                recyclerView.setAdapter(adapter);

                               /* for(int j = 0; j < userJson.length(); j++)
                                {
                                    User u=allUser.get(j);
                                    Log.d("id+name",""+u.getUserID()+" : "+u.getName());

                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private String placeWebCall() {

        String responseString = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Configuration.URL);

        try {

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //Log.d("response", formJson().toString());
            entityBuilder.addTextBody("reqObject", formJson().toString());
            HttpEntity entity = entityBuilder.build();

            httpPost.setEntity(entity);
            HttpResponse resp = null;
            try {

                resp = client.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                resp.getEntity().writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            responseString = out.toString();
            //final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
           // final JSONObject jsonResponse=new JSONObject(newRespStr);
           // Log.d("avatar2",""+jsonResponse.getString("avatar"));

          //  Log.d("response2", responseString);


        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseString;
    }

    private JSONObject formJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("task", "user_list");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
