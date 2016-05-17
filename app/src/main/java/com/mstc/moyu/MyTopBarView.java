package com.mstc.moyu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class MyTopBarView extends RelativeLayout {

    private String weekStr;
    private float textSize;
    private TextView weekText;
    private Button rightButton;
    private int backGroundResource;
    private String[] fiveWeekStr;
    int currentWeek;

    private LayoutParams textParam,buttomParam;

    public MyTopBarView(Context context) {
        super(context);
        init(context);
    }

    public MyTopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTopBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        weekText = new TextView(context);
        weekStr = "第1周（本周）";
        currentWeek = 0;
        textSize = 20;
        weekText.setText(weekStr);
        weekText.setTextSize(textSize);
        weekText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow popupWindow = new PopupWindow(360,270);
                View contentView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow,null);
                popupWindow.setContentView(contentView);
//                TextView setCurrentWeek = (TextView)contentView.findViewById(R.id.setCurrentWeek);
//                setCurrentWeek.setText("设置当前周");
//                setCurrentWeek.setGravity(Gravity.CENTER);
//                setCurrentWeek.setTextSize(textSize);
//                setCurrentWeek.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        /**
//                         *  TODO: add set current week dialog
//                         */
//
//                    }
//                });
                ListView listView = (ListView)contentView.findViewById(R.id.listView);
                fiveWeekStr = new String[20];
                for(int i=0;i<20;++i){
                    fiveWeekStr[i] = "第"+(i+1)+"周";
                    if(i == currentWeek){
                        fiveWeekStr[i] = fiveWeekStr[i] + "（本周）";
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.list_item,fiveWeekStr);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        weekText.setText(fiveWeekStr[position]);
                        Intent intent = new Intent("SHOW_WEEK_CHANGED");
                        intent.putExtra("SHOW_WEEK",position);
                        context.sendBroadcast(intent);
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(v,-20,0);

            }
        });
        textParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(weekText,textParam);

        rightButton = new Button(context);
        backGroundResource = R.mipmap.ic_launcher;
        rightButton.setBackgroundResource(backGroundResource);
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddItemActivity.class);
                context.startActivity(intent);
            }
        });
        buttomParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttomParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(rightButton,buttomParam);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
