package com.willnjames.android.morgbook;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by jamesprijatna on 5/10/16.
 */
public class DashboardActivity extends Activity {

    private ProgressBar circularProgress;
    private Button reset;
    private Button random;
    private Button full;
    private ProgressBarAnimation anim;
    private ProgressBarAnimation anim2;

    private TextView weekText;
    private TextView textView;
    private SeekBar seekBar;

    private int circleProgressValue;
    private int seekBarProgressValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        circleProgressValue = 0;
        seekBarProgressValue = 0;

        circularProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        reset = (Button) findViewById(R.id.button);
        random = (Button) findViewById(R.id.button2);
        full = (Button) findViewById(R.id.button3);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        textView = (TextView) findViewById(R.id.textView);
        weekText = (TextView) findViewById(R.id.textView4);

        circularProgress.setMax(10000);
        seekBar.setEnabled(false);
        seekBar.setMax(1200);

        textView.setText("0");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgressValue = circularProgress.getProgress();
                seekBarProgressValue = seekBar.getProgress();

                anim = new ProgressBarAnimation(circularProgress, circleProgressValue, 0);
                anim.setDuration(1000);
                circularProgress.startAnimation(anim);

                circleProgressValue = circleProgressValue / 100;

                startCountAnimation(circleProgressValue, 0);
                seekBar.setProgress(0);

                weekText.setText("This is Week #");

                anim2 = new ProgressBarAnimation(seekBar, seekBarProgressValue, 0);
                anim2.setDuration(1000);
                seekBar.startAnimation(anim2);


            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgressValue = circularProgress.getProgress();
                seekBarProgressValue = seekBar.getProgress();

                Random a = new Random();
                int b = a.nextInt(101);
                int c = b*100;

                anim = new ProgressBarAnimation(circularProgress, circleProgressValue, c);
                anim.setDuration(1000);
                circularProgress.startAnimation(anim);

                circleProgressValue = circleProgressValue / 100;

                startCountAnimation(circleProgressValue, b);

                Random r = new Random();
                int j = r.nextInt(13);
                int m = j*100;

                seekBar.setProgress(j);
                weekText.setText("This is Week "+j);

                anim2 = new ProgressBarAnimation(seekBar, seekBarProgressValue, m);
                anim2.setDuration(1000);
                seekBar.startAnimation(anim2);

            }
        });

        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgressValue = circularProgress.getProgress();
                seekBarProgressValue = seekBar.getProgress();

                anim = new ProgressBarAnimation(circularProgress, circleProgressValue, 10000);
                anim.setDuration(1000);
                circularProgress.startAnimation(anim);

                circleProgressValue = circleProgressValue / 100;

                startCountAnimation(circleProgressValue, 100);
                seekBar.setProgress(0);

                weekText.setText("This is Week 12");

                anim2 = new ProgressBarAnimation(seekBar, seekBarProgressValue, 1200);
                anim2.setDuration(1000);
                seekBar.startAnimation(anim2);


            }
        });

        init();
    }

    private void startCountAnimation(int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);

        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue());

            }
        });
        animator.start();
    }


    // Code taken from StackOverFlow user Aby Mathew @: http://stackoverflow.com/a/22682248
    public void init() {
        TableLayout layout = (TableLayout) findViewById(R.id.table_main);
        TableRow tableRow = new TableRow(this);
        TextView idText = new TextView(this);
        idText.setText("ID");
        idText.setTextColor(Color.WHITE);
        tableRow.addView(idText);
        TextView nameText = new TextView(this);
        nameText.setText("Name");
        nameText.setTextColor(Color.WHITE);
        tableRow.addView(nameText);
        TextView locationText = new TextView(this);
        locationText.setText("Location");
        locationText.setTextColor(Color.WHITE);
        tableRow.addView(locationText);
        TextView timeText = new TextView(this);
        timeText.setText("Time");
        timeText.setTextColor(Color.WHITE);
        tableRow.addView(timeText);
        layout.addView(tableRow);

        idText.setPadding(9,2,9,2);
        nameText.setPadding(9,2,9,2);
        locationText.setPadding(9,2,9,2);
        timeText.setPadding(9,2,9,2);

        idText.setTextSize(20);
        nameText.setTextSize(20);
        locationText.setTextSize(20);
        timeText.setTextSize(20);

        idText.setBackground(getDrawable(R.drawable.bordertop));
        nameText.setBackground(getDrawable(R.drawable.bordertop));
        locationText.setBackground(getDrawable(R.drawable.bordertop));
        timeText.setBackground(getDrawable(R.drawable.bordertop));


        for (int i = 0; i < 6; i++) {
            TableRow entryRow = new TableRow(this);
            TextView idEntry = new TextView(this);
            idEntry.setText("" + i);
            idEntry.setTextColor(Color.WHITE);
            idEntry.setGravity(Gravity.CENTER);
            entryRow.addView(idEntry);
            TextView nameEntry = new TextView(this);
            nameEntry.setText("Name " + i);
            nameEntry.setTextColor(Color.WHITE);
            nameEntry.setGravity(Gravity.CENTER);
            entryRow.addView(nameEntry);
            TextView locationEntry = new TextView(this);
            locationEntry.setText("Rm." + i);
            locationEntry.setTextColor(Color.WHITE);
            locationEntry.setGravity(Gravity.CENTER);
            entryRow.addView(locationEntry);
            TextView timeEntry = new TextView(this);
            timeEntry.setText("" + i * 15 / 32 * 10+":00");
            timeEntry.setTextColor(Color.WHITE);
            timeEntry.setGravity(Gravity.CENTER);
            entryRow.addView(timeEntry);
            layout.addView(entryRow);


            idEntry.setPadding(9,2,9,2);
            nameEntry.setPadding(9,2,9,2);
            locationEntry.setPadding(9,2,9,2);
            timeEntry.setPadding(9,2,9,2);

            idEntry.setTextSize(15);
            nameEntry.setTextSize(15);
            locationEntry.setTextSize(15);
            timeEntry.setTextSize(15);

            idEntry.setBackground(getDrawable(R.drawable.border));
            nameEntry.setBackground(getDrawable(R.drawable.border));
            locationEntry.setBackground(getDrawable(R.drawable.border));
            timeEntry.setBackground(getDrawable(R.drawable.border));

        }
    }
}
