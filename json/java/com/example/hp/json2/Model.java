package com.example.hp.json2;

/**
 * Created by HP on 27-02-2018.
 */

public class Model {

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    String no;
    String name;
    String email;

    public String tostring()
    {
        return no+name+email;
    }
}

