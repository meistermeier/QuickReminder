package com.meistermeier.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 */
public class TaskDBOpenHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "tasks";
    private final static int DB_VERSION = 1;
    private final static String CREATE_QUERY = "CREATE TABLE " +
            DB_NAME +
            " ("+TaskItem.ID_FIELD+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskItem.NAME_FIELD+" TEXT, " +
            TaskItem.TIMESTAMP_FIELD +" INTEGER, " +
            TaskItem.REMINDER_FIELD+" INTEGER)";

    public TaskDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        sqLiteDatabase.execSQL(CREATE_QUERY);

    }
}
