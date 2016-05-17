package com.mstc.db;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Affair {
    public String description ;
    public String comment ;
    public int week ;
    public int day_of_week ;
    public String date  ;
    public String time ;
    public int repeat;
    public String alarm;

    public Affair(String description,String comment,int week,int day_of_week,String date,String time,int repeat,String alarm){
        this.description = description;
        this.comment = comment;
        this.week = week;
        this.day_of_week = day_of_week;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.alarm = alarm;
    }
}
