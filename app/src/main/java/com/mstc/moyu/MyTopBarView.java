package com.mstc.moyu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
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
    private TextView weekText;
    private Button rightButton;
    private int backGroundResource;
    private String[] fiveWeekStr;
    int currentWeek;
    int popupWindowWidth = (int)getResources().getDimension(R.dimen.x250);
    int popupWindowHeight = (int)getResources().getDimension(R.dimen.y220);

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
        weekStr = "（本周）第1周";
        currentWeek = 0;
        weekText.setText(weekStr);
        weekText.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuGreen));
        weekText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y32));
        weekText.setGravity(Gravity.CENTER);
        weekText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow popupWindow = new PopupWindow(popupWindowWidth,popupWindowHeight);
                View contentView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow,null);
                popupWindow.setContentView(contentView);
                ListView listView = (ListView)contentView.findViewById(R.id.listView);
                fiveWeekStr = new String[20];
                for(int i=0;i<20;++i){
                    fiveWeekStr[i] = "第"+(i+1)+"周";
                    if(i == currentWeek){
                        fiveWeekStr[i] =  "（本周）" + fiveWeekStr[i];
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

                popupWindow.showAsDropDown(v,0,0);

            }
        });
        textParam = new LayoutParams(popupWindowWidth, LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(weekText,textParam);

        rightButton = new Button(context);
        backGroundResource = R.drawable.title_add;
        rightButton.setBackgroundResource(backGroundResource);
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddItemActivity.class);
                context.startActivity(intent);
            }
        });
        buttomParam = new LayoutParams((int)getResources().getDimension(R.dimen.x80),(int)getResources().getDimension(R.dimen.x80));
        buttomParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(rightButton,buttomParam);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
