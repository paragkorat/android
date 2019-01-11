package com.demo.parag.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       student s=new student("parag",21);
         Log.d("school",""+s.age);
       Log.d("Student",""+s.getStudentName());
      /*   s.setAge(21);
        Log.d("age",""+s.getAge());

        teacher t=new teacher();
        Log.d("school",""+t.getName());
        //Log.d("Student",""+t.getNAme());
*/
       newSchool ns=new newSchool();
       Log.d("school",""+ns.getName());

    }


}
