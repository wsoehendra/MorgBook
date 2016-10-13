package com.willnjames.android.morgbook.Model;

/**
 * Created by wsoeh on 13/10/2016.
 */

public class Attendance {

    int a_ID;
    int z_ID;
    int weekNo;
    String status;

    public Attendance(int a_ID, int z_ID, int weekNo, String status) {
        this.a_ID = a_ID;
        this.z_ID = z_ID;
        this.weekNo = weekNo;
        this.status = status;
    }

    public Attendance(int z_ID, int weekNo, String status) {
        this.z_ID = z_ID;
        this.weekNo = weekNo;
        this.status = status;
    }

    public int getA_ID() {
        return a_ID;
    }

    public void setA_ID(int a_ID) {
        this.a_ID = a_ID;
    }

    public int getZ_ID() {
        return z_ID;
    }

    public void setZ_ID(int z_ID) {
        this.z_ID = z_ID;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "A_ID: "+a_ID+"\n"
                +"ZID: "+z_ID+"\n"
                +"Week No: "+weekNo+"\n"
                +"Status: "+status+"\n";
    }
}
