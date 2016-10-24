package com.willnjames.android.morgbook;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.willnjames.android.morgbook.Database.DatabaseAccess;
import com.willnjames.android.morgbook.Model.Attendance;
import com.willnjames.android.morgbook.Model.Meeting;
import com.willnjames.android.morgbook.Model.Progress;
import com.willnjames.android.morgbook.Model.ProgressBarAnimation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by jamesprijatna on 5/10/16.
 */
public class DashboardActivity extends Activity {

    private ProgressBar attendanceProgressBar;
    private ProgressBar weekProgressBar;
    private Button reset;
    private Button random;
    private Button full;
    private ProgressBarAnimation attendanceAnimation;
    private ProgressBarAnimation weekAnimation;

    private TextView attendanceTextCount;
    private TextView weekTextCount;
    private TextView attendingText;
    private TextView absentText;

    private int attendanceProgressValue;
    private int weekProgressValue;

    private ListView meetingList;

    private TextView dateText;

    private ArrayList<Meeting> meetingItems;

    GraphView graph;
    BarGraphSeries<DataPoint> series;

    DatabaseAccess dbAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        meetingItems = new ArrayList<>();

        attendanceProgressValue = 0;
        weekProgressValue = 0;

        attendanceProgressBar = (ProgressBar) findViewById(R.id.circularProgressbar);
        weekProgressBar = (ProgressBar) findViewById(R.id.weeklyProgress);
        reset = (Button) findViewById(R.id.resetButton);
        random = (Button) findViewById(R.id.button2);
        full = (Button) findViewById(R.id.button3);
        attendingText = (TextView) findViewById(R.id.attendingText);
        absentText = (TextView) findViewById(R.id.absentText);

        attendanceTextCount = (TextView) findViewById(R.id.attendanceText);
        weekTextCount = (TextView) findViewById(R.id.weekText);

        dateText = (TextView) findViewById(R.id.dateText);

        meetingList = (ListView) findViewById(R.id.meetingsList);

        setDateText();

        attendanceProgressBar.setMax(10000);
        weekProgressBar.setMax(1200);

        attendanceTextCount.setText("0");
        weekTextCount.setText("0");


        ArrayAdapter<Meeting> adapter = new ArrayAdapter<Meeting>(this, android.R.layout.simple_list_item_1, meetingItems);
        meetingList.setAdapter(adapter);

        graph = (GraphView) findViewById(R.id.progressGraph);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceProgressValue = attendanceProgressBar.getProgress();
                weekProgressValue = weekProgressBar.getProgress();

                attendanceAnimation = new ProgressBarAnimation(attendanceProgressBar, attendanceProgressValue, 0);
                attendanceAnimation.setDuration(1000);
                attendanceProgressBar.startAnimation(attendanceAnimation);

                weekAnimation = new ProgressBarAnimation(weekProgressBar, weekProgressValue, 0);
                weekAnimation.setDuration(1000);
                weekProgressBar.startAnimation(weekAnimation);

                attendanceProgressValue = attendanceProgressValue / 100;
                weekProgressValue = weekProgressValue / 100;

                startAttendanceAnimation(attendanceProgressValue, 0);
                startWeekAnimation(weekProgressValue, 0);


            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAttendanceProgress();

                Random r = new Random();
                int j = r.nextInt(13);
                int m = j*100;

                weekAnimation = new ProgressBarAnimation(weekProgressBar, weekProgressValue, m);
                weekAnimation.setDuration(1000);
                weekProgressBar.startAnimation(weekAnimation);

                weekProgressValue = weekProgressValue / 100;

                startWeekAnimation(weekProgressValue, j);

            }
        });

        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceProgressValue = attendanceProgressBar.getProgress();
                weekProgressValue = weekProgressBar.getProgress();

                attendanceAnimation = new ProgressBarAnimation(attendanceProgressBar, attendanceProgressValue, 10000);
                attendanceAnimation.setDuration(1000);
                attendanceProgressBar.startAnimation(attendanceAnimation);

                attendanceProgressValue = attendanceProgressValue / 100;

                startAttendanceAnimation(attendanceProgressValue, 100);

                weekAnimation = new ProgressBarAnimation(weekProgressBar, weekProgressValue, 1200);
                weekAnimation.setDuration(1000);
                weekProgressBar.startAnimation(weekAnimation);

                weekProgressValue = weekProgressValue / 100;

                startWeekAnimation(weekProgressValue, 12);
            }
        });

    }

    private void refreshAttendanceProgress(){
        dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        ArrayList<Attendance> attend = dbAccess.getAttendance();
        final double allRecords = attend.size();
        final double present = dbAccess.getPresentCount();
        final double absent = dbAccess.getAbsentCount();
        Log.d("TEST7", "allRecords= "+allRecords+" present= "+present);
        attendanceProgressValue = attendanceProgressBar.getProgress();
        weekProgressValue = weekProgressBar.getProgress();

        double a = ((present/allRecords)*100);
        int b = (int) a;
        int c = b*100;

        attendingText.setText(String.valueOf((int) (present)));
        absentText.setText(String.valueOf((int) (absent)));

        attendanceAnimation = new ProgressBarAnimation(attendanceProgressBar, attendanceProgressValue, c);
        attendanceAnimation.setDuration(1000);
        attendanceProgressBar.startAnimation(attendanceAnimation);

        attendanceProgressValue = attendanceProgressValue / 100;

        startAttendanceAnimation(attendanceProgressValue, b);
    }

    private void setDateText() {
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

    private void fillAttendanceProgress(){
        dbAccess.open();
        ArrayList<Attendance> a = dbAccess.getAttendance();
        int allRecords = a.size();
        int absentees = dbAccess.getAbsentCount();
    }

    private void startAttendanceAnimation(int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);

        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                attendanceTextCount.setText("" + (int) animation.getAnimatedValue());

            }
        });
        animator.start();
    }

    private void startWeekAnimation(int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);

        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                weekTextCount.setText("" + (int) animation.getAnimatedValue());

            }
        });
        animator.start();
    }

    private void drawChart(){
        Log.d("TEST7", "drawGraph Started");
        graph.removeAllSeries();

        dbAccess = DatabaseAccess.getInstance(this);
        dbAccess.open();
        ArrayList<Progress> allProgress = dbAccess.getAllProgress();
        dbAccess.close();

        int badCounter=0;
        int averageCounter=0;
        int goodCounter=0;

        for(Progress p: allProgress){
            switch (p.getProgress()){
                case "Bad": badCounter++;
                    break;
                case "Average": averageCounter++;
                    break;
                case "Good": goodCounter++;
                    break;
            }
        }

        Log.d("Counters", "BAD:"+badCounter+"AV: "+averageCounter+"GOOD: "+goodCounter);

        DataPoint[] dp = new DataPoint[4];
        dp[0] = new DataPoint(0, 0);
        dp[1] = new DataPoint(2, badCounter);
        dp[2] = new DataPoint(4, averageCounter);
        dp[3] = new DataPoint(6, goodCounter);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"","Bad", "Average", "Good", ""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series = new BarGraphSeries<>(dp);
        series.setSpacing(5);
        graph.addSeries(series);
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshAttendanceProgress();

        drawChart();
    }
}
