package com.mstc.moyu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.mstc.customview.MyTimeTableView;
import com.mstc.customview.MyTopBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ScheduleFragment extends Activity {


    private MyTimeTableView myTimeTableView = null;
    private MyTopBarView myTopBarView = null;
    private ViewTreeObserver vto = null;
    boolean hasMeasured = false;
    int windowWidth,windowHeight;
    int currentWeek,showWeek;

    Date today,day,firstWeekMonday;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_schedule);
        c = Calendar.getInstance(TimeZone.getDefault());
        today = c.getTime();
        today = c.getTime();
        try {
            firstWeekMonday = new SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.firstWeekMonStr);
            int dayDiff = (int)((today.getTime() - firstWeekMonday.getTime())/(24*60*60*1000));
            currentWeek = (int)(dayDiff/7);
            Log.d("daydiff",dayDiff+"");
            Log.d("current week",currentWeek+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myTopBarView = (MyTopBarView)findViewById(R.id.myTopBarView);

        myTimeTableView = (MyTimeTableView)findViewById(R.id.myTimeTableView);
        vto = myTimeTableView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(hasMeasured == false) {
                    hasMeasured = true;
                    windowHeight = myTimeTableView.getMeasuredHeight();
                    windowWidth = myTimeTableView.getMeasuredWidth();
                    myTimeTableView.setWindowWidth(windowWidth);
                    myTimeTableView.setWindowHeight(windowHeight);

                    myTimeTableView.setWeekDay(getWeekDay(today));
                    myTimeTableView.drawTimeTable();
                    myTimeTableView.drawDateTable(getDayOfWeek(today));
                    myTimeTableView.drawTable(currentWeek,getDayOfWeek(today));
                }
                return true;
            }
        });

        BroadcastReceiver weekChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Reciever","1");
                if(intent.getAction() == "SHOW_WEEK_CHANGED"){
                    Log.d("Reciever","2");
                    Bundle bundle = intent.getExtras();
                    c.setTime(today);
                    showWeek = bundle.getInt("SHOW_WEEK");
                    Log.d("show week",showWeek+"");
                    c.add(Calendar.DATE,7*(showWeek - currentWeek));
                    day = c.getTime();
                    myTimeTableView.cleanAllTable();
                    myTimeTableView.setWeekDay(getWeekDay(day));
                    myTimeTableView.drawTimeTable();
                    myTimeTableView.drawDateTable(getDayOfWeek(day));
                    myTimeTableView.drawTable(showWeek,getDayOfWeek(day));
                }
            }
        };
        IntentFilter weekChangedFilter = new IntentFilter();
        weekChangedFilter.addAction("SHOW_WEEK_CHANGED");
        this.registerReceiver(weekChangedReceiver,weekChangedFilter);
    }

    /**
     *  get the day of week(from 0 to 6, stands for Mon. to Sun.)
     * @param day
     * @return
     */
    int getDayOfWeek(Date day){
        c.setTime(day);
        int date = c.get(Calendar.DAY_OF_WEEK);
        date = date - 2;
        if(date == -1){
            date = 6;
        }
        return date;
    }

    /**
     *  given a day, return an string array of 7 days
     * @param day
     * @return
     */
    String[] getWeekDay(Date day){
        String WeekDay[] = new String[7];
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        int date = getDayOfWeek(day);
        WeekDay[date] = new SimpleDateFormat("MM-dd").format(c.getTime());
        int pos = date;
        while(pos < 6){
            ++pos;
            c.add(Calendar.DATE,1);
            WeekDay[pos] = new SimpleDateFormat("MM-dd").format(c.getTime());
            Log.d("WeekDay",pos+","+WeekDay[pos]);
        }
        pos = date;
        c.setTime(day);
        while(pos > 0){
            --pos;
            c.add(Calendar.DATE,-1);
            WeekDay[pos] = new SimpleDateFormat("MM-dd").format(c.getTime());
            Log.d("WeekDay",pos+","+WeekDay[pos]);
        }
        return WeekDay;
    }

}
