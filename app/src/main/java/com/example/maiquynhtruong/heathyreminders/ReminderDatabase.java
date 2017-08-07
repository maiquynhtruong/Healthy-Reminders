package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.provider.BaseColumns;

import static com.example.maiquynhtruong.heathyreminders.ReminderDatabase.ReminderEntry.REMINDER_DATE;
import static com.example.maiquynhtruong.heathyreminders.ReminderDatabase.ReminderEntry.REMINDER_ID;
import static com.example.maiquynhtruong.heathyreminders.ReminderDatabase.ReminderEntry.TABLE_NAME;

public class ReminderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ReminderDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ReminderEntry.TABLE_NAME;


    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" +
                ReminderEntry.REMINDER_ID + " INTEGER PRIMARY KEY, " +
                ReminderEntry.REMINDER_TITLE + " TEXT, " +
                ReminderEntry.REMINDER_DATE + " TEXT, " +
                ReminderEntry.REMINDER_HOUR + " INTEGER, " +
                ReminderEntry.REMINDER_MINUTE + " INTEGER, " +
                ReminderEntry.REMINDER_REPEAT + " BOOLEAN, " +
                ReminderEntry.REMINDER_REPEAT_NUMBER + " INTEGER" +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
    public static class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "ReminderEntries";
        public static final String REMINDER_ID = "id";
        public static final String REMINDER_TITLE = "title";
        public static final String REMINDER_HOUR = "hour";
        public static final String REMINDER_MINUTE = "minute";
        public static final String REMINDER_DATE = "date";
        public static final String REMINDER_REPEAT = "repeat";
        public static final String REMINDER_REPEAT_NUMBER = "repeat-number";
    }
}
