package com.mstc.moyu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mstc.db.Course;
import com.mstc.db.DataBaseHelper;
import com.mstc.db.MoyuContract;

import java.util.Vector;
/**
 * TODO: document your custom view class.
 */
public class MyTimeTableView extends RelativeLayout {

    private String[] DayOfWeek = {"周一","周二","周三","周四","周五","周六","周日"};
    private String[] WeekDay = {"","","","","","",""};
    private String[] Time = {"早间","8:00\n1","8:55\n2","10:00\n3","10:55\n4","午间","14:30\n5","15:25\n6","16:20\n7",
                                            "17:15\n8","19:00\n9","19:55\n10","20:50\n11","21:45\n12","晚间"};
    int currentWeek;
    MyHorizontalScrollView dateScrollView,tableHorizonScrollView;
    MyScrollView timeScrollView,tableScrollView;
    TableLayout tableLayout,myTimeTable,myDateTable;
    RelativeLayout tableRelativeLayout;
    TableRow tableRow;
    int enlargeCol;
    int rowNum,colNum,showColNum,showRowNum;
    int cellBackGroundResource;
    int windowWidth,windowHeight;
    private final int FOCUS_CHANGED = 233;
    Vector<TextView> textViewVector;

    private Handler handler;

    public MyTimeTableView(Context context) {
        super(context);
        init(context);
    }

    public MyTimeTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTimeTableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setWindowWidth(int windowWidth){
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight){
        this.windowHeight = windowHeight;
    }

    public void setWeekDay(String[] WeekDay){
        for(int i=0;i<7;i++){
            this.WeekDay[i] = DayOfWeek[i] + "\n" + WeekDay[i];
        }
    }

    private void init(Context context) {
        enlargeCol = 1;
        rowNum = 15;
        colNum = 7;
        showRowNum = 11;
        showColNum = 7;
        cellBackGroundResource = R.drawable.biankuang;
        textViewVector = new Vector<>();
        currentWeek = 0;
        inflate(context,R.layout.my_time_table_view,this);
        tableRelativeLayout = (RelativeLayout)findViewById(R.id.tableRelativeLayout);
        dateScrollView = (MyHorizontalScrollView)findViewById(R.id.myDateScrollView);
        tableHorizonScrollView = (MyHorizontalScrollView)findViewById(R.id.myHorizontalScrollView);

        timeScrollView = (MyScrollView)findViewById(R.id.myTimeScrollView);
        tableScrollView = (MyScrollView)findViewById(R.id.myScrollView);

        tableLayout = (TableLayout)findViewById(R.id.myTable);
        myTimeTable = (TableLayout)findViewById(R.id.myTimeTable);
        myDateTable = (TableLayout)findViewById(R.id.myDateTable);

        dateScrollView.setOnHorizontalScrollViewListener(new HorizontalScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView myHorizonalScrollView, int l, int t, int oldl, int oldt) {
                tableHorizonScrollView.scrollTo(l,t);
            }
        });
        tableHorizonScrollView.setOnHorizontalScrollViewListener(new HorizontalScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView myHorizonalScrollView, int l, int t, int oldl, int oldt) {
                dateScrollView.scrollTo(l,t);
            }
        });
        timeScrollView.setOnScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                tableScrollView.scrollTo(x,y);
            }
        });
        tableScrollView.setOnScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                timeScrollView.scrollTo(x,y);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FOCUS_CHANGED: {
                        enlargeCol = (int)msg.obj;
                        cleanAllTable();
                        drawTimeTable();
                        drawDateTable(enlargeCol);
                        drawTable(currentWeek,enlargeCol);
                        break;
                    }
                }
            }
        };
    }

    /**
       * @param enlargeCol define which col to be enlarged
     */
    void drawTable(int week,int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth*(1-(1/showColNum)),windowHeight*(1-(1/showRowNum)));
        rlp.leftMargin = windowWidth/showColNum;
        rlp.topMargin = windowHeight/showRowNum;
        tableScrollView.setLayoutParams(rlp);
        for(int row=0;row<rowNum;++row){
            tableRow = new TableRow(getContext());
            for(int col=0;col<colNum;++col){
                TextView tv = new TextView(getContext());
                tv.setText("");
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(cellBackGroundResource);
                final int finalCol = col;
                final int finalRow = row;
                tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("row,col", finalRow +","+finalCol);
                        Message msg = new Message();
                        msg.what = FOCUS_CHANGED;
                        msg.obj = finalCol;
                        handler.sendMessage(msg);
                    }
                });
                tv.setLongClickable(true);
                tv.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("Long click","LA La LA");
                        Intent intent = new Intent(getContext(),AddItemActivity.class);
                        getContext().startActivity(intent);
                        return false;
                    }
                });
                if(col == enlargeCol){
                    tableRow.addView(tv,new TableRow.LayoutParams(2*windowWidth/showColNum,windowHeight/showRowNum,1));
                } else {
                    tableRow.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,windowHeight/showRowNum,1));
                }
            }
            tableLayout.addView(tableRow);
        }
//        addItem("有聊",1,1,2,enlargeCol);//Tuesday,from class 1 to class 2
//        addItem("好有聊",4,3,4,enlargeCol);//Friday, from class 3 to class 4
//        addItem("超级无敌有聊",2,1,4,enlargeCol);//Wednesday, from class 1 to class 4
        currentWeek = week;
        addEvents(week,enlargeCol);
    }

    void drawTimeTable(){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth/colNum,windowHeight*(1-(1/showRowNum)));
        rlp.topMargin = windowHeight/showRowNum;
        rlp.leftMargin = 0;
        timeScrollView.setLayoutParams(rlp);
        for(int row=1;row<=rowNum;++row){
            //Log.d("row",row+"");
            TextView tv = new TextView(getContext());
            TableRow tr = new TableRow(getContext());
            tv.setText(Time[row-1]);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(cellBackGroundResource);
            tr.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,windowHeight/showRowNum,1));
            myTimeTable.addView(tr);
        }
    }

    void drawDateTable(int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth*(1 - (1/colNum)),windowHeight/showRowNum);
        rlp.leftMargin = windowWidth/colNum;
        rlp.topMargin = 0;
        dateScrollView.setLayoutParams(rlp);
        TableRow tr = new TableRow(getContext());
        for(int col=0;col<colNum;++col){
            TextView tv = new TextView(getContext());
            final int finalCol = col;
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(cellBackGroundResource);

            tv.setText(WeekDay[col]);
            //Log.d("date", WeekDay[col]);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("Touched", WeekDay[finalCol]);
                    Message msg = new Message();
                    msg.what = FOCUS_CHANGED;
                    msg.obj = finalCol;
                    handler.sendMessage(msg);
                }
            });

            if(col == enlargeCol){
                tr.addView(tv,new TableRow.LayoutParams(2*windowWidth/colNum,windowHeight/showRowNum,1));
            } else {
                tr.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,windowHeight/showRowNum,1));
            }
        }
        myDateTable.addView(tr);
    }

    void addItem(String text, int date, int start, int end, int enlargeCol){
        TextView info = new TextView(getContext());
        final int finalDate = date;
        final String finalText = text;
        textViewVector.add(info);
        info.setText(text);
        info.setBackgroundResource(cellBackGroundResource);
        info.setGravity(Gravity.CENTER);
        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("item",finalText);
                Message msg = new Message();
                msg.what = FOCUS_CHANGED;
                msg.obj = finalDate;
                handler.sendMessage(msg);
            }
        });
        int w,itemHeight,wEnlarge,itemWidth;
        w = windowWidth/colNum;
        info.setTextSize((w-16)/6);
        wEnlarge = 2*windowWidth/colNum;
        itemHeight = windowHeight/showRowNum;
        if(date == enlargeCol){
            itemWidth = wEnlarge;
        } else {
            itemWidth = w;
        }
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(itemWidth,itemHeight*(end-start+1));
        rlp.topMargin = itemHeight * (start-1);
        rlp.leftMargin = date*w;
        if(date > enlargeCol){
            rlp.leftMargin = (date-1)*w + wEnlarge;
        }
        info.setLayoutParams(rlp);
        tableRelativeLayout.addView(info);
    }

    void cleanAllTable(){
        tableLayout.removeAllViews();
        myDateTable.removeAllViews();
        myTimeTable.removeAllViews();
        if(textViewVector.size() > 0){
            for(int i=0;i<textViewVector.size();++i){
                TextView tx = textViewVector.get(i);
                tableRelativeLayout.removeView(tx);
            }
            textViewVector.clear();
        }
    }

    void addEvents(int week,int enlargeCol){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String[] projection = {
                MoyuContract.CourseEntry.COURSE_NAME,
                MoyuContract.CourseEntry.CLASSROOM,
                MoyuContract.CourseEntry.WEEK,
                MoyuContract.CourseEntry.DAY_OF_WEEK,
                MoyuContract.CourseEntry.DATE,
                MoyuContract.CourseEntry.TIME
        };
        Cursor cursor = db.query(MoyuContract.CourseEntry.TABLE_NAME,
                projection,
                MoyuContract.CourseEntry.WEEK+"=?",
                new String[]{week+""},
                null,
                null,
                null
                );
        while(cursor.moveToNext()){
            Course course = new Course(cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.CLASSROOM)),
                    cursor.getInt(cursor.getColumnIndex(MoyuContract.CourseEntry.WEEK)),
                    cursor.getInt(cursor.getColumnIndex(MoyuContract.CourseEntry.DAY_OF_WEEK)),
                    cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.DATE)),
                    cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.TIME))
            );
            Vector<Integer> timeVector = Course.parseTimeStr(course.time);
            addItem(course.course_name+"@\n"+course.classroom,course.day_of_week,timeVector.firstElement(),timeVector.lastElement(),enlargeCol);
        }
        db.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce
    }

}
