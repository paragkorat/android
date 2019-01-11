package com.demo.parag.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilName, tilEmail, tilMobile, tilDate, tilAddress;
    private TextInputEditText edtName, edtEmail, edtMobile, edtDate, edtAddress;
    private ImageView imgUser;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private Button btnSubmit;
    private TextView txtGenderError;
    private int  mYear,mMonth, mDay;
    String responseString=null;
    public String temp;
    Handler handler=new Handler();
    int PICK_IMAGE_REQUEST=1;
    Bitmap bitmap;




    final Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle(getResources().getString(R.string.registration));

        findViews();
        btnSubmit.setOnClickListener(this);
        edtMobile.addTextChangedListener(edtMobileWatcher);
        edtEmail.addTextChangedListener(edtEmailWatcher);
        edtName.addTextChangedListener(edtNameWatcher);
        edtAddress.addTextChangedListener(edtAddressWatcher);
        edtDate.addTextChangedListener(edtDateWatcher);
        edtDate.setOnClickListener(this);
        imgUser.setOnClickListener(this);
        rdbMale.setOnCheckedChangeListener(new Radio_check());
        rdbFemale.setOnCheckedChangeListener(new Radio_check());

        mYear = c.get(Calendar.YEAR)-21;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_registration,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

        return super.onOptionsItemSelected(item);

    }



    private void findViews() {
        tilName = findViewById(R.id.tilName);
        edtName = findViewById(R.id.edtName);
        tilEmail = findViewById(R.id.tilEmail);
        edtEmail = findViewById(R.id.edtEmail);
        tilMobile = findViewById(R.id.tilMobile);
        edtMobile = findViewById(R.id.edtMobile);
        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);
        tilDate = findViewById(R.id.tilDate);
        edtDate = findViewById(R.id.edtDate);
        tilAddress = findViewById(R.id.tilAddress);
        edtAddress = findViewById(R.id.edtAddress);
        imgUser=findViewById(R.id.imgUser);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtGenderError = findViewById(R.id.txtGenderError);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSubmit:

               if( isInputValid()){

                   webCallThread.start();
               }


                break;
            case R.id.edtDate:

                showDatePickerDialog();

                break;

            case R.id.imgUser:

                setImage();
                break;

        }

    }

    private void setImage() {

            Intent intent = new Intent ();
            intent.setType ("image/*");
            intent.setAction (Intent.ACTION_GET_CONTENT);
            startActivityForResult (Intent.createChooser (intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mYear=year;
                        mMonth=monthOfYear;
                        mDay=dayOfMonth;

                        if(mYear>c.get(Calendar.YEAR)-18){
                            btnSubmit.setEnabled(false);
                            tilDate.setError("Your age under 18+ So not eligiable for enter in Application..!!");
                        }else if(mYear<=c.get(Calendar.YEAR)-18){
                            btnSubmit.setEnabled(true);
                            tilDate.setError(null);
                            tilDate.setErrorEnabled(false);
                        }
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    Thread webCallThread =new Thread(new Runnable() {
        @Override
        public void run() {

            JSONObject formedJson=formJson();
            placeWebCall(Configuration.URL,formedJson);
        }
    });

    private JSONObject formJson() {

        final JSONObject jsonObject=new JSONObject();
        try {

            jsonObject.put("task","register");
            jsonObject.put("name",edtName.getText().toString());
            jsonObject.put("email",getEdtValue(edtEmail));
            jsonObject.put("mobile",getEdtValue(edtMobile));
            String strGendr=rdbMale.isChecked()?"M":"F";
            jsonObject.put("gender",strGendr);
            jsonObject.put("dob",getEdtValue(edtDate));
            jsonObject.put("address",getEdtValue(edtAddress));
            jsonObject.put("avatar",temp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private String getEdtValue(TextInputEditText edt) {

       return edt.getText().toString().trim();
    }

    private boolean isInputValid() {


        boolean isValid=true;

        if (isEdtEmpty(edtName)) {

            tilName.setError(getResources().getString(R.string.empty_error));
            isValid=false;
        } else if (isEdtEmpty(edtEmail)) {

            tilEmail.setError(getResources().getString(R.string.empty_error));
            isValid=false;

        } else if (isEdtEmpty(edtMobile)) {

            tilMobile.setError(getResources().getString(R.string.empty_error));
            isValid=false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {

            tilEmail.setError(getResources().getString(R.string.invalide_error));
        } else if ((!rdbMale.isChecked()) && (!rdbFemale.isChecked())) {
            txtGenderError.setVisibility(View.VISIBLE);
            txtGenderError.setText(getResources().getString(R.string.select_gender));
        } else if (isEdtEmpty(edtDate)) {

            tilDate.setError(getResources().getString(R.string.empty_error));
        } else if (isEdtEmpty(edtAddress)) {

            tilAddress.setError(getResources().getString(R.string.empty_error));
        }

        return isValid;
    }

    private boolean isEdtEmpty(TextInputEditText edt) {

        return edt.getText().toString().trim().length()<=0;
    };

    TextWatcher edtNameWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (count > 0) {

                tilName.setError(null);
                tilName.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
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
    TextWatcher edtMobileWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i2 > 0) {

                tilMobile.setError(null);
                tilMobile.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher edtAddressWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i2 > 0) {

                tilAddress.setError(null);
                tilAddress.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher edtDateWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i2 > 0) {

                tilDate.setError(null);
                tilDate.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    class Radio_check implements  CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if((!rdbMale.isChecked()) || (!rdbFemale.isChecked()))
            {
                txtGenderError.setVisibility(View.GONE);
            }
            else
            {
                txtGenderError.setText(getResources().getString(R.string.select_gender));
            }
        }
    }


    private void placeWebCall(String url,JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("reqObject", jsonObject.toString());
            if(bitmap!=null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte [] data = bos.toByteArray();
                long l=System.currentTimeMillis();
                entityBuilder.addPart("avatar", new ByteArrayBody(data,"image/jpeg", "image"+l+".jpg"));
            }

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
            responseString = out.toString();

            final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(RegistrationActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse=new JSONObject(newRespStr);

                if(jsonResponse.getInt("code") == 200){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(RegistrationActivity.this,"Registration sucessfull..", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegistrationActivity.this,OTPActivity.class);
                            intent.putExtra("email",edtEmail.getText().toString());
                            startActivity(intent);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Toast.makeText(RegistrationActivity.this,jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imgUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}