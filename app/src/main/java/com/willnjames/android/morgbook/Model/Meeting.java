package com.willnjames.android.morgbook.Model;

/**
 * Created by jamesprijatna on 18/10/16.
 */

public class Meeting {

    int m_ID;
    int studentZID;
    String room;
    String date;
    String startTime;
    String endTime;
    String topic;
    int staffZID;

    public Meeting(int m_ID, int studentZID, int staffZID, String date, String startTime, String endTime, String topic, String room) {
        this.m_ID = m_ID;
        this.studentZID = studentZID;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
        this.staffZID = staffZID;
    }

    public Meeting(int studentZID, int staffZID, String date, String startTime, String endTime, String topic, String room) {
        this.studentZID = studentZID;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
        this.staffZID = staffZID;
    }

    public int getM_ID() {
        return m_ID;
    }

    public void setM_ID(int m_ID) {
        this.m_ID = m_ID;
    }

    public int getStudentZID() {
        return studentZID;
    }

    public void setStudentZID(int studentZID) {
        this.studentZID = studentZID;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStaffZID() {
        return staffZID;
    }

    public void setStaffZID(int staffZID) {
        this.staffZID = staffZID;
    }

    @Override
    public String toString() {
        return
                "Student: " + studentZID + "\n"
                        + "Staff: " + staffZID + "\n"
                        + "Date: " + date + "\n"
                        + "Start: " + startTime + "\n"
                        + "End: " + endTime + "\n"
                        + "Topic: " + topic+"\n"
                        + "Room: " + room;
    }
}
