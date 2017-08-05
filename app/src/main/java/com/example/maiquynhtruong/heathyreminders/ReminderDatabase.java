package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.provider.BaseColumns;

import static com.example.maiquynhtruong.heathyreminders.ReminderDatabase.ReminderEntry.TABLE_NAME;

public class ReminderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ReminderDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ReminderEntry.TABLE_NAME + " ("
    + ReminderEntry._ID + " INTEGER PRIMARY KEY, " + ReminderEntry.NOTIICATION_ID + " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ReminderEntry.TABLE_NAME;
    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public static class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "ReminderEntries";
        public static final String NOTIICATION_ID = "Notification-ID";
    }
}
