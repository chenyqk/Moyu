package com.mstc.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mstc.moyu.AddItemActivity;
import com.mstc.moyu.MainActivity;
import com.mstc.moyu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TODO: document your custom view class.
 */
public class MyTopBarView extends RelativeLayout {

    private SpannableString weekStr;
    private TextView weekText;
    private TextView currentWeekText;
    private Button rightButton;
    private ImageView imageView;
    private int backGroundResource;
    private SpannableString[] fiveWeekStr;
    private OnClickListener onClickListener;
    int currentWeek;
    int popupWindowWidth = (int)getResources().getDimension(R.dimen.x254);
    int popupWindowHeight = (int)getResources().getDimension(R.dimen.y326);

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

        weekText = new TextView(context);
        currentWeekText = new TextView(context);
        weekStr = new SpannableString("第"+(currentWeek+1)+"周");
        weekStr.setSpan(new TextAppearanceSpan(getContext(),R.style.PopupWindowStyle2),0,weekStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //currentWeek = 0;
        final View contentView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow,null);
        final PopupWindow popupWindow = new PopupWindow(contentView,popupWindowWidth,popupWindowHeight);
        fiveWeekStr = new SpannableString[20];
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.invisible));
                ListView listView = (ListView)contentView.findViewById(R.id.listView);
                imageView.setImageResource(R.drawable.title_more_reverse);
                for(int i=0;i<20;++i){
                    if(i == currentWeek){
                        fiveWeekStr[i] = new SpannableString("（本周）第"+(i+1)+"周");
                        fiveWeekStr[i].setSpan(new TextAppearanceSpan(getContext(),R.style.PopupWindowStyle4),0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        fiveWeekStr[i].setSpan(new TextAppearanceSpan(getContext(),R.style.PopupWindowStyle5),4,fiveWeekStr[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        if(i < 9){
                            fiveWeekStr[i] = new SpannableString("            第"+(i+1)+"周");
                        } else {
                            fiveWeekStr[i] = new SpannableString("           第"+(i+1)+"周");
                        }

                        fiveWeekStr[i].setSpan(new TextAppearanceSpan(getContext(),R.style.PopupWindowStyle5),0,fiveWeekStr[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }
                ArrayAdapter<SpannableString> adapter = new ArrayAdapter<SpannableString>(context,R.layout.list_item,fiveWeekStr);
                listView.setAdapter(adapter);
                listView.setBackgroundResource(R.drawable.round_corner);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        weekText.setText("第"+(position+1)+"周");
                        if(position == currentWeek){
                            currentWeekText.setVisibility(VISIBLE);
                        } else {
                            currentWeekText.setVisibility(INVISIBLE);
                        }

                        Intent intent = new Intent("SHOW_WEEK_CHANGED");
                        intent.putExtra("SHOW_WEEK",position);
                        context.sendBroadcast(intent);
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAsDropDown(currentWeekText,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imageView.setImageResource(R.drawable.title_more);
                    }
                });
            }
        };

        weekText.setText(weekStr);
        weekText.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuGreen));
        weekText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y32));
        weekText.setGravity(Gravity.CENTER);
        weekText.setOnClickListener(onClickListener);
        textParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        weekText.setId(generateViewId());
        addView(weekText,textParam);

        currentWeekText.setText("（本周）");
        currentWeekText.setTextColor(ContextCompat.getColor(getContext(),R.color.MoyuGreen));
        currentWeekText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.y24));
        currentWeekText.setOnClickListener(onClickListener);
        weekText.setVisibility(VISIBLE);
        textParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.LEFT_OF,weekText.getId());
        textParam.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(currentWeekText,textParam);

        imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.title_more);
        imageView.setOnClickListener(onClickListener);
        imageParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Log.d("weekText",weekText.getId()+"");
        imageParam.addRule(RelativeLayout.RIGHT_OF,weekText.getId());
        imageParam.leftMargin = (int)getResources().getDimension(R.dimen.x26);
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