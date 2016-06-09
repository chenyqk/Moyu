package com.mstc.moyu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mstc.db.Affair;
import com.mstc.db.Course;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Intent intent = getIntent();
        Boolean isCourse = intent.getBooleanExtra("IS_COURSE",true);
        if(isCourse){
            Course course = (Course) intent.getSerializableExtra("COURSE");
            if(course != null){
                Toast.makeText(getApplicationContext(),"course name : " + course.course_name,Toast.LENGTH_LONG).show();
            }
        } else {
            Affair affair = (Affair) intent.getSerializableExtra("AFFAIR");
            if(affair != null){
                Toast.makeText(getApplicationContext(),"affair description : " + affair.description,Toast.LENGTH_LONG).show();
            }
        }
    }
}
