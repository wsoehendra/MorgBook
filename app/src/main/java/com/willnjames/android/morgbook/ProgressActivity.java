package com.willnjames.android.morgbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.willnjames.android.morgbook.Database.DatabaseAccess;
import com.willnjames.android.morgbook.Model.Person;
import com.willnjames.android.morgbook.Model.RVAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by jamesprijatna on 7/10/16.
 */
public class ProgressActivity extends Activity {

    private TextClock textClock;

    private TextView dateText;

    DatabaseAccess dbAccess;

    Person currentSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        setDateText();

        dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        ArrayList<Person> studentsList = new ArrayList<Person>();
        studentsList = dbAccess.getStudents();
        dbAccess.close();

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(studentsList);
        rv.setAdapter(adapter);
    }


    private void setDateText() {
        dateText = (TextView) findViewById(R.id.dateText);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        SimpleDateFormat sdm = new SimpleDateFormat("MMM");
        Date dd = new Date();
        String month = sdm.format(dd);
        String sDate = dayOfTheWeek + ", " + c.get(Calendar.DAY_OF_MONTH) + " " + month + " " + c.get(Calendar.YEAR);

        dateText.setText(sDate);
    }

    public void setCurrentSelection(int inputZID) {
        dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        currentSelection = dbAccess.getStudent(inputZID);
        dbAccess.close();

        LineChart chart = (LineChart) findViewById(R.id.chart);
    }
}
