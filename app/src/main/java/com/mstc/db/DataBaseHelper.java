package com.mstc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/17.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "event.db";

    private static final String SQL_CREATE_AFFAIR = "CREATE TABLE " + MoyuContract.AffairEntry.TABLE_NAME  + " (" +
            MoyuContract.AffairEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoyuContract.AffairEntry.DESCRIPTION + " TEXT, " +
            MoyuContract.AffairEntry.COMMENT + " TEXT," +
            MoyuContract.AffairEntry.WEEK + " INTEGER, " +
            MoyuContract.AffairEntry.DAY_OF_WEEK + " INTEGER," +
            MoyuContract.AffairEntry.DATE + " TEXT, " +
            MoyuContract.AffairEntry.TIME + " TEXT, " +
            MoyuContract.AffairEntry.REPEAT + " INTEGER, " +
            MoyuContract.AffairEntry.ALARM + " TEXT);";

    private  static String SQL_CREATE_COURSE = "CREATE TABLE " + MoyuContract.CourseEntry.TABLE_NAME + " (" +
            MoyuContract.CourseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoyuContract.CourseEntry.COURSE_NAME + " TEXT, " +
            MoyuContract.CourseEntry.CLASSROOM + " TEXT," +
            MoyuContract.CourseEntry.WEEK + " INTEGER, " +
            MoyuContract.CourseEntry.DAY_OF_WEEK + " INTEGER, " +
            MoyuContract.CourseEntry.DATE + " TEXT, " +
            MoyuContract.CourseEntry.TIME + " TEXT);";

    private static String SQL_CREATE_DELETED_REPEAT_AFFAIR = "CREATE TABLE " + MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME + " (" +
            MoyuContract.DeletedRepeatAffairEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION  + " TEXT, " +
            MoyuContract.DeletedRepeatAffairEntry.COMMENT + " TEXT, " +
            MoyuContract.DeletedRepeatAffairEntry.WEEK + " INTEGER, " +
            MoyuContract.DeletedRepeatAffairEntry.DAY_OF_WEEK + " INTEGER, " +
            MoyuContract.DeletedRepeatAffairEntry.DATE + " TEXT, " +
            MoyuContract.DeletedRepeatAffairEntry.TIME + " TEXT);";

    private static String SQL_DELETE_AFFAIR_ENTRIES = "DROP TABLE IF EXISTS " + MoyuContract.AffairEntry.TABLE_NAME;
    private static String SQL_DELETE_COURSE_ENTRIES = "DROP TABLE IF EXISTS " + MoyuContract.CourseEntry.TABLE_NAME;
    private static String SQL_DELETE_DELETED_REPEAT_AFFAIR = "DROP TABLE IF EXISTS " + MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME;

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AFFAIR);
        db.execSQL(SQL_CREATE_COURSE);
        db.execSQL(SQL_CREATE_DELETED_REPEAT_AFFAIR);
        Log.d("SQL","create database------------------->");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AFFAIR_ENTRIES);
        db.execSQL(SQL_DELETE_COURSE_ENTRIES);
        db.execSQL(SQL_DELETE_DELETED_REPEAT_AFFAIR);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
