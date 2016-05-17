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
import android.widget.TextView;

public class MainActivity extends ActivityGroup {

    private TabHost tabHost = null;
    private Intent dbService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup(getLocalActivityManager());

        initeTabhost(this,"ScheduleFragment",R.string.title_schedule_fragment_activity,R.color.button_material_light,
                R.mipmap.ic_launcher,new Intent(MainActivity.this,ScheduleFragment.class),tabHost);
        initeTabhost(this,"GroupFragment",R.string.title_group_fragment_activity,R.color.button_material_light,
                R.mipmap.ic_launcher,new Intent(MainActivity.this,GroupFragment.class),tabHost);
        initeTabhost(this,"AboutmeFragment",R.string.title_aboutme_fragment_activity,R.color.button_material_light,
                R.mipmap.ic_launcher,new Intent(MainActivity.this,AboutmeFragment.class),tabHost);

        tabHost.setCurrentTab(0);

        //dbService = new Intent(MainActivity.this,DataBaseService.class);
        //startService(dbService);
    }

    /**
     * initalize the tab
     * @param context
     * @param tabId
     * @param name
     * @param backgroundId
     * @param intent
     * @param tabHost
     */
    private void initeTabhost(Context context, String tabId, int name, int backgroundId, int iconId, Intent intent, TabHost tabHost){
        LinearLayout tab=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.labelview, null);
        ImageView icon1=(ImageView)tab.findViewById(R.id.icon);
        icon1.setBackgroundResource(iconId);
        TextView title1=(TextView)tab.findViewById(R.id.title);
        title1.setText(getString(name));
        tab.setBackgroundResource(backgroundId);
        TabHost.TabSpec ts1 = tabHost.newTabSpec(tabId);
        ts1.setIndicator(tab);//set the content of each tab
        ts1.setContent(intent);
        tabHost.addTab(ts1);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        if(dbService != null){
//            stopService(dbService);
//        }
    }
}
