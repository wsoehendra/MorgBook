package com.willnjames.android.morgbook.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.willnjames.android.morgbook.Model.Attendance;
import com.willnjames.android.morgbook.Model.Meeting;
import com.willnjames.android.morgbook.Model.Person;

import java.util.ArrayList;

/**
 * Created by wsoeh on 10/10/2016.
 */

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
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

    //Get ALL Students
    public ArrayList<Person> getStudents() {
        ArrayList<Person> studentsList = new ArrayList<Person>();
        Cursor cursor = database.rawQuery("SELECT * FROM PERSONS WHERE ROLE IS 'Student' ORDER BY LNAME", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Person p = new Person(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            studentsList.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("QUERY|GET", "List of Students: " + studentsList.toString());
        return studentsList;
    }

    public ArrayList<Attendance> getAttendance() {
        ArrayList<Attendance> attendanceList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ATTENDANCE", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Attendance a = new Attendance(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3)
            );
            attendanceList.add(a);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("QUERY|GET", "All Attendance: " + attendanceList.toString());
        return attendanceList;
    }

    public void addAttendance(Attendance a) {
        Cursor checkIfExists = database.rawQuery("SELECT * FROM ATTENDANCE WHERE Z_ID='" + a.getZ_ID() + "' " +
                "AND WEEKNO='" + a.getWeekNo() + "'", null);
        if (checkIfExists.getCount() == 0) {
            try {
                String sqlCommand = "INSERT INTO ATTENDANCE (Z_ID, WEEKNO, STATUS) " +
                        "VALUES ('" + a.getZ_ID() + "','" + a.getWeekNo() + "','" + a.getStatus() + "')";
                database.execSQL(sqlCommand);
                Log.d("QUERY|ADD", "Adding Attendance Successful!" + "\n" + a.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Adding Attendance Failed!" + "\n" + e.toString());
            }
        } else if (checkIfExists.getCount() > 0) {
            try {
                String sqlCommand = "UPDATE ATTENDANCE " +
                        "SET STATUS='" + a.getStatus() + "' " +
                        "WHERE Z_ID='" + a.getZ_ID() + "' AND WEEKNO='" + a.getWeekNo() + "'";
                database.execSQL(sqlCommand);
                Log.d("QUERY|ADD", "Updating Attendance Successful!" + "\n" + a.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Updating Attendance Failed!" + "\n" + e.toString());
            }
        }
    }

    public int getAbsentCount(){
        Cursor cursor = database.rawQuery("SELECT * FROM ATTENDANCE WHERE STATUS = 'Absent' OR 'Explained Absence'", null);
        return cursor.getCount();
    }

    //Get A Student
    public Person getStudent(int ZID){
        Cursor cursor = database.rawQuery("SELECT * FROM PERSONS WHERE Z_ID='"+ZID+"'",null);
        cursor.moveToFirst();
        Person p = new Person(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );

        return p;
    }
}
