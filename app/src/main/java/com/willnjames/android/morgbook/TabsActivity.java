package com.willnjames.android.morgbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by jamesprijatna on 7/10/16.
 */
public class TabsActivity extends android.app.TabActivity implements View.OnClickListener {

    private Button tab0;
    private Button tab1;
    private Button tab2;

    private Button[] btn = new Button[3];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.tab0, R.id.tab1, R.id.tab2};

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.tabs );

        tab0 = (Button) findViewById(R.id.tab0);
        tab1 = (Button) findViewById(R.id.tab1);
        tab2 = (Button) findViewById(R.id.tab1);

        tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("first").setIndicator("First").setContent(new Intent(this  ,DashboardActivity.class )));
        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("Second").setContent(new Intent(this  ,AttendanceActivity.class )));
        tabHost.addTab(tabHost.newTabSpec("third").setIndicator("Third").setContent(new Intent(this  ,SecondActivity.class )));
        tabHost.setCurrentTab(0);

        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.parseColor("#3F51B5"));
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
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setBackgroundColor(Color.parseColor("#3F51B5"));
        btn_focus.setBackgroundColor(Color.parseColor("#303F9F"));
    }

    private void initFocus(Button btn_unfocus){
        btn_unfocus.setBackgroundColor(Color.parseColor("#303F9F"));
    }

}