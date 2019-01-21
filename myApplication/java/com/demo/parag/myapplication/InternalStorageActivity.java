package com.demo.parag.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InternalStorageActivity extends AppCompatActivity {

    TextView sumtext;
    EditText num1,num2;
    Button sumbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        sumtext=findViewById(R.id.sumtext);
        num1=findViewById(R.id.num1);
        num2=findViewById(R.id.num2);
        sumbtn=findViewById(R.id.sum);
        readdata();
        sumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum= Integer.valueOf(num1.getText().toString())+Integer.valueOf(num2.getText().toString());
                sumtext.setText("sum ="+String.valueOf(sum));
                savedata();
            }
        });
    }
    public void savedata(){
        try {
            FileOutputStream fileOutputStream=openFileOutput("sum.txt",MODE_PRIVATE);
            fileOutputStream.write(sumtext.getText().toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readdata(){
        try {
            FileInputStream fileInputStream=openFileInput("sum.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String data=reader.readLine();
            if(!data.isEmpty())
                sumtext.setText(data);
                else
                sumtext.setText("0");

        } catch (FileNotFoundException e) {
            sumtext.setText("0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
