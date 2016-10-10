package com.willnjames.android.morgbook.Model;

/**
 * Created by wsoeh on 10/10/2016.
 */

public class Person {

    int z_ID;
    String fName;
    String lName;
    String role;

    public Person(){}

    public Person(int z_ID, String FName, String LName, String role) {
        this.z_ID = z_ID;
        this.fName = FName;
        this.lName = LName;
        this.role = role;
    }

    public int getZ_ID() {
        return z_ID;
    }

    public void setZ_ID(int z_ID) {
        this.z_ID = z_ID;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String FName) {
        this.fName = FName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String LName) {
        this.lName = LName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return "zID: "+z_ID+"\n"
                +"FName: "+fName+"\n"
                +"LName: "+lName+"\n"
                +"Role: "+role;
    }
}
