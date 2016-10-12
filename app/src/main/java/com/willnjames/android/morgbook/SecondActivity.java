package com.willnjames.android.morgbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;


/**
 * Created by jamesprijatna on 7/10/16.
 */
public class SecondActivity extends Activity {

    private TextClock textClock;
    private Button popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity2);

        textClock = (TextClock) findViewById(R.id.textClock2);
        popup = (Button) findViewById(R.id.popup);

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(SecondActivity.this, PopupMeetingActivity.class);
                SecondActivity.this.startActivity(myIntent);


            }
        });

    }
}