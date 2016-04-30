package com.mstc.moyu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Administrator on 2016/4/9.
 */
public class GroupFragment extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_group);
    }
}
