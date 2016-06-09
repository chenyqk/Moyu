package com.mstc.db;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/5/17.
 */
public final class MoyuContract {
    public MoyuContract() {}
    public static abstract class AffairEntry implements BaseColumns{
        public static final String TABLE_NAME = "affair";
        public static final String DESCRIPTION = "description";
        public static final String COMMENT = "comment";
        public static final String WEEK = "week";
        public static final String DAY_OF_WEEK = "day_of_week";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String REPEAT = "repeat";
        public static final String ALARM = "alarm";
    }

    public static abstract class CourseEntry implements BaseColumns{
        public static final String TABLE_NAME = "class";
        public static final String COURSE_NAME = "course_name";
        public static final String CLASSROOM = "classroom";
        public static final String WEEK = "week";
        public static final String DAY_OF_WEEK = "day_of_week";
        public static final String DATE = "date";
        public static final String TIME = "time";
    }

    public static abstract class DeletedRepeatAffairEntry implements BaseColumns{
        public static final String TABLE_NAME = "deleted_repeat_affair";
        public static final String DESCRIPTION = "description";
        public static final String COMMENT = "comment";
        public static final String WEEK = "week";
        public static final String DAY_OF_WEEK = "day_of_week";
        public static final String DATE = "date";
        public static final String TIME = "time";
    }
}
