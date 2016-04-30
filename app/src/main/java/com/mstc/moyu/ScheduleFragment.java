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

    private RelativeLayout courseTableLayout = null;
    private TableLayout tableLayout = null;
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
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        wm = this.getWindowManager();
        windowWidth = wm.getDefaultDisplay().getWidth();

        drawTable(0);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case FOCUS_CHANGED: {
                        courseTableLayout.removeAllViews();
                        tableLayout.removeAllViews();
                        courseTableLayout.addView(tableLayout);
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
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
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
                    tableRow.addView(tv, new TableRow.LayoutParams(windowWidth/3, 200, 1));
                } else {
                    tableRow.addView(tv, new TableRow.LayoutParams(windowWidth/6, 200, 1));
                }
               // Log.d("test", "6");
            }

            tableLayout.addView(tableRow);
            //Log.d("test","7");
        }
        addItem("有聊",1,1,2,c);
        addItem("好有聊",4,3,4,c);
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
        w = windowWidth/6;
        wEnlarge = windowWidth/3;
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
}
