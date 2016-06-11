package com.mstc.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mstc.db.Affair;
import com.mstc.db.Course;
import com.mstc.db.DataBaseFactory;
import com.mstc.db.DataBaseHelper;
import com.mstc.db.MoyuContract;
import com.mstc.moyu.AddItemActivity;
import com.mstc.moyu.MainActivity;
import com.mstc.moyu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
/**
 * TODO: document your custom view class.
 */
public class MyTimeTableView extends RelativeLayout {

    private String[] DayOfWeek = {"周一","周二","周三","周四","周五","周六","周日"};
    private String[] WeekDay = {"","","","","","",""};
    private String[] Time = {"早间","8:00\n\n1","8:55\n\n2","10:00\n\n3","10:55\n\n4","午间","14:30\n\n5","15:25\n\n6","16:20\n\n7",
                                            "17:15\n\n8","19:00\n\n9","19:55\n\n10","20:50\n\n11","21:45\n\n12","晚间"};
    int currentWeek;
    MyHorizontalScrollView dateScrollView,tableHorizonScrollView;
    MyScrollView timeScrollView,tableScrollView;
    TableLayout tableLayout,myTimeTable,myDateTable;
    RelativeLayout tableRelativeLayout,myTimeTableRelativeLayout;
    TableRow tableRow;
    int enlargeCol;
    int rowNum,colNum,showColNum,showRowNum;
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
            this.WeekDay[i] = WeekDay[i];
        }
    }

    private void init(Context context) {
        enlargeCol = 1;
        rowNum = 15;
        colNum = 7;
        showRowNum = 11;
        showColNum = 7;
        textViewVector = new Vector<>();
        //currentWeek = 0;
        Date today,firstWeekMonday;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
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
        inflate(context, R.layout.my_time_table_view,this);
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

        TextView headerText = new TextView(getContext());
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.x80),(int)getResources().getDimension(R.dimen.y112));
        rlp.leftMargin = 0;
        rlp.topMargin = 0;
        headerText.setLayoutParams(rlp);
        SpannableString headerString  = new SpannableString("周数\n/\n时间");
        headerString.setSpan(new TextAppearanceSpan(getContext(),R.style.HeaderTitleStyle1),0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headerString.setSpan(new TextAppearanceSpan(getContext(),R.style.HeaderTitleStyle2),4,headerString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headerText.setText(headerString,TextView.BufferType.SPANNABLE);
        headerText.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuTextGrey));
        headerText.setBackgroundResource(R.drawable.shu_122);
        headerText.setGravity(Gravity.CENTER);
        myTimeTableRelativeLayout = (RelativeLayout)findViewById(R.id.myTimeTableRelativeLayout);
        myTimeTableRelativeLayout.addView(headerText);

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



        //insert record test, can remove after passed all test
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                DataBaseFactory.ClearAffairTable(dataBaseHelper);
                DataBaseFactory.ClearCourseTable(dataBaseHelper);
                DataBaseFactory.ClearDeletedRepeatAffairTable(dataBaseHelper);
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                Course course = new Course("高等数学","340201",0,2,"2016-05-25","1,2,6,7");//第一周,周三,1-2,5-6节课(午间，有1个单位的偏移)
                String[] projection = {MoyuContract.CourseEntry.COURSE_NAME,MoyuContract.CourseEntry.CLASSROOM};
                String selection = MoyuContract.CourseEntry.COURSE_NAME + " LIKE ?";
                String[] selectionArgs = {course.course_name};
                Cursor cursor = db.query(MoyuContract.CourseEntry.TABLE_NAME,projection, selection,selectionArgs,null,null,null);
                if(cursor.getCount() == 0){
                    Log.d("sql","insert course~");
                    DataBaseFactory.InsertCourse(dataBaseHelper,course);
                }
                cursor.close();
                //non-repeat affair
                Affair affair1 = new Affair("打羽毛球","和小雅打羽毛球",1,3,"2016-06-02","0,1,2,3",-1,"0000000");//第一周，周四，早间和1-3节课
                projection[0] = MoyuContract.AffairEntry.DESCRIPTION;
                projection[1] = MoyuContract.AffairEntry.DATE;
                selection = MoyuContract.AffairEntry.DESCRIPTION + " LIKE ?";
                selectionArgs[0] = affair1.description;
                cursor = db.query(MoyuContract.AffairEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                if(cursor.getCount() == 0){
                    Log.d("sql","insert non-repeat affair~");
                    DataBaseFactory.InsertAffair(dataBaseHelper,affair1);
                }
                cursor.close();
                //repeat affair
                Affair affair2 = new Affair("跑步","西区运动场",2,3,"2016-06-09","14",5,"0000000");//每工作日，晚间
                projection[0] = MoyuContract.AffairEntry.DESCRIPTION;
                projection[1] = MoyuContract.AffairEntry.DATE;
                selection = MoyuContract.AffairEntry.DESCRIPTION + " LIKE ?";
                selectionArgs[0] = affair2.description;
                cursor = db.query(MoyuContract.AffairEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                if(cursor.getCount() == 0){
                    Log.d("sql","insert repeat affair~");
                    DataBaseFactory.InsertAffair(dataBaseHelper,affair2);
                }
                cursor.close();
                //delete repeat affair
                Affair affair3 = new Affair("跑步","西区运动场",2,3,"2016-06-09","14",5,"0000000");//每工作日，晚间，第三周周四暂停跑步
                projection[0] = MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION;
                projection[1] = MoyuContract.DeletedRepeatAffairEntry.DATE;
                selection = MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION + " LIKE ?";
                selectionArgs[0] = affair3.description;
                cursor = db.query(MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                if(cursor.getCount() == 0){
                    Log.d("sql","insert deleted repeat affair~");
                    DataBaseFactory.InsertDeletedRepeatAffair(dataBaseHelper,affair3);
                }
                cursor.close();
                //insert conflict
                Affair affair4 = new Affair("逛街","和小雅逛街",0,2,"2016-05-25","1,2",-1,"0000000");
                projection[0] = MoyuContract.AffairEntry.DESCRIPTION;
                projection[1] = MoyuContract.AffairEntry.DATE;
                selection = MoyuContract.AffairEntry.DESCRIPTION + " LIKE ?";
                selectionArgs[0] = affair4.description;
                cursor = db.query(MoyuContract.AffairEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                Log.d("sql","insert conflict affair : " + cursor.getCount());
                if(cursor.getCount() == 0){
                    Log.d("sql","insert conflict affair~");
                    DataBaseFactory.InsertAffair(dataBaseHelper,affair4);
                }
                cursor.close();
            }
        }).start();

    }

    /**
       * @param enlargeCol define which col to be enlarged
     */
    public void drawTable(int week,int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth-(int)getResources().getDimension(R.dimen.x80),windowHeight-(int)getResources().getDimension(R.dimen.y112));
        rlp.leftMargin = (int)getResources().getDimension(R.dimen.x80);
        rlp.topMargin = (int)getResources().getDimension(R.dimen.y112);
        tableScrollView.setLayoutParams(rlp);
        for(int row=0;row<rowNum;++row){
            tableRow = new TableRow(getContext());
            for(int col=0;col<colNum;++col){
                TextView tv = new TextView(getContext());
                tv.setText("");
                tv.setGravity(Gravity.CENTER);
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
                    tv.setBackgroundResource(R.drawable.heng_152);
                    tableRow.addView(tv,new TableRow.LayoutParams((int)getResources().getDimension(R.dimen.x152),(int)getResources().getDimension(R.dimen.y92),1));
                } else {
                    tv.setBackgroundResource(R.drawable.heng_122);
                    tableRow.addView(tv,new TableRow.LayoutParams((int)getResources().getDimension(R.dimen.x122),(int)getResources().getDimension(R.dimen.y92),1));
                }
            }
            tableLayout.addView(tableRow);
        }
        currentWeek = week;
        addEvents(week,enlargeCol);
    }

    /**
     *  draw the time table(including time and class)
     */
    public void drawTimeTable(){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.x80),windowHeight-(int)getResources().getDimension(R.dimen.y112));
        rlp.topMargin = (int)getResources().getDimension(R.dimen.y112);
        rlp.leftMargin = 0;
        timeScrollView.setLayoutParams(rlp);
        for(int row=1;row<=rowNum;++row){
            //Log.d("row",row+"");
            TextView tv = new TextView(getContext());
            TableRow tr = new TableRow(getContext());
            if(row-1 == 0 || row-1 == 5 || row == rowNum){
                tv.setText(Time[row-1]);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y24));
                tv.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuTimeGrey));
            } else {
                SpannableString timeTitle = new SpannableString(Time[row-1]);
                timeTitle.setSpan(new TextAppearanceSpan(getContext(),R.style.TimeTitleStyle1),0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                timeTitle.setSpan(new TextAppearanceSpan(getContext(),R.style.TimeTitleStyle2),5,timeTitle.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(timeTitle,TextView.BufferType.SPANNABLE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y16));
            }

            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.heng_122);
            tr.addView(tv,new TableRow.LayoutParams((int)getResources().getDimension(R.dimen.x80),(int)getResources().getDimension(R.dimen.y92),1));
            myTimeTable.addView(tr);
        }
    }

    /**
     *  draw the date table(including date and day of week)
     * @param enlargeCol
     */
    public void drawDateTable(int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth-(int)getResources().getDimension(R.dimen.x80),(int)getResources().getDimension(R.dimen.y112));
        rlp.leftMargin = (int)getResources().getDimension(R.dimen.x80);
        rlp.topMargin = 0;
        dateScrollView.setLayoutParams(rlp);
        TableRow tr = new TableRow(getContext());
        for(int col=0;col<colNum;++col){
            TextView tv = new TextView(getContext());
            final int finalCol = col;
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y32));
            SpannableString dateTitle = new SpannableString(DayOfWeek[col]+"\n\n"+WeekDay[col]);
            dateTitle.setSpan(new TextAppearanceSpan(getContext(),R.style.DateTitleStyle1),0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dateTitle.setSpan(new TextAppearanceSpan(getContext(),R.style.DateTitleStyle2),3,dateTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(dateTitle,TextView.BufferType.SPANNABLE);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = FOCUS_CHANGED;
                    msg.obj = finalCol;
                    handler.sendMessage(msg);
                }
            });

            if(col == enlargeCol){
                tv.setBackgroundResource(R.drawable.shu_152);
                tv.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuGreen));
                tr.addView(tv,new TableRow.LayoutParams((int)getResources().getDimension(R.dimen.x152),(int)getResources().getDimension(R.dimen.y112),1));
            } else {
                tv.setBackgroundResource(R.drawable.shu_122);
                tv.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuTextGrey));
                tr.addView(tv,new TableRow.LayoutParams((int)getResources().getDimension(R.dimen.x122),(int)getResources().getDimension(R.dimen.y112),1));
            }
        }
        myDateTable.addView(tr);

    }

    /**
     *  add an item(course or affair)
     * @param text
     * @param date
     * @param start
     * @param end
     * @param enlargeCol
     * @param backGroundId
     * @param item
     */
    void addItem(String text, int date, int start, int end, int enlargeCol, final int backGroundId, final Object item, @Nullable final Object item2){
        TextView info = new TextView(getContext());
        final int finalDate = date;
        final String finalText = text;
        textViewVector.add(info);
        info.setText(text);
        info.setBackgroundResource(backGroundId);
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
        info.setLongClickable(true);
        info.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (backGroundId){
                    case R.drawable.course:{
                        Intent intent = new Intent();
                        intent.setClass(getContext(),AddItemActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("IS_COURSE",true);
                        bundle.putSerializable("COURSE",(Course)item);
                        intent.putExtras(bundle);
                        getContext().startActivity(intent);
                        break;
                    }
                    case R.drawable.affair:{
                        Intent intent = new Intent();
                        intent.setClass(getContext(),AddItemActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("IS_COURSE",false);
                        bundle.putSerializable("AFFAIR",(Affair)item);
                        intent.putExtras(bundle);
                        getContext().startActivity(intent);
                        break;
                    }
                    case R.drawable.conflict:{
                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                        View detailView = layoutInflater.inflate(R.layout.detail_view,null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(detailView).create();
                        alertDialog.getWindow().setBackgroundDrawableResource(R.color.Transparent);

                        TextView affairDetail = (TextView)detailView.findViewById(R.id.affairDetail);
                        TextView courseDetail = (TextView)detailView.findViewById(R.id.courseDetail);
                        final Affair affair = (Affair)item;
                        final Course course = (Course)item2;
                        affairDetail.setText(affair.description+"\n\n"+
                                (affair.week+1)+"周\n\n"+
                                DayOfWeek[affair.day_of_week]+" "+affair.time+"节\n\n"
                        );
                        affairDetail.setWidth((int)getResources().getDimension(R.dimen.x258));
                        affairDetail.setHeight((int)getResources().getDimension(R.dimen.y400));
                        affairDetail.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(getContext(),AddItemActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("IS_COURSE",false);
                                bundle.putSerializable("AFFAIR",affair);
                                intent.putExtras(bundle);
                                getContext().startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        courseDetail.setText(course.course_name+"\n\n"+
                                (course.week+1)+"周\n\n"+
                                DayOfWeek[course.day_of_week]+" "+course.time+"节\n\n"+
                                course.course_name
                        );
                        courseDetail.setWidth((int)getResources().getDimension(R.dimen.x258));
                        courseDetail.setHeight((int)getResources().getDimension(R.dimen.y400));
                        courseDetail.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(getContext(),AddItemActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("IS_COURSE",true);
                                bundle.putSerializable("COURSE",course);
                                intent.putExtras(bundle);
                                getContext().startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        break;
                    }
                    default:{
                        break;
                    }
                }
                return false;
            }
        });
        int itemHeight,itemWidth;
        info.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y26));
        info.setTextColor(Color.WHITE);
        itemHeight = (int)getResources().getDimension(R.dimen.y82);
        if(date == enlargeCol){
            itemWidth = (int)getResources().getDimension(R.dimen.x140);
        } else {
            itemWidth = (int)getResources().getDimension(R.dimen.x112);
        }
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(itemWidth,itemHeight*(end-start+1) + (int)getResources().getDimension(R.dimen.y10)*(end-start));
        rlp.topMargin = (int)getResources().getDimension(R.dimen.y5) + (itemHeight + (int)getResources().getDimension(R.dimen.y10)) * start;
        rlp.leftMargin = (int)getResources().getDimension(R.dimen.x5) + date*(int)getResources().getDimension(R.dimen.x122);
        if(date > enlargeCol){
            rlp.leftMargin = (int)getResources().getDimension(R.dimen.x5) + (date-1)*(int)getResources().getDimension(R.dimen.x122) + (int)getResources().getDimension(R.dimen.x150);
        }
        info.setLayoutParams(rlp);
        tableRelativeLayout.addView(info);
    }

    /**
     * remove all table, need to be call when redraw the table
     */
    public void cleanAllTable(){
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

    /**
     *  procedure as follow
     *  1 - find the 7 day of the week
     *  2 - open database, add item day by day(set an array to judge to find conflicts in course and affair)
     *  3 - add course first, then add affair, if is repeat affair, check if we have record in the delete table
     *  4 -close database
     *
     * @param week which week to show
     * @param enlargeCol which col to enlarge
     */
    void addEvents(final int week, final int enlargeCol){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        for(int day_of_week=0;day_of_week<7;day_of_week++){
            int[] isOccupied = new int[15];
            Course[] ouccupiedCourse = new Course[15];
            Cursor cursor;
            for(int i=0;i<15;i++){
                isOccupied[i] = 0;
            }
            //----------------------------- add course-------------------------------------------
            String[] courseProjection = {
                    MoyuContract.CourseEntry.COURSE_NAME,
                    MoyuContract.CourseEntry.CLASSROOM,
                    MoyuContract.CourseEntry.WEEK,
                    MoyuContract.CourseEntry.DAY_OF_WEEK,
                    MoyuContract.CourseEntry.DATE,
                    MoyuContract.CourseEntry.TIME
            };
            //find current week and current day course
            String courseSelection =
                    MoyuContract.CourseEntry.WEEK + " LIKE ? AND " +
                    MoyuContract.CourseEntry.DAY_OF_WEEK + " LIKE ?";
            String[] courseSelectionArgs = {week+"",day_of_week+""};
            cursor = db.query(MoyuContract.CourseEntry.TABLE_NAME,
                    courseProjection,
                    courseSelection,
                    courseSelectionArgs,
                    null,
                    null,
                    null
            );
            while(cursor.moveToNext()){
                Course course = new Course(
                        cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.COURSE_NAME)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.CLASSROOM)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.CourseEntry.WEEK)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.CourseEntry.DAY_OF_WEEK)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.DATE)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.CourseEntry.TIME))
                );
                Vector<Vector<Integer>> timeVector = Course.parseTimeStr(course.time);
                for(int i=0;i<timeVector.size();i++){
                    int startTime = timeVector.get(i).firstElement();
                    int endTime = timeVector.get(i).lastElement();
                    boolean noConflict = true;
                    for(int j=startTime;j<=endTime;j++){//find conflict
                        if(isOccupied[j] ==0){
                            isOccupied[j] = 1;
                            ouccupiedCourse[j] = course;
                        } else {
                            noConflict = false;
                        }
                    }
                    if(noConflict){
                        addItem(course.course_name+"\n@"+course.classroom,course.day_of_week,startTime,endTime,enlargeCol,R.drawable.course,course,null);
                    } else {
                        Log.d("add item conflit",startTime + "-" + endTime);
                    }
                }
            }
            cursor.close();
            //----------------------------- add affair(non repeat)-------------------------------------------
            String[] affairProjection = {
                    MoyuContract.AffairEntry.DESCRIPTION,
                    MoyuContract.AffairEntry.COMMENT,
                    MoyuContract.AffairEntry.WEEK,
                    MoyuContract.AffairEntry.DAY_OF_WEEK,
                    MoyuContract.AffairEntry.DATE,
                    MoyuContract.AffairEntry.TIME,
                    MoyuContract.AffairEntry.REPEAT,
                    MoyuContract.AffairEntry.ALARM
            };
            String affairSelection =
                    MoyuContract.AffairEntry.WEEK + " LIKE ? AND " +
                    MoyuContract.AffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                    MoyuContract.AffairEntry.REPEAT + " LIKE ?";
            String[] affairSelectionArgs = {week+"",day_of_week+"", "-1"};//repeat == -1 stands for non repeat affair
            cursor = db.query(
                    MoyuContract.AffairEntry.TABLE_NAME,
                    affairProjection,
                    affairSelection,
                    affairSelectionArgs,
                    null,
                    null,
                    null);
            while(cursor.moveToNext()){
                Affair affair = new Affair(
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.COMMENT)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.WEEK)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.DAY_OF_WEEK)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.DATE)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.TIME)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.REPEAT)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.ALARM))
                );
                Vector<Vector<Integer>> timeVector = Affair.parseTimeStr(affair.time);
                for(int i=0;i<timeVector.size();i++){
                    int startTime = timeVector.get(i).firstElement();
                    int endTime = timeVector.get(i).lastElement();
                    int noConflict = 1;
                    Course occupCourse = null;
                    for(int j=startTime;j<=endTime;j++){//find conflict
                        if(isOccupied[j] ==0){
                            isOccupied[j] = 1;
                        } else if(isOccupied[j] == 1) {
                            isOccupied[j] = 2;
                            occupCourse = ouccupiedCourse[j];
                            noConflict = 2;
                        } else {
                            noConflict = 0;
                        }
                    }
                    if(noConflict == 1){
                        addItem(affair.description,affair.day_of_week,startTime,endTime,enlargeCol,R.drawable.affair,affair,null);
                    } else if(noConflict == 2){//have both course and affair
                        //TODO: change background when we have both course and affair
                        addItem(affair.description,affair.day_of_week,startTime,endTime,enlargeCol,R.drawable.conflict,affair,occupCourse);
                    } else {
                        Log.d("add item conflit",startTime + "-" + endTime);
                    }
                }
            }
            cursor.close();
            //----------------------------- add affair(repeat)------------------------------------------------
            String repeatAffairSelection = MoyuContract.AffairEntry.REPEAT + " >= ?";//find all repeat affair
            String[] repeatAffairSelectionArgs = {"0"};//repeat >= 0 stands for repeat affair
            cursor = db.query(MoyuContract.AffairEntry.TABLE_NAME,
                    affairProjection,
                    repeatAffairSelection,
                    repeatAffairSelectionArgs,
                    null,
                    null,
                    null);
            while(cursor.moveToNext()){
                Affair affair = new Affair(
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.COMMENT)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.WEEK)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.DAY_OF_WEEK)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.DATE)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.TIME)),
                        cursor.getInt(cursor.getColumnIndex(MoyuContract.AffairEntry.REPEAT)),
                        cursor.getString(cursor.getColumnIndex(MoyuContract.AffairEntry.ALARM))
                );
                //find if the repeat affair is deleted, execute a query in deletedRepeatAffair
                String[] deletedAffairProjection = {MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION};
                String deletedAffairSelection =
                        MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION + " LIKE ? AND " +
                        MoyuContract.DeletedRepeatAffairEntry.COMMENT + " LIKE ? AND " +
                        MoyuContract.DeletedRepeatAffairEntry.WEEK + " LIKE ? AND " +
                        MoyuContract.DeletedRepeatAffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                        MoyuContract.DeletedRepeatAffairEntry.TIME + " LIKE ?";
                String[] deletedAffairSelectionArgs = {
                        affair.description,
                        affair.comment,
                        week+"",
                        day_of_week+"",
                        affair.time+""
                };
                Cursor mCursor = db.query(
                        MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME,
                        deletedAffairProjection,
                        deletedAffairSelection,
                        deletedAffairSelectionArgs,
                        null,
                        null,
                        null
                        );
                Log.d("delete record match",mCursor.getCount()+"");
                if(mCursor.getCount() == 0){//this repeat affair is not deleted
                    //TODO: check if we have to add this affair in this day
                    boolean isAdd = false;
                    switch (affair.repeat){
                        case 0:{//everyday
                            isAdd = true;
                            break;
                        }
                        case 1:{//every two day
                            int dayDiff = (week - affair.week)*7 + (day_of_week - affair.day_of_week);
                            isAdd = (dayDiff%2 == 0);
                            break;
                        }
                        case 2:{//every week
                            if(day_of_week == affair.day_of_week){
                                isAdd = true;
                            } else {
                                isAdd = false;
                            }
                            break;
                        }
                        case 3:{//every month
                            int dayDiff = (week - affair.week)*7 + (day_of_week - affair.day_of_week);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date affairDate=null;
                            try {
                                affairDate = sdf.parse(affair.date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(affairDate!=null){
                                Calendar c = Calendar.getInstance();
                                c.setTime(affairDate);
                                int affairDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                c.add(Calendar.DATE,dayDiff);
                                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                Log.d("currentDayOfMonth",currentDayOfMonth+"");
                                Log.d("affairDayOfMonth",affairDayOfMonth+"");
                                Log.d("DayDiff",dayDiff+"");
                                Log.d("currentDay",c.get(Calendar.DATE)+"");
                                c.setTime(affairDate);
                                Log.d("affairDay",c.get(Calendar.DATE)+"");
                                if(affairDayOfMonth == currentDayOfMonth){
                                    isAdd = true;
                                } else {
                                    isAdd =false;
                                }
                            }
                            break;
                        }
                        case 4:{//every year
                            int dayDiff = (week - affair.week)*7 + (day_of_week - affair.day_of_week);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date affairDate=null;
                            try {
                                affairDate = sdf.parse(affair.date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(affairDate!=null){
                                Calendar c = Calendar.getInstance();
                                c.setTime(affairDate);
                                int affairDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                int affairMonth = c.get(Calendar.MONTH);
                                c.add(Calendar.DATE,dayDiff);
                                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                int currentMonth = c.get(Calendar.MONTH);
                                if(affairDayOfMonth == currentDayOfMonth && affairMonth == currentMonth){
                                    isAdd = true;
                                } else {
                                    isAdd =false;
                                }
                            }
                            break;
                        }
                        case 5:{//work day
                            if(day_of_week <= 4){
                                isAdd = true;
                            } else {
                                isAdd = false;
                            }
                            break;
                        }
                    }
                    if(isAdd){
                        Vector<Vector<Integer>> timeVector = Affair.parseTimeStr(affair.time);
                        for(int i=0;i<timeVector.size();i++){
                            int startTime = timeVector.get(i).firstElement();
                            int endTime = timeVector.get(i).lastElement();
                            int noConflict = 1;
                            Course occupCourse = null;
                            for(int j=startTime;j<=endTime;j++){//find conflict
                                if(isOccupied[j] ==0){
                                    isOccupied[j] = 1;
                                } else if(isOccupied[j] == 1) {
                                    isOccupied[j] = 2;
                                    occupCourse = ouccupiedCourse[j];
                                    noConflict = 2;
                                } else {
                                    noConflict = 0;
                                }
                            }
                            if(noConflict == 1){
                                addItem(affair.description,day_of_week,startTime,endTime,enlargeCol,R.drawable.affair,affair,null);
                            } else if(noConflict == 2){//have both course and affair
                                //TODO: change background when we have both course and affair
                                addItem(affair.description,day_of_week,startTime,endTime,enlargeCol,R.drawable.conflict,affair,occupCourse);
                            } else {
                                Log.d("add item conflit",startTime + "-" + endTime);
                            }
                        }
                    }
                }
                mCursor.close();
            }
            cursor.close();
        }
        db.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce
    }

}
