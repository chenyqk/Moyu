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

import com.mstc.db.Affair;
import com.mstc.db.Course;
import com.mstc.db.DataBaseFactory;
import com.mstc.db.DataBaseHelper;
import com.mstc.db.MoyuContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

        //insert record test
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                DataBaseFactory.ClearAffairTable(dataBaseHelper);
                DataBaseFactory.ClearCourseTable(dataBaseHelper);
                DataBaseFactory.ClearDeletedRepeatAffairTable(dataBaseHelper);
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                Course course = new Course("高等数学","340201",1,2,"2016-05-17","1,2");//第二周,周三,1-2节课
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
                Affair affair1 = new Affair("打羽毛球","和小雅打羽毛球",2,3,"2016-05-25","0,1,2,3",-1,"0000000");//第三周，周四，早间和1-3节课
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
                Affair affair2 = new Affair("跑步","西区运动场",2,3,"2016-05-25","14",3,"0000000");//每月，晚间
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
                Affair affair3 = new Affair("跑步","西区运动场",2,3,"2016-05-25","14",3,"0000000");//每月，晚间，5月25晚暂停跑步
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
            }
        }).start();

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
        rlp.topMargin = itemHeight * start;
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
                        } else {
                            noConflict = false;
                        }
                    }
                    if(noConflict){
                        addItem(course.course_name+"\n@"+course.classroom,course.day_of_week,startTime,endTime,enlargeCol);
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
                    for(int j=startTime;j<=endTime;j++){//find conflict
                        if(isOccupied[j] ==0){
                            isOccupied[j] = 1;
                        } else if(isOccupied[j] == 1) {
                            isOccupied[j] = 2;
                            noConflict = 2;
                        } else {
                            noConflict = 0;
                        }
                    }
                    if(noConflict == 1){
                        addItem(affair.description,affair.day_of_week,startTime,endTime,enlargeCol);
                    } else if(noConflict == 2){//have both course and affair
                        //TODO: change background when we have both course and affair
                        addItem(affair.description,affair.day_of_week,startTime,endTime,enlargeCol);
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
                            for(int j=startTime;j<=endTime;j++){//find conflict
                                if(isOccupied[j] ==0){
                                    isOccupied[j] = 1;
                                } else if(isOccupied[j] == 1) {
                                    isOccupied[j] = 2;
                                    noConflict = 2;
                                } else {
                                    noConflict = 0;
                                }
                            }
                            if(noConflict == 1){
                                addItem(affair.description,day_of_week,startTime,endTime,enlargeCol);
                            } else if(noConflict == 2){//have both course and affair
                                //TODO: change background when we have both course and affair
                                addItem(affair.description,day_of_week,startTime,endTime,enlargeCol);
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
