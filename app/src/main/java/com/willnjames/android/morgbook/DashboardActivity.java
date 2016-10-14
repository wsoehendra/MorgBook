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

import com.willnjames.android.morgbook.Model.ProgressBarAnimation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.willnjames.android.morgbook.R.id.dateText;

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

    private TextView dateText;


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
}
