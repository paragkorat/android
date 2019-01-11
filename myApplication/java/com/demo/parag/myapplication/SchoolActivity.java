package com.demo.parag.myapplication;

public class SchoolActivity {
    String schoolName;

    public String getName() {
        return schoolName="AIT";
    }

}
class newSchool extends SchoolActivity {
    String schoolName = "new AIT";

    @Override
    public String getName() {
        return super.getName();
    }
}
