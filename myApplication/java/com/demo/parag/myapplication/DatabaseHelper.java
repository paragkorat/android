package com.demo.parag.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NSME="contact.db";
    public static final String TABLE_NAME="contact";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NSME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,MOBILE_NUMBER INTEGER,EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }
    public Boolean insertdata(String name,int mobile_number,String email){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("MOBILE_NUMBER",mobile_number);
        contentValues.put("email",email);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)return false;
        else return true;

    }
}
