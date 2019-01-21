package com.demo.parag.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SQLiteActivity extends AppCompatActivity {
    EditText name,mobile,email;
   private Button submit;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        databaseHelper=new DatabaseHelper(this);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              boolean result=  databaseHelper.insertdata(name.getText().toString(),Integer.parseInt(mobile.getText().toString()),email.getText().toString());
                if(result)
                    Toast.makeText(SQLiteActivity.this,"insert successfully..",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SQLiteActivity.this,"insert Failed..",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
