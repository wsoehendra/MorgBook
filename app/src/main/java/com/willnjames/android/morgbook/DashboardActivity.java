package com.willnjames.android.morgbook;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.willnjames.android.morgbook.Model.Meeting;
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

    private int attendanceProgressValue;
    private int weekProgressValue;

    private ListView meetingList;

    private TextView dateText;

    private ArrayList<Meeting> meetingItems;


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

        attendanceTextCount = (TextView) findViewById(R.id.attendanceText);
        weekTextCount = (TextView) findViewById(R.id.weekText);

        dateText = (TextView) findViewById(R.id.dateText);

        meetingList = (ListView) findViewById(R.id.meetingsList);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        SimpleDateFormat sdm = new SimpleDateFormat("MMM");
        Date dd = new Date();
        String month = sdm.format(dd);

        String sDate = dayOfTheWeek +", "+c.get(Calendar.DAY_OF_MONTH)+" "+month+" "+c.get(Calendar.YEAR);

        dateText.setText(sDate);

        attendanceProgressBar.setMax(10000);
        weekProgressBar.setMax(1200);

        attendanceTextCount.setText("0");
        weekTextCount.setText("0");

        for(int q=0; q < 10; q++){

            int id = q;
            String time = "0"+q+":00 PM";
            String location = "Room "+q;

            Meeting meeting = new Meeting(id, time, location, "Morgan");
            meetingItems.add(meeting);
        }

        ArrayAdapter<Meeting> adapter = new ArrayAdapter<Meeting>(this, android.R.layout.simple_list_item_1, meetingItems);
        meetingList.setAdapter(adapter);

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
                attendanceProgressValue = attendanceProgressBar.getProgress();
                weekProgressValue = weekProgressBar.getProgress();

                Random a = new Random();
                int b = a.nextInt(101);
                int c = b*100;

                attendanceAnimation = new ProgressBarAnimation(attendanceProgressBar, attendanceProgressValue, c);
                attendanceAnimation.setDuration(1000);
                attendanceProgressBar.startAnimation(attendanceAnimation);

                attendanceProgressValue = attendanceProgressValue / 100;

                startAttendanceAnimation(attendanceProgressValue, b);

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
}
