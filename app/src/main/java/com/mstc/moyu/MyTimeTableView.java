package com.mstc.moyu;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Vector;

/**
 * TODO: document your custom view class.
 */
public class MyTimeTableView extends RelativeLayout {

    private String[] Date = {"周一","周二","周三","周四","周五","周六","周日"};
    MyHorizontalScrollView dateScrollView,tableHorizonScrollView;
    MyScrollView timeScrollView,tableScrollView;
    TableLayout tableLayout,myTimeTable,myDateTable;
    RelativeLayout tableRelativeLayout;
    TableRow tableRow;
    int enlargeCol;
    int rowNum,colNum;
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
    private void init(Context context) {
        enlargeCol = 1;
        rowNum = 8;
        colNum = 7;
        cellBackGroundResource = R.drawable.biankuang;
        textViewVector = new Vector<>();
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
                        Log.d("handler","focus changed~");
                        //removeAllViews();
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
                        drawTimeTable();
                        drawDateTable(enlargeCol);
                        drawTable(enlargeCol);
                        break;
                    }
                }
            }
        };
    }

    void drawTable(int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth*(1-(1/colNum)),windowHeight-200);
        rlp.leftMargin = windowWidth/colNum;
        rlp.topMargin = 200;
        tableScrollView.setLayoutParams(rlp);
        Log.d("enlarge",enlargeCol+"");
        for(int row=0;row<rowNum;++row){
            tableRow = new TableRow(getContext());
            //tableRow.setBackgroundColor(Color.WHITE);
            Log.d("row",row+"");
            for(int col=0;col<colNum;++col){
                TextView tv = new TextView(getContext());
                tv.setText("");
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(cellBackGroundResource);
                final int finalCol = col;
                tv.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Message msg = new Message();
                        msg.what = FOCUS_CHANGED;
                        msg.obj = finalCol;
                        handler.sendMessage(msg);
                        return false;
                    }
                });

                if(col == enlargeCol){
                    tableRow.addView(tv,new TableRow.LayoutParams(2*windowWidth/colNum,200,1));
                } else {
                    tableRow.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,200,1));
                }
            }
            tableLayout.addView(tableRow);
        }
        addItem("有聊",1,1,2,enlargeCol);//Tuesday,from class 1 to class 2
        addItem("好有聊",4,3,4,enlargeCol);//Friday, from class 3 to class 4
    }

    void drawTimeTable(){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth/colNum,windowHeight-200);
        rlp.topMargin = 200;
        rlp.leftMargin = 0;
        timeScrollView.setLayoutParams(rlp);
        for(int row=1;row<=rowNum;++row){
            Log.d("row",row+"");
            TextView tv = new TextView(getContext());
            TableRow tr = new TableRow(getContext());
            tv.setText(row+"");
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(cellBackGroundResource);
            tr.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,200,1));
            myTimeTable.addView(tr);
        }
    }

    void drawDateTable(int enlargeCol){
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(windowWidth*(1 - (1/colNum)),200);
        rlp.leftMargin = windowWidth/colNum;
        rlp.topMargin = 0;
        dateScrollView.setLayoutParams(rlp);
        TableRow tr = new TableRow(getContext());
        for(int col=0;col<colNum;++col){
            TextView tv = new TextView(getContext());
            final int finalCol = col;
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(cellBackGroundResource);

            tv.setText(Date[col]);
            Log.d("date", Date[col]);
            tv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("Touched", Date[finalCol]);
                    Message msg = new Message();
                    msg.what = FOCUS_CHANGED;
                    msg.obj = finalCol;
                    handler.sendMessage(msg);
                       return false;
                }
            });

            if(col == enlargeCol){
                tr.addView(tv,new TableRow.LayoutParams(2*windowWidth/colNum,200,1));
            } else {
                tr.addView(tv,new TableRow.LayoutParams(windowWidth/colNum,200,1));
            }
        }
        myDateTable.addView(tr);
    }

    void addItem(String text,int date,int start,int end,int enlargeCol){
        TextView info = new TextView(getContext());
        textViewVector.add(info);
        info.setText(text);
        info.setBackgroundResource(cellBackGroundResource);
        info.setGravity(Gravity.CENTER);
        info.setTextSize(12);
        int w,itemHeight,wEnlarge,itemWidth;
        w = windowWidth/colNum;
        wEnlarge = 2*windowWidth/colNum;
        itemHeight = 200;
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

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("view","a");
        super.onDraw(canvas);
        Log.d("view","b");
        //drawTable(enlargeCol);
        Log.d("view","c");
        // TODO: consider storing these as member variables to reduce
    }

//    @Override
//    protected void onMeasure(int w,int h){
//        super.onMeasure(w,h);
//    }

}
