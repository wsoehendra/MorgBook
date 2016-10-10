package com.willnjames.android.morgbook.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.willnjames.android.morgbook.Model.Person;

import java.util.ArrayList;

/**
 * Created by wsoeh on 10/10/2016.
 */

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpener(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    //Open connection
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    //close connection
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public ArrayList<Person> testGetStudents(){
        ArrayList<Person> studentsList = new ArrayList<Person>();
        Cursor cursor  = database.rawQuery("SELECT * FROM PERSONS", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Person p = new Person();
            p.setZ_ID(cursor.getInt(0));
            p.setFName(cursor.getString(1));
            p.setLName(cursor.getString(2));
            p.setRole(cursor.getString(3));
            studentsList.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TEST QUERY",studentsList.toString());
        return studentsList;
    }

}
