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

    public static void DeleteAffair(final DataBaseHelper dataBaseHelper,final Affair affair){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String selection =  MoyuContract.AffairEntry.DESCRIPTION + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.COMMENT + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.WEEK + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.DATE + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.TIME + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.REPEAT + " LIKE ? AND " +
                                        MoyuContract.AffairEntry.ALARM + " LIKE ?";
        String[] selectionArgs = {affair.description,
                                                    affair.comment,
                                                    String.valueOf(affair.week),
                                                    String.valueOf(affair.day_of_week),
                                                    affair.date,
                                                    affair.time,
                                                    String.valueOf(affair.repeat),
                                                    affair.alarm};

        db.delete(MoyuContract.AffairEntry.TABLE_NAME,selection,selectionArgs);
    }

    public static void DeleteCourse(final DataBaseHelper dataBaseHelper, final Course course){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String selection = MoyuContract.CourseEntry.COURSE_NAME + " LIKE ? AND " +
                                        MoyuContract.CourseEntry.CLASSROOM + " LIKE ? AND " +
                                        MoyuContract.CourseEntry.WEEK + " LIKE ? AND " +
                                        MoyuContract.CourseEntry.DAY_OF_WEEK + " LIKE ? AND " +
                                        MoyuContract.CourseEntry.DATE + " LIKE ? AND " +
                                        MoyuContract.CourseEntry.TIME + " LIKE ?";

        String[] selectionArgs = {course.course_name,
                                                    course.classroom,
                                                    String.valueOf(course.week),
                                                    String.valueOf(course.day_of_week),
                                                    course.date,
                                                    course.time};

        db.delete(MoyuContract.CourseEntry.TABLE_NAME,selection,selectionArgs);
    }

    public static void DeleteDeletedRepeatAffair(final DataBaseHelper dataBaseHelper,final Affair affair){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String selection =  MoyuContract.AffairEntry.DESCRIPTION + " LIKE ? AND " +
                MoyuContract.AffairEntry.COMMENT + " LIKE ? AND " +
                MoyuContract.AffairEntry.WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DATE + " LIKE ? AND " +
                MoyuContract.AffairEntry.TIME + " LIKE ?";

        String[] selectionArgs = {affair.description,
                affair.comment,
                String.valueOf(affair.week),
                String.valueOf(affair.day_of_week),
                affair.date,
                affair.time};

        db.delete(MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME,selection,selectionArgs);
    }

    public static int UpdateAffair(final DataBaseHelper dataBaseHelper,final Affair oldAffair,final Affair newAffair){
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoyuContract.AffairEntry.DESCRIPTION,newAffair.description);
        contentValues.put(MoyuContract.AffairEntry.COMMENT,newAffair.comment);
        contentValues.put(MoyuContract.AffairEntry.WEEK,newAffair.week);
        contentValues.put(MoyuContract.AffairEntry.DAY_OF_WEEK,newAffair.day_of_week);
        contentValues.put(MoyuContract.AffairEntry.DATE,newAffair.date);
        contentValues.put(MoyuContract.AffairEntry.TIME,newAffair.time);
        contentValues.put(MoyuContract.AffairEntry.REPEAT,newAffair.repeat);
        contentValues.put(MoyuContract.AffairEntry.ALARM,newAffair.alarm);

        String selection =  MoyuContract.AffairEntry.DESCRIPTION + " LIKE ? AND " +
                MoyuContract.AffairEntry.COMMENT + " LIKE ? AND " +
                MoyuContract.AffairEntry.WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DATE + " LIKE ? AND " +
                MoyuContract.AffairEntry.TIME + " LIKE ? AND " +
                MoyuContract.AffairEntry.REPEAT + " LIKE ? AND " +
                MoyuContract.AffairEntry.ALARM + " LIKE ?";
        String[] selectionArgs = {oldAffair.description,
                oldAffair.comment,
                String.valueOf(oldAffair.week),
                String.valueOf(oldAffair.day_of_week),
                oldAffair.date,
                oldAffair.time,
                String.valueOf(oldAffair.repeat),
                oldAffair.alarm};

        int count = db.update(MoyuContract.AffairEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        return count;
    }

    public static int UpdateCourse(final DataBaseHelper dataBaseHelper,final Course oldCourse,final Course newCourse){
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoyuContract.CourseEntry.COURSE_NAME,newCourse.course_name);
        contentValues.put(MoyuContract.CourseEntry.CLASSROOM,newCourse.classroom);
        contentValues.put(MoyuContract.CourseEntry.WEEK,newCourse.week);
        contentValues.put(MoyuContract.CourseEntry.DAY_OF_WEEK,newCourse.day_of_week);
        contentValues.put(MoyuContract.CourseEntry.DATE,newCourse.date);
        contentValues.put(MoyuContract.CourseEntry.TIME,newCourse.time);

        String selection = MoyuContract.CourseEntry.COURSE_NAME + " LIKE ? AND " +
                MoyuContract.CourseEntry.CLASSROOM + " LIKE ? AND " +
                MoyuContract.CourseEntry.WEEK + " LIKE ? AND " +
                MoyuContract.CourseEntry.DAY_OF_WEEK + " LIKE ? AND " +
                MoyuContract.CourseEntry.DATE + " LIKE ? AND " +
                MoyuContract.CourseEntry.TIME + " LIKE ?";

        String[] selectionArgs = {oldCourse.course_name,
                oldCourse.classroom,
                String.valueOf(oldCourse.week),
                String.valueOf(oldCourse.day_of_week),
                oldCourse.date,
                oldCourse.time};

        int count = db.update(MoyuContract.CourseEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        return count;
    }

    public static int UpdateDeletedRepeatAffair(final DataBaseHelper dataBaseHelper,final Affair oldAffair,final Affair newAffair){
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DESCRIPTION,newAffair.description);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.COMMENT,newAffair.comment);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.WEEK,newAffair.week);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DAY_OF_WEEK,newAffair.day_of_week);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.DATE,newAffair.date);
        contentValues.put(MoyuContract.DeletedRepeatAffairEntry.TIME,newAffair.time);

        String selection =  MoyuContract.AffairEntry.DESCRIPTION + " LIKE ? AND " +
                MoyuContract.AffairEntry.COMMENT + " LIKE ? AND " +
                MoyuContract.AffairEntry.WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DAY_OF_WEEK + " LIKE ? AND " +
                MoyuContract.AffairEntry.DATE + " LIKE ? AND " +
                MoyuContract.AffairEntry.TIME + " LIKE ?";

        String[] selectionArgs = {oldAffair.description,
                oldAffair.comment,
                String.valueOf(oldAffair.week),
                String.valueOf(oldAffair.day_of_week),
                oldAffair.date,
                oldAffair.time};

        int count = db.update(MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        return count;
    }

    public static void ClearAffairTable(final DataBaseHelper dataBaseHelper){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + MoyuContract.AffairEntry.TABLE_NAME + ";");
    }

    public static void ClearCourseTable(final DataBaseHelper dataBaseHelper){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + MoyuContract.CourseEntry.TABLE_NAME + ";");
    }

    public static void ClearDeletedRepeatAffairTable(final DataBaseHelper dataBaseHelper){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + MoyuContract.DeletedRepeatAffairEntry.TABLE_NAME + ";");
    }
}
