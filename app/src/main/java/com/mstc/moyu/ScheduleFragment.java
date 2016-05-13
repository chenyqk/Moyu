package com.mstc.moyu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import android.view.Window;
import android.widget.TableLayout;

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
    int windowWidth,windowHeight;
    int currentWeek;
    int today;

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
                    windowHeight = myTimeTableView.getMeasuredHeight();
                    windowWidth = myTimeTableView.getMeasuredWidth();
                    myTimeTableView.setWindowWidth(windowWidth);
                    myTimeTableView.setWindowHeight(windowHeight);
                    currentWeek = 0;
                    Calendar c = Calendar.getInstance();
                    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    today = c.get(Calendar.DATE);
                    myTimeTableView.setWeekDay(getWeekDay(today));
                    myTimeTableView.drawTimeTable();
                    myTimeTableView.drawDateTable(getDayofWeek(today));
                    myTimeTableView.drawTable(getDayofWeek(today));
                }
                return true;
            }
        });

        BroadcastReceiver weekChangedReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() == "SHOW_WEEK_CHANGED"){
                    Bundle bundle = intent.getExtras();
                    int day = today + 7*(bundle.getInt("SHOW_WEEK") - currentWeek);
                    TableLayout tableLayout,myDateTable,myTimeTable;
                    View view = LayoutInflater.from(context).inflate(R.layout.my_time_table_view,null);
                    tableLayout = (TableLayout)view.findViewById(R.id.myTable);
                    myDateTable = (TableLayout)view.findViewById(R.id.myDateTable);
                    myTimeTable = (TableLayout)view.findViewById(R.id.myTimeTable);
                    tableLayout.removeAllViews();
                    myDateTable.removeAllViews();
                    myTimeTable.removeAllViews();
                    myTimeTableView.drawTimeTable();
                    myTimeTableView.drawDateTable(getDayofWeek(day));
                    myTimeTableView.drawDateTable(getDayofWeek(day));
                }
            }
        };

    }
    int getDayofWeek(int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE,day);
        int date = c.get(Calendar.DAY_OF_WEEK);
        date = date - 2;
        if(date == -1){
            date = 6;
        }
        return date;
    }
    String[] getWeekDay(int day){
        String WeekDay[] = new String[7];
        Calendar c = Calendar.getInstance();
        //c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        //int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE,day);
        int date = getDayofWeek(day);
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
