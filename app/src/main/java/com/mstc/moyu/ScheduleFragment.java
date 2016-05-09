package com.mstc.moyu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ScheduleFragment extends Activity {

    WindowManager wm = null;
    private int windowWidth;
    private int windowHeight;

    private RelativeLayout courseTableLayout = null;
    private RelativeLayout relativeLayout = null;

    private TableLayout tableLayout = null;
    private TableLayout timeTable = null;
//    private TableLayout dateTable = null;
//
//    private ScrollView scrollView = null;

    private TableRow tableRow = null;
    private Handler handler = null;
    private String[] Date = {"周一","周二","周三","周四","周五","周六","周日"};

    private final int FOCUS_CHANGED = 0;

    private MyTimeTableView myTimeTableView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_schedule);
        myTimeTableView = (MyTimeTableView)findViewById(R.id.myTimeTableView);
        //myTimeTableView.drawTable(2);
        //myTimeTableView.postInvalidate();
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
