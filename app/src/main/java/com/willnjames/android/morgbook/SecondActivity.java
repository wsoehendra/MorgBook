package com.willnjames.android.morgbook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextClock;


/**
 * Created by jamesprijatna on 7/10/16.
 */
public class SecondActivity extends Activity {

    private TextClock textClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity2);

        textClock = (TextClock) findViewById(R.id.textClock2);

    }
}