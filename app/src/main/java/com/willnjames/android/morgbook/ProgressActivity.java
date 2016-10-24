package com.willnjames.android.morgbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.willnjames.android.morgbook.Database.DatabaseAccess;
import com.willnjames.android.morgbook.Model.Person;
import com.willnjames.android.morgbook.Model.Progress;
import com.willnjames.android.morgbook.Model.RVAdapter;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by jamesprijatna on 7/10/16.
 */
public class ProgressActivity extends Activity {

    private TextView dateText;

    DatabaseAccess dbAccess;

    GraphView graph;
    LineGraphSeries<DataPoint> series;
    Person selection;
    ArrayList<Progress> pList;

    TextView detailHeading;
    Spinner weekSpinner;
    Spinner progressSpinner;
    EditText notesEditText;

    String inStudentProgress;
    String inStudentNotes;
    int inWeek;
    Button submitButton;
    TextView errorText;

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

        detailHeading = (TextView) findViewById(R.id.detailHeadingTextView);
        errorText = (TextView) findViewById(R.id.errorTextView);
        graph = (GraphView) findViewById(R.id.progressGraph);

        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setVerticalLabels(new String[] {"Bad", "Avg.", "Good"});
        staticLabelsFormatter.setHorizontalLabels(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        initializeWeekSpinner();
        initializeProgressSpinner();

        notesEditText = (EditText) findViewById(R.id.notesEditText);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selection == null){
                    return;
                }
                inStudentNotes = String.valueOf(notesEditText.getText());
                if(validateSubmit() == false){
                    Log.d("TEST6", "Can't submit");
                } else {
                    Progress inProg = new Progress (selection.getZ_ID(), inStudentProgress, inWeek, inStudentNotes);
                    dbAccess.open();
                    dbAccess.addProgress(inProg);
                    dbAccess.close();
                    initializeWeekSpinner();
                    setProgressSelection(selection.getZ_ID());
                }
            }
        });
    }

    private void initializeProgressSpinner() {
        progressSpinner = (Spinner) findViewById(R.id.progressSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Bad");
        list.add("Average");
        list.add("Good");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        progressSpinner.setAdapter(dataAdapter);
        progressSpinner.setSelection(1);
        progressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: Log.d("TEST6", "BAD Selected!");
                        inStudentProgress = "Bad";
                        break;
                    case 1: Log.d("TEST6", "AVERAGE Selected!");
                        inStudentProgress = "Average";
                        break;
                    case 2: Log.d("TEST6", "GOOD Selected!");
                        inStudentProgress = "Good";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validateSubmit(){
        if(pList == null){
            return true;
        }
        boolean validate = true;
        if(selection == null){
            Log.d("TEST6", "Invalidated: no Selection.");
            errorText.setText("Please select a Student");
            validate = false;
        } else if (inWeek == 0) {
            Log.d("TEST6", "Invalidated: inWeek == 0");
            errorText.setText("Please Select a Week");
            validate = false;
        } else if (inWeek > pList.size()+1){
            Log.d("TEST6", "Invalidated: inWeek is 2 after the last entry");
            errorText.setText("Entries can only be added for the next consecutive week.");
            validate = false;
        }
        return validate;
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

    private void initializeWeekSpinner(){
        weekSpinner = (Spinner) findViewById(R.id.weekSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add("Week 1");
        list.add("Week 2");
        list.add("Week 3");
        list.add("Week 4");
        list.add("Week 5");
        list.add("Week 6");
        list.add("Week 7");
        list.add("Week 8");
        list.add("Week 9");
        list.add("Week 10");
        list.add("Week 11");
        list.add("Week 12");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(dataAdapter);
        weekSpinner.setSelection(0);
        weekSpinnerListener();
    }

    public void weekSpinnerListener(){
        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(selection == null){
                    //Error text
                    return;
                } else if(i==0){
                    return;
                }
                switch (i){
                    case 0: Log.d("TEST6", "Spinner #0 Selected!");
                        break;
                    case 1: Log.d("TEST6", "Spinner #1 Selected!");
                        inWeek = 1;
                        populateFields();
                        break;
                    case 2: Log.d("TEST6", "Spinner #2 Selected!");
                        inWeek = 2;
                        populateFields();
                        break;
                    case 3: Log.d("TEST6", "Spinner #3 Selected!");
                        inWeek = 3;
                        populateFields();
                        break;
                    case 4: Log.d("TEST6", "Spinner #4 Selected!");
                        inWeek = 4;
                        populateFields();
                        break;
                    case 5: Log.d("TEST6", "Spinner #5 Selected!");
                        inWeek = 5;
                        populateFields();
                        break;
                    case 6: Log.d("TEST6", "Spinner #6 Selected!");
                        inWeek = 6;
                        populateFields();
                        break;
                    case 7: Log.d("TEST6", "Spinner #7 Selected!");
                        inWeek = 7;
                        populateFields();
                        break;
                    case 8: Log.d("TEST6", "Spinner #8 Selected!");
                        inWeek = 8;
                        populateFields();
                        break;
                    case 9: Log.d("TEST6", "Spinner #9 Selected!");
                        inWeek = 9;
                        populateFields();
                        break;
                    case 10: Log.d("TEST6", "Spinner #10 Selected!");
                        inWeek = 10;
                        populateFields();
                        break;
                    case 11: Log.d("TEST6", "Spinner #11 Selected!");
                        inWeek = 11;
                        populateFields();
                        break;
                    case 12: Log.d("TEST6", "Spinner #12 Selected!");
                        inWeek = 12;
                        populateFields();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void populateFields(){
        if(pList == null){
            return;
        }
        if(selection == null){
            return;
        } else if(weekSpinner.getSelectedItem() == null){
            return;
        } else if(inWeek > pList.size()){
            return;
        } else {
            Progress p = pList.get(inWeek - 1);
            switch (p.getProgress()) {
                case "Bad":
                    progressSpinner.setSelection(0);
                    break;
                case "Average":
                    progressSpinner.setSelection(1);
                    break;
                case "Good":
                    progressSpinner.setSelection(2);
                    break;
            }
            notesEditText.setText(p.getNotes());
        }
    }

    private void clearGraph(){
        graph.removeAllSeries();
    }


    public void setProgressSelection(int zID){
        String fullName;
        dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        pList = dbAccess.getStudentProgress(zID);
        selection = dbAccess.getPerson(zID);
        fullName = selection.getLName().toUpperCase()+", "+selection.getFName();
        if(pList == null){
            graph.removeAllSeries();
            detailHeading.setText("No Data for "+fullName);
            weekSpinner.setSelection(0);
            progressSpinner.setSelection(1);
            notesEditText.setText("");
            return;
        }
        dbAccess.close();

        DataPoint[] dp = new DataPoint[pList.size()];

        for(int i=0;i<pList.size();i++){
            double y = -1;

            switch(pList.get(i).getProgress()){
                case "Bad": y=0;
                    break;
                case "Average": y=5;
                    break;
                case "Good": y=10;
                    break;
                default: break;
            }

            double x;
            x = pList.get(i).getWeekNo();
            DataPoint d = new DataPoint(x,y);
            dp[i] = d;
        }

        series = new LineGraphSeries<DataPoint>(dp);
        series.setThickness(10);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(13);

        detailHeading.setText("Weekly Progress for "+fullName);
        clearGraph();
        graph.addSeries(series);

    }

}
