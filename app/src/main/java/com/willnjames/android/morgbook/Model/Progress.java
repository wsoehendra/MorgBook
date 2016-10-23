package com.willnjames.android.morgbook.Model;

/**
 * Created by wsoeh on 22/10/2016.
 */

public class Progress {

    int p_ID;
    int z_ID;
    String progress;
    int weekNo;
    String notes;

    public Progress(int z_ID, String progress, int weekNo, String notes) {
        this.z_ID = z_ID;
        this.progress = progress;
        this.weekNo = weekNo;
        this.notes = notes;
    }

    public Progress(int p_ID, int z_ID, String progress, int weekNo, String notes) {
        this.p_ID = p_ID;
        this.z_ID = z_ID;
        this.progress = progress;
        this.weekNo = weekNo;
        this.notes = notes;
    }

    public int getP_ID() {
        return p_ID;
    }

    public void setP_ID(int p_ID) {
        this.p_ID = p_ID;
    }

    public int getZ_ID() {
        return z_ID;
    }

    public void setZ_ID(int z_ID) {
        this.z_ID = z_ID;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString(){
        return "pID: "+p_ID+"\n"
                +"zID: "+z_ID+"\n"
                +"Progress: "+progress+"\n"
                +"WeekNo: "+weekNo+"\n"
                +"Notes: "+notes+"\n";
    }
}
