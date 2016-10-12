package com.willnjames.android.morgbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

/**
 * Created by jamesprijatna on 7/10/16.
 */
public class TabsActivity extends android.app.TabActivity implements View.OnClickListener {

    private ImageButton tab0;
    private ImageButton tab1;
    private ImageButton tab2;
    private ImageButton tab3;

    private ImageButton[] btn = new ImageButton[4];
    private ImageButton btn_unfocus;
    private int[] btn_id = {R.id.tab0, R.id.tab1, R.id.tab2, R.id.tab3};

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.tabs );


        tab0 = (ImageButton) findViewById(R.id.tab0);
        tab1 = (ImageButton) findViewById(R.id.tab1);
        tab2 = (ImageButton) findViewById(R.id.tab2);
        tab3 = (ImageButton) findViewById(R.id.tab3);

        tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("first").setIndicator("First").setContent(new Intent(this  ,DashboardActivity.class )));
        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("Second").setContent(new Intent(this  ,AttendanceActivity.class )));
        tabHost.addTab(tabHost.newTabSpec("third").setIndicator("Third").setContent(new Intent(this  ,BasicActivity.class )));
        tabHost.addTab(tabHost.newTabSpec("fourth").setIndicator("Fourth").setContent(new Intent(this  ,SecondActivity.class )));
        tabHost.setCurrentTab(0);

        for(int i = 0; i < btn.length; i++){
            btn[i] = (ImageButton) findViewById(btn_id[i]);
            //btn[i].setBackgroundColor(Color.parseColor("#3F51B5"));
            btn[i].setOnClickListener(this);
        }

        btn_unfocus=btn[0];
        initFocus(btn_unfocus);

    }


    /* Code taken from StackOverFlow user Huo Chhunleng @: http://stackoverflow.com/a/36172295
     * and also from user blessenm @: http://stackoverflow.com/a/7268225
     */

    @Override
    public void onClick(View v) {

        tab0.setSelected(false);
        tab1.setSelected(false);
        tab2.setSelected(false);
        tab3.setSelected(false);

        switch (v.getId()){
            case R.id.tab0 :
                setFocus(btn_unfocus, btn[0]);
                btn_unfocus = btn[0];
                tabHost.setCurrentTab(0);
                tab0.setSelected(true);
                break;

            case R.id.tab1 :
                setFocus(btn_unfocus, btn[1]);
                btn_unfocus = btn[1];
                tabHost.setCurrentTab(1);
                tab1.setSelected(true);
                break;

            case R.id.tab2 :
                setFocus(btn_unfocus, btn[2]);
                btn_unfocus = btn[2];
                tabHost.setCurrentTab(2);
                tab2.setSelected(true);
                break;

            case R.id.tab3 :
                setFocus(btn_unfocus, btn[3]);
                btn_unfocus = btn[3];
                tabHost.setCurrentTab(3);
                tab3.setSelected(true);
                break;
        }
    }

    private void setFocus(ImageButton btn_unfocus, ImageButton btn_focus){
        btn_unfocus.setBackgroundColor(Color.parseColor("#FFC107"));
        btn_focus.setBackgroundColor(Color.parseColor("#e6ac00"));
    }

    private void initFocus(ImageButton btn_unfocus){
        btn_unfocus.setBackgroundColor(Color.parseColor("#e6ac00"));
    }

}