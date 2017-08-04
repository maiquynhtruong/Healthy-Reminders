package com.example.maiquynhtruong.heathyreminders.Activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public abstract class ReminderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ReminderDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final SQLiteDatabase.CursorFactory cursorFactory = new SQLiteDatabase.CursorFactory() {
        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            return null;
        }
    };

    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
