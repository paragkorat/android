package com.demo.parag.myapplication;

public class teacher extends SchoolActivity {
    private String teacherName="teacher 1";
    public String getName() {

        return super.getName()+teacherName;
    }

}
