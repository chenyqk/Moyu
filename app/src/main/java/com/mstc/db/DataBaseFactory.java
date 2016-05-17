package com.mstc.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/5/17.
 */
public final class DataBaseFactory {
    public static void InsertAffair(final DataBaseHelper dataBaseHelper,final Affair affair){

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MoyuContract.AffairEntry.DESCRIPTION,affair.description);
        contentValues.put(MoyuContract.AffairEntry.COMMENT,affair.comment);
        contentValues.put(MoyuContract.AffairEntry.WEEK,affair.week);
        contentValues.put(MoyuContract.AffairEntry.DAY_OF_WEEK,affair.day_of_week);
        contentValues.put(MoyuContract.AffairEntry.DATE,affair.date);
        contentValues.put(MoyuContract.AffairEntry.TIME,affair.time);
        contentValues.put(MoyuContract.AffairEntry.REPEAT,affair.repeat);
        contentValues.put(MoyuContract.AffairEntry.ALARM,affair.alarm);

        db.insert(MoyuContract.AffairEntry.TABLE_NAME,null,contentValues);
    }

    public static void InsertCourse(final DataBaseHelper dataBaseHelper,final Course course){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MoyuContract.CourseEntry.COURSE_NAME,course.course_name);
        contentValues.put(MoyuContract.CourseEntry.CLASSROOM,course.classroom);
        contentValues.put(MoyuContract.CourseEntry.WEEK,course.week);
        contentValues.put(MoyuContract.CourseEntry.DAY_OF_WEEK,course.day_of_week);
        contentValues.put(MoyuContract.CourseEntry.DATE,course.date);
        contentValues.put(MoyuContract.CourseEntry.TIME,course.time);

        db.insert(MoyuContract.CourseEntry.TABLE_NAME,null,contentValues);
    }

    public static void InsertDeletedRepeatAffair(final DataBaseHelper dataBaseHelper, final Affair affair){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION,affair.description);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.COMMENT,affair.comment);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.WEEK,affair.week);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DAY_OF_WEEK,affair.day_of_week);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DATE,affair.date);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.TIME,affair.time);

        db.insert(MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME,null,contentValues);
    }
}
