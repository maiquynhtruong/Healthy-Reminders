package com.example.maiquynhtruong.heathyreminders;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by maiquynhtruong on 8/5/17.
 */

public class ReminderApplications extends Application {
    public static ReminderDatabase dbHelper;
    public static SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new ReminderDatabase(this);
        database = dbHelper.getWritableDatabase();
    }
}
