package com.mstc.moyu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ScheduleFragment extends Activity {

    private MyTimeTableView myTimeTableView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_schedule);
        myTimeTableView = (MyTimeTableView)findViewById(R.id.myTimeTableView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myTimeTableView.setWindowWidth(metrics.widthPixels);
        myTimeTableView.setWindowHeight(metrics.heightPixels);
        //myTimeTableView.drawDateTable(2);
        myTimeTableView.drawTimeTable();
        myTimeTableView.drawDateTable(2);
        myTimeTableView.drawTable(2);
    }

}
