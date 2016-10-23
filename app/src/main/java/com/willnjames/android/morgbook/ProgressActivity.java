package com.willnjames.android.morgbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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
    RelativeLayout graphLayout;
    private TextView testTextV;
    LineGraphSeries<DataPoint> currentProgress;

    TextView detailHeading;

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

    private void setChart(LineGraphSeries<DataPoint> series){
        testTextV.setText("Text Set: "+series.toString());
        //graph.addSeries(series);
    }

    public void setProgressSelection(int zID){
        Person selection;
        String fullName;

        dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        ArrayList<Progress> pList = dbAccess.getStudentProgress(zID);
        selection = dbAccess.getPerson(zID);
        dbAccess.close();

        fullName = selection.getLName().toUpperCase()+", "+selection.getLName();
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
            }

            double x;
            x = pList.get(i).getWeekNo();
            dp[i] = new DataPoint(x,y);

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dp);
        series.setThickness(10);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(13);

        detailHeading.setText("Weekly Progress for "+fullName);

        graph.addSeries(series);
    }

    /*private void setChart(LineData lineData){
        LineChart progressChart = (LineChart) findViewById(R.id.progressChart);
        progressChart.setData(lineData);
        progressChart.invalidate();
    }*/
}
