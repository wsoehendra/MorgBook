package com.willnjames.android.morgbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by jamesprijatna on 7/10/16.
 */
public class ProgressActivity extends Activity {

    private TextClock textClock;
    private Button popup;

    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_activity);

        textClock = (TextClock) findViewById(R.id.textClock2);
        popup = (Button) findViewById(R.id.popup);

        setDateText();

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ProgressActivity.this, PopupMeetingActivity.class);
                ProgressActivity.this.startActivity(myIntent);


            }
        });

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