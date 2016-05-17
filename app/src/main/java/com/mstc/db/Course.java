package com.mstc.db;

import java.util.Vector;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Course {
    public String course_name;
    public String classroom;
    public int week;
    public int day_of_week;
    public String date;
    public String time;

    public Course(String course_name,String classroom,int week,int day_of_week,String date,String time){
        this.course_name = course_name;
        this.classroom = classroom;
        this.week = week;
        this.day_of_week = day_of_week;
        this.date = date;
        this.time = time;
    }

    public static Vector<Integer> parseTimeStr(String timeStr) {
        String[] timeArray = timeStr.split(",");
        Vector<Integer> timeVector = new Vector<>();
        for (int i = 0; i < timeArray.length; i++) {
            timeVector.add(Integer.parseInt(timeArray[i]));
        }
        return timeVector;
    }
}
