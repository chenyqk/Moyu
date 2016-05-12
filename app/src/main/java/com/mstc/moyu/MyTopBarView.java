package com.mstc.moyu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
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
        weekStr = "第一周";
        textSize = 20;
        weekText.setText(weekStr);
        weekText.setTextSize(textSize);
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
