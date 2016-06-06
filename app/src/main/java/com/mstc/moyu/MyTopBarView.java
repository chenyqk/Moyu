package com.mstc.moyu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView imageView;
    private int backGroundResource;
    private String[] fiveWeekStr;
    private OnClickListener onClickListener;
    int currentWeek;
    int popupWindowWidth = (int)getResources().getDimension(R.dimen.x254);
    int popupWindowHeight = (int)getResources().getDimension(R.dimen.y288);

    private LayoutParams textParam,buttomParam,imageParam;

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

        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow popupWindow = new PopupWindow(popupWindowWidth,popupWindowHeight);
                View contentView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow,null);
                popupWindow.setContentView(contentView);
                ListView listView = (ListView)contentView.findViewById(R.id.listView);
                imageView.setImageResource(R.drawable.title_more);
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

                popupWindow.showAsDropDown(weekText,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imageView.setImageResource(R.drawable.title_more_reverse);
                    }
                });
            }
        };

        weekText.setText(weekStr);
        weekText.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuGreen));
        weekText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y32));
        weekText.setGravity(Gravity.CENTER);
        weekText.setOnClickListener(onClickListener);
        textParam = new LayoutParams(popupWindowWidth, LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        weekText.setId(generateViewId());
        addView(weekText,textParam);

        imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.title_more_reverse);
        imageView.setOnClickListener(onClickListener);
        imageParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Log.d("weekText",weekText.getId()+"");
        imageParam.addRule(RelativeLayout.RIGHT_OF,weekText.getId());
        imageParam.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(imageView,imageParam);

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
        buttomParam = new LayoutParams((int)getResources().getDimension(R.dimen.x40),(int)getResources().getDimension(R.dimen.x40));
        buttomParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttomParam.addRule(RelativeLayout.CENTER_VERTICAL);
        buttomParam.rightMargin = (int)getResources().getDimension(R.dimen.x42);
        addView(rightButton,buttomParam);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}