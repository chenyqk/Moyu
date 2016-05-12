package com.mstc.moyu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ScheduleFragment extends Activity {

    private MyTimeTableView myTimeTableView = null;
    private MyTopBarView myTopBarView = null;
    private ViewTreeObserver vto = null;
    boolean hasMeasured = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_schedule);

        myTopBarView = (MyTopBarView)findViewById(R.id.myTopBarView);

        myTimeTableView = (MyTimeTableView)findViewById(R.id.myTimeTableView);
        vto = myTimeTableView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(hasMeasured == false) {
                    hasMeasured = true;
                    myTimeTableView.setWindowWidth(myTimeTableView.getMeasuredWidth());
                    myTimeTableView.setWindowHeight(myTimeTableView.getMeasuredHeight());
                    myTimeTableView.setWeekDay(getWeekDay());

                    myTimeTableView.drawTimeTable();
                    myTimeTableView.drawDateTable(getDayofWeek());
                    myTimeTableView.drawTable(getDayofWeek());
                }
                return true;
            }
        });




    }
    int getDayofWeek(){
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_WEEK);
        date = date - 2;
        if(date == -1){
            date = 6;
        }
        return date;
    }
    String[] getWeekDay(){
        String WeekDay[] = new String[7];
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int day = c.get(Calendar.DATE);
        int date = getDayofWeek();
        WeekDay[date] = new SimpleDateFormat("MM-dd").format(c.getTime());
        int pos = date;
        int val = day;
        while(pos < 6){
            ++pos;
            ++val;
            c.set(Calendar.DATE,val);
            WeekDay[pos] = new SimpleDateFormat("MM-dd").format(c.getTime());
            Log.d("WeekDay",pos+","+WeekDay[pos]);
        }
        pos = date;
        val = day;
        while(pos > 0){
            --pos;
            --val;
            c.set(Calendar.DATE,val);
            WeekDay[pos] = new SimpleDateFormat("MM-dd").format(c.getTime());
            Log.d("WeekDay",pos+","+WeekDay[pos]);
        }
        return WeekDay;
    }

}
