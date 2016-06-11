package com.mstc.moyu;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class MainActivity extends ActivityGroup {

    private TabHost tabHost = null;
    public static final String firstWeekMonStr = "2016-05-23";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup(getLocalActivityManager());

        initeTabhost(this,"ScheduleFragment",R.color.MoyuWhite,
                R.drawable.date1,new Intent(MainActivity.this,ScheduleFragment.class),tabHost);
        initeTabhost(this,"GroupFragment",R.color.MoyuWhite,
                R.drawable.me,new Intent(MainActivity.this,GroupFragment.class),tabHost);
        initeTabhost(this,"AboutmeFragment",R.color.MoyuWhite,
                R.drawable.people,new Intent(MainActivity.this,AboutmeFragment.class),tabHost);

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabHost.getCurrentTab()){
                    case 0:{
                        ImageView tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.date1);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.me);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(2).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.people);
                        break;
                    }
                    case 1:{
                        ImageView tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.date);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.me1);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(2).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.people);
                        break;
                    }
                    case 2:{
                        ImageView tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.date);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.me);
                        tmp1 = (ImageView) tabHost.getTabWidget().getChildAt(2).findViewById(R.id.icon);
                        tmp1.setImageResource(R.drawable.people1);
                        break;
                    }
                }
            }
        });

    }

    /**
     * initalize the tab
     * @param context
     * @param tabId
     * @param backgroundId
     * @param intent
     * @param tabHost
     */
    private void initeTabhost(Context context, String tabId, int backgroundId, int iconId, Intent intent, TabHost tabHost){
        LinearLayout tab=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.labelview, null);
        ImageView icon1=(ImageView)tab.findViewById(R.id.icon);
        icon1.setImageResource(iconId);
        tab.setBackgroundResource(backgroundId);
        TabHost.TabSpec ts1 = tabHost.newTabSpec(tabId);
        ts1.setIndicator(tab);//set the content of each tab
        ts1.setContent(intent);
        tabHost.addTab(ts1);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
