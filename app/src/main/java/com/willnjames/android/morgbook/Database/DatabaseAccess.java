package com.willnjames.android.morgbook.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.willnjames.android.morgbook.Model.Attendance;
import com.willnjames.android.morgbook.Model.Meeting;
import com.willnjames.android.morgbook.Model.Person;
import com.willnjames.android.morgbook.Model.Progress;

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

    //Get a Student's Progress
    public ArrayList<Progress> getStudentProgress(int ZID){
        ArrayList<Progress> progressList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM PROGRESS WHERE Z_ID="+ZID+" ORDER BY WEEKNO",null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        while(!cursor.isAfterLast()) {
            Progress p = new Progress(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4)
            );
            progressList.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return progressList;
    }

    public Person getPerson(int zID){
        Cursor cursor = database.rawQuery("SELECT * FROM PERSONS WHERE Z_ID='"+zID+"'", null);
        cursor.moveToFirst();
        Person p = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return p;
    }

    public void addProgress(Progress p) {
        Cursor checkIfExists = database.rawQuery("SELECT * FROM PROGRESS WHERE Z_ID='" + p.getZ_ID() + "' " +
                "AND WEEKNO='" + p.getWeekNo() + "'", null);
        if (checkIfExists.getCount() == 0) {
            try {
                String sqlCommand = "INSERT INTO PROGRESS (Z_ID, PROGRESS, WEEKNO, NOTES) " +
                        "VALUES ('" + p.getZ_ID() + "','" + p.getProgress() + "','" + p.getWeekNo() + "','" + p.getNotes() + "')";
                database.execSQL(sqlCommand);
                Log.d("QUERY|ADD", "Adding Progress Successful!" + "\n" + p.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Adding Progress Failed!" + "\n" + e.toString());
            }
        } else if (checkIfExists.getCount() > 0) {
            try {
//                String sqlCommand = "UPDATE PROGRESS " +
//                        "SET PROGRESS='" + p.getProgress() + "','" + "NOTES='BLAH' " +
//                        "WHERE Z_ID='" + p.getZ_ID() + "' AND WEEKNO='" + p.getWeekNo() + "'";
                String sqlCommand = "UPDATE PROGRESS SET PROGRESS='Bad',NOTES='N/A WHERE Z_ID=5010004 AND WEEKNO=4";
                database.execSQL(sqlCommand);
                Log.d("QUERY|ADD", "Updating Progress Successful!" + "\n" + p.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Updating Progress Failed!" + "\n" + e.toString());
            }
        }
    }

    //Meetings

    public ArrayList<Meeting> getMeeting() {
        ArrayList<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM MEETINGS", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Meeting m = new Meeting(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            meetingList.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("QUERY|GET", "All Meeting: " + meetingList.toString());
        return meetingList;
    }

    public void addMeeting(Meeting m) {
        Cursor checkIfExists = database.rawQuery("SELECT * FROM MEETINGS", null);
        if (checkIfExists.getCount() == 0) {
            try {
                String sqlCommand = "INSERT INTO MEETINGS (STU_ZID, STA_ZID, DATE, START_TIME, END_TIME, TOPIC, ROOM) " +
                        "VALUES ('" + m.getStudentZID() + "','" + m.getStaffZID() + "','" + m.getDate() + "','" + m.getStartTime() + "','" + m.getEndTime() + "','" + m.getTopic() + "','" + m.getRoom() + "')";
                database.execSQL(sqlCommand);
                Log.d("QUERY|ADD", "Adding Meeting Successful!" + "\n" + m.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Adding Meeting Failed!" + "\n" + e.toString());
            }
        } else if (checkIfExists.getCount() > 0) {
            try {
                /*String sqlCommand = "UPDATE MEETING " +
                        "SET STATUS='" + a.getStatus() + "' " +
                        "WHERE Z_ID='" + a.getZ_ID() + "' AND WEEKNO='" + a.getWeekNo() + "'";
                database.execSQL(sqlCommand);*/
                Log.d("QUERY|ADD", "Updating Meeting Successful!" + "\n" + m.toString());
            } catch (Exception e) {
                Log.d("QUERY|ADD", "Updating Meeting Failed!" + "\n" + e.toString());
            }
        }
    }
}
