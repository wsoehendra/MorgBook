package com.willnjames.android.morgbook.Model;

/**
 * Created by jamesprijatna on 18/10/16.
 */

public class Meeting {

    int m_ID;
    int z_ID;
    String location;
    String dateTime;
    String tutor; //??


    public Meeting (int m_ID, int z_ID, String location, String dateTime, String tutor){

        this.m_ID = m_ID;
        this.z_ID = z_ID;
        this.location = location;
        this.dateTime = dateTime;
        this.tutor = tutor;

    }

    public Meeting (int z_ID, String location, String dateTime, String tutor){

        this.z_ID = z_ID;
        this.location = location;
        this.dateTime = dateTime;
        this.tutor = tutor;

    }

    public int getM_ID() {
        return m_ID;
    }

    public void setM_ID(int m_ID) {
        this.m_ID = m_ID;
    }

    public int getZ_ID() {
        return z_ID;
    }

    public void setZ_ID(int z_ID) {
        this.z_ID = z_ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    @Override
    public String toString(){
        return  ""+"\n"
                +"Student: "+"z500000"+z_ID+"\n"
                +"Time: "+dateTime+"\n"
                +"Location: "+location+"\n";
    }
}
