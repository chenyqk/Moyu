package com.mstc.moyu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_schedule);

        courseTableLayout = (RelativeLayout) findViewById(R.id.course_table);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        timeTable = (TableLayout)findViewById(R.id.timeTable);
//        dateTable = (TableLayout)findViewById(R.id.dateTable);
//        scrollView = (ScrollView)findViewById(R.id.scrollView);

        wm = this.getWindowManager();
        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,windowHeight - 100);
//        layoutParams.setMargins(0,100,0,0);
//        scrollView.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(windowWidth - 100, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(100,0,0,0);//left top right bottom
        relativeLayout.setLayoutParams(layoutParams);

        //layoutParams.setMargins(0,0,0,0);
        //scrollView.setLayoutParams(layoutParams);
        drawTimeTable();
        drawTable(0);
//        drawDateTable(0);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case FOCUS_CHANGED: {
//                        dateTable.removeAllViews();
                        courseTableLayout.removeAllViews();
                        tableLayout.removeAllViews();
                        courseTableLayout.addView(tableLayout);
//                        drawDateTable((int)msg.obj);
                        drawTable((int)msg.obj);
                        break;
                    }
                }
            }
        };
    }

    /**
     * draw the table
     * @param c define which column to be enlarge
     */
    void drawTable(int c){

        Log.d("test","1");
        for(int row=0;row<8;row++){
            tableRow = new TableRow(ScheduleFragment.this);
            Log.d("test","2");
            tableRow.setBackgroundColor(Color.WHITE);
            Log.d("test","3");
            for (int col = 0; col < 7; col++) {
                final TextView tv = new TextView(ScheduleFragment.this);
                if(row == 0){
                    tv.setText(Date[col]);
                } else {
                    Log.d("test", "4");
                    tv.setText("");
                    Log.d("test", "5");
                }
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.biankuang);
                final int finalCol = col;
                tv.setOnTouchListener(new View.OnTouchListener() {
                    @Override public boolean onTouch(View v, MotionEvent event) {
                        Message msg = new Message();
                        msg.what = FOCUS_CHANGED;
                        msg.obj = finalCol;
                        handler.sendMessage(msg);
                        return false;
                    }
                });

                if (col == c) {
                    tableRow.addView(tv, new TableRow.LayoutParams(2*windowWidth/7, 200, 1));
                } else {
                    tableRow.addView(tv, new TableRow.LayoutParams(windowWidth/7, 200, 1));
                }
               // Log.d("test", "6");
            }

            tableLayout.addView(tableRow);
            //Log.d("test","7");
        }
        addItem("有聊",1,1,2,c);//Tuesday,from class 1 to class 2
        addItem("好有聊",4,3,4,c);//Friday, from class 3 to class 4
    }

    /**
     * add a class, or an issue
     * @param text classinfo
     * @param date from Mon. to Sun. , from 0 to 6
     * @param start start class
     * @param end end class
     * @param enlarge tell me which date is to enlarge
     */
    void addItem(String text,int date,int start,int end,int enlarge){
        //Log.d("test", "8");

        TextView info = new TextView(this);
       // Log.d("test", "9");
        //set information
        info.setText(text);
        ////set background
        info.setBackgroundResource(R.drawable.biankuang);
        //set gravity
        info.setGravity(Gravity.CENTER);
        info.setTextSize(12);
        //courseTableLayout.addView(info);
        int w,itemHeight,wEnlarge,itemWidth;
        w = windowWidth/7;
        wEnlarge = 2*windowWidth/7;
        itemHeight = 200;
        if(date == enlarge){
            itemWidth = wEnlarge;
        } else {
            itemWidth = w;
        }
        //Log.d("test", "10");
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(itemWidth,itemHeight*(end-start+1));
        rlp.topMargin = itemHeight * start;
        rlp.leftMargin = date*w;
        if(date > enlarge) {
            rlp.leftMargin = (date-1)*w + wEnlarge;
        }
        //Log.d("test", "11");
        info.setLayoutParams(rlp);
        //Log.d("test", "12");
        courseTableLayout.addView(info);
        //Log.d("test", "13");
    }

    /**
     *  draw the time table
     */
    void drawTimeTable(){
        for(int i=0;i<8;i++){
            TextView tx = new TextView(this);
            TableRow tr = new TableRow(this);
            if(i > 0) {
                tx.setText(i + "");
            }
            tx.setGravity(Gravity.CENTER);
            tx.setBackgroundResource(R.drawable.biankuang);
            tr.addView(tx, new TableRow.LayoutParams(windowWidth/7,200));
            timeTable.addView(tr);
        }
    }

//    /**
//     *
//     * @param c the column to be enlarge
//     */
//    void drawDateTable(int c){
//        TableRow tr = new TableRow(this);
//        for(int i=0;i<8;i++){
//            TextView tv = new TextView(this);
//            final int finalCol = i;
//            if(i > 0) {
//                tv.setText(Date[i-1]);
//                tv.setOnTouchListener(new View.OnTouchListener() {
//                    @Override public boolean onTouch(View v, MotionEvent event) {
//                        Message msg = new Message();
//                        msg.what = FOCUS_CHANGED;
//                        msg.obj = finalCol - 1;
//                        handler.sendMessage(msg);
//                        return false;
//                    }
//                });
//            }
//            tv.setGravity(Gravity.CENTER);
//            tv.setBackgroundResource(R.drawable.biankuang);
//
//
//            if(i-1 == c){
//                tr.addView(tv,new TableRow.LayoutParams(2*windowWidth/7,100));
//            } else {
//                tr.addView(tv, new TableRow.LayoutParams(windowWidth / 7, 100));
//            }
//        }
//        dateTable.addView(tr);
//    }
}
