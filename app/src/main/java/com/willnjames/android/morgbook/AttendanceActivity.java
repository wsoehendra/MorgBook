package com.willnjames.android.morgbook;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by jamesprijatna on 7/10/16.
 */
public class AttendanceActivity extends Activity {

    private TextView[] txt = new TextView[13];
    private Button[] btn = new Button[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.attendance_activity);

        initialise();
    }

    public void initialise() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_main);
        TableRow weekLabel = new TableRow(this);

        TextView blankLabel = new TextView(this);
        blankLabel.setText("            ");
        weekLabel.addView(blankLabel);


        for(int i = 1; i < txt.length; i++){
            txt[i] = new TextView(this);
            txt[i].setText("W"+i);
            txt[i].setTextColor(Color.WHITE);
            txt[i].setTextSize(20);
            txt[i].setGravity(Gravity.RIGHT);
            txt[i].setPadding(9,2,9,2);
            weekLabel.addView(txt[i]);
        }

        tableLayout.addView(weekLabel);

        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 12; i++) {
            TableRow tableRow = new TableRow(this);

            TextView nameText = new TextView(this);
            nameText.setText("Name #"+i);
            nameText.setGravity(Gravity.LEFT);
            tableRow.addView(nameText);

            for(int j = 0; j < btn.length; j++){
                btn[j] = new Button(this);
                btn[j].setId(View.generateViewId());
                btn[j].setGravity(Gravity.CENTER);
                btn[j].setLayoutParams(new TableRow.LayoutParams(110, 110));
                btn[j].setOnClickListener(doSomething(btn[j]));
                tableRow.addView(btn[j]);
            }

            tableLayout.addView(tableRow);
            tableRowParams.setMargins(30, 5, 10, 5);
            tableRow.setLayoutParams(tableRowParams);

        }

    }

    View.OnClickListener doSomething(final Button button)  {
        return new View.OnClickListener() {
            int counter = 1;
            public void onClick(View v) {
                if(counter==1) {
                    v.getBackground().setColorFilter(Color.parseColor("#FF00FF"), PorterDuff.Mode.MULTIPLY);
                    counter++;
                }
                else if(counter==2){
                    v.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.MULTIPLY);
                    counter++;
                }
                else if(counter==3){
                    v.getBackground().setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.MULTIPLY);
                    counter++;
                }
                else if(counter==4){
                    v.getBackground().clearColorFilter();
                    counter=1;
                }
            }
        };
    }

}