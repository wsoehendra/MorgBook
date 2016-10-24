package com.willnjames.android.morgbook;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.willnjames.android.morgbook.Database.DatabaseAccess;
import com.willnjames.android.morgbook.Model.Attendance;
import com.willnjames.android.morgbook.Model.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jamesprijatna on 7/10/16.
 */
public class AttendanceActivity extends Activity {

    private TextView[] txt = new TextView[13];
    private Button[] btn = new Button[12];
    DatabaseAccess dbAccess;
    ArrayList<Button> buttons = new ArrayList<Button>();

    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        initialise();
        setDateText();
        populateAttendances();
    }

    //Create and setup the Attendance table
    public void initialise() {
        dbAccess = DatabaseAccess.getInstance(this);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_main);
        TableRow weekLabel = new TableRow(this);

        TextView blankLabel = new TextView(this);
        blankLabel.setText("            ");
        weekLabel.addView(blankLabel);


        for(int i = 1; i < txt.length; i++){
            txt[i] = new TextView(this);
            txt[i].setText("W"+i);
            txt[i].setTextColor(Color.BLACK);
            txt[i].setTextSize(18);
            txt[i].setGravity(Gravity.CENTER);
            txt[i].setPadding(45,2,9,2);
            weekLabel.addView(txt[i]);
        }

        tableLayout.addView(weekLabel);

        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);

        dbAccess.open();
        ArrayList<Person> studentsList = dbAccess.getStudents();
        if(studentsList == null){Log.d("Query", "List is null");}
        dbAccess.close();

        for (int i = 0; i < studentsList.size(); i++) {
            TableRow tableRow = new TableRow(this);
            int studentID = studentsList.get(i).getZ_ID();
            TextView nameText = new TextView(this);
            String fullName = studentsList.get(i).getLName().toUpperCase()+", "+studentsList.get(i).getFName();
            nameText.setText(fullName);
            nameText.setTextSize(17);
            nameText.setGravity(Gravity.LEFT);
            nameText.setPadding(0,0,20,0);
            tableRow.addView(nameText);

            for(int j = 0; j < btn.length; j++){
                int weekNo = j+1;
                btn[j] = new Button(this);
                btn[j].setGravity(Gravity.CENTER);
                btn[j].setLayoutParams(new TableRow.LayoutParams(115, 115));
                btn[j].setOnClickListener(doSomething(btn[j], studentID, weekNo));
                btn[j].setId(Integer.valueOf(String.valueOf(weekNo)+String.valueOf(studentID)));
                buttons.add(btn[j]);
                tableRow.addView(btn[j]);
            }

            tableLayout.addView(tableRow);
            tableRowParams.setMargins(30, 5, 10, 5);
            tableRow.setLayoutParams(tableRowParams);
        }

    }

    //Populate the Attendance table with existing records
    //some method here
    private void populateAttendances(){
        dbAccess.open();
        ArrayList<Attendance> attendances = dbAccess.getAttendance();
        dbAccess.close();

        for(int i=0;i<attendances.size();i++){
            int id = Integer.valueOf(String.valueOf(attendances.get(i).getWeekNo())+String.valueOf(attendances.get(i).getZ_ID()));
            for(Button b: buttons){
                if(b.getId() == id){
                    String status = attendances.get(i).getStatus();
                    switch (status){
                        case "Present": b.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                            b.setTag("Present");
                            break;
                        case "Absent": b.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            b.setTag("Absent");
                            break;
                        case "Explained Absence": b.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                            b.setTag("Explained Absence");
                            break;
                        case "NODATA": b.getBackground().clearColorFilter();
                            b.setTag("NODATA");
                            break;
                    }
                }
            }
        }

    }


    View.OnClickListener doSomething(final Button button, final int studentID, final int weekNo)  {
        return new View.OnClickListener() {
            int counter = 1;
            Attendance attendance;
            public void onClick(View v) {
                if(v.getTag() == "Present") {
                    counter = 2;
                } else if (v.getTag() == "Absent"){
                    counter = 3;
                } else if (v.getTag() == "Explained Absence"){
                    counter = 4;
                }

                Log.d("Attendance|Click", "Student: "+studentID+" Week: "+weekNo+"\n");
                if(counter==1) {    //Student is present
                    v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    counter++;
                    Attendance aPresent = new Attendance(studentID,weekNo,"Present");
                    dbAccess.open();
                    dbAccess.addAttendance(aPresent);
                    dbAccess.close();
                    populateAttendances();
                }
                else if(counter==2){    //Student is absent
                    v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    counter++;
                    dbAccess.open();
                    Attendance aAbsent = new Attendance(studentID,weekNo,"Absent");
                    dbAccess.addAttendance(aAbsent);
                    populateAttendances();
                }
                else if(counter==3){    //Student is absent with explanation
                    v.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    counter++;
                    dbAccess.open();
                    Attendance aAbsentExp = new Attendance(studentID,weekNo,"Explained Absence");
                    dbAccess.addAttendance(aAbsentExp);
                    populateAttendances();
                }
                else if(counter==4){
                    v.getBackground().clearColorFilter();
                    counter=1;
                    dbAccess.open();
                    Attendance aNoData = new Attendance(studentID,weekNo,"NODATA");
                    dbAccess.addAttendance(aNoData);
                    populateAttendances();

                }
            }
        };
    }

    private void setDateText(){
        dateText = (TextView) findViewById(R.id.dateText);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        SimpleDateFormat sdm = new SimpleDateFormat("MMM");
        Date dd = new Date();
        String month = sdm.format(dd);

        String sDate = dayOfTheWeek +", "+c.get(Calendar.DAY_OF_MONTH)+" "+month+" "+c.get(Calendar.YEAR);

        dateText.setText(sDate);
    }

}