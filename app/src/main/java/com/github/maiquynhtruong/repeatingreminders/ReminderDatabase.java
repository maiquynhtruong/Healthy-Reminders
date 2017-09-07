package com.github.maiquynhtruong.repeatingreminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.github.maiquynhtruong.repeatingreminders.ReminderDatabase.ReminderEntry.REMINDER_ID;
import static com.github.maiquynhtruong.repeatingreminders.ReminderDatabase.ReminderEntry.TABLE_NAME;

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
                ReminderEntry.REMINDER_ID + " INTEGER PRIMARY KEY," +
                ReminderEntry.REMINDER_TITLE + " TEXT," +
                ReminderEntry.REMINDER_HOUR + " INTEGER," +
                ReminderEntry.REMINDER_MINUTE + " INTEGER," +
                ReminderEntry.REMINDER_MONTH + " INTEGER," +
                ReminderEntry.REMINDER_DAY + " INTEGER," +
                ReminderEntry.REMINDER_YEAR + " INTEGER," +
                ReminderEntry.REMINDER_REPEAT_NUMBER + " INTEGER," +
                ReminderEntry.REMINDER_REPEAT_TYPE + " TEXT," +
                ReminderEntry.REMINDER_COLOR + " TEXT " +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
    public long setReminder(Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.i("ReminderDatabase", "setReminder() add reminder " + reminder.getTitle() + ", " +
                reminder.getHour() + ":" + reminder.getMinute() + ", " + reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear() +
                ", " + reminder.getRepeatType() + ", and color " + reminder.getColor());
        values.put(ReminderEntry.REMINDER_TITLE, reminder.getTitle());
        values.put(ReminderEntry.REMINDER_HOUR, reminder.getHour());
        values.put(ReminderEntry.REMINDER_MINUTE, reminder.getMinute());
        values.put(ReminderEntry.REMINDER_MONTH, reminder.getMonth());
        values.put(ReminderEntry.REMINDER_DAY, reminder.getDay());
        values.put(ReminderEntry.REMINDER_YEAR, reminder.getYear());
        values.put(ReminderEntry.REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());
        values.put(ReminderEntry.REMINDER_REPEAT_TYPE, reminder.getRepeatType());
        values.put(ReminderEntry.REMINDER_COLOR, reminder.getColor());
        long reminderID = database.insert(ReminderDatabase.ReminderEntry.TABLE_NAME, null, values);
        return reminderID;
    }

    public Reminder getReminder(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(ReminderDatabase.ReminderEntry.TABLE_NAME,
                new String[]{
                        ReminderEntry.REMINDER_ID,
                        ReminderEntry.REMINDER_TITLE,
                        ReminderEntry.REMINDER_HOUR,
                        ReminderEntry.REMINDER_MINUTE,
                        ReminderEntry.REMINDER_MONTH,
                        ReminderEntry.REMINDER_DAY,
                        ReminderEntry.REMINDER_YEAR,
                        ReminderEntry.REMINDER_REPEAT_NUMBER,
                        ReminderEntry.REMINDER_REPEAT_TYPE,
                        ReminderEntry.REMINDER_COLOR
                }, ReminderDatabase.ReminderEntry.REMINDER_ID + " = ?",
                new String[]{"" + id}, null, null, null);
        Reminder reminder = null;
        if (cursor != null && cursor.moveToFirst()) {
            int reminder_id = Integer.parseInt(cursor.getString(0));
            String title = cursor.getString(1);
            int hour = Integer.parseInt(cursor.getString(2));
            int minute = Integer.parseInt(cursor.getString(3));
            int month = Integer.parseInt(cursor.getString(4));
            int day = Integer.parseInt(cursor.getString(5));
            int year = Integer.parseInt(cursor.getString(6));
            int repeatNumber = Integer.parseInt(cursor.getString(7));
            String repeatType = cursor.getString(8);
            int color = Integer.parseInt(cursor.getString(9));
            reminder = new Reminder(reminder_id, title, hour, minute, month, day, year, repeatNumber, repeatType, color);
            Log.i("ReminderDatabase", "getReminder() successful with " + reminder.getTitle() + ", " +
                    reminder.getHour() + ":" + reminder.getMinute() + ", " + reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear() +
                            ", " + reminder.getRepeatType() + ", and " + reminder.getColor());
            cursor.close();
        } else {
            Log.i("ReminderDatabase", "getReminder() failed from id " + id + " since cursor is null!!");
        }
        return reminder;
    }

    public List<Reminder> getAllReminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + ReminderEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        List<Reminder> reminderList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int reminder_id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                int hour = Integer.parseInt(cursor.getString(2));
                int minute = Integer.parseInt(cursor.getString(3));
                int month = Integer.parseInt(cursor.getString(4));
                int day = Integer.parseInt(cursor.getString(5));
                int year = Integer.parseInt(cursor.getString(6));
                int repeatNumber = Integer.parseInt(cursor.getString(7));
                String repeatType = cursor.getString(8);
                int color = Integer.parseInt(cursor.getString(9));
                Reminder reminder = new Reminder(reminder_id, title, hour, minute, month, day, year, repeatNumber, repeatType, color);
                reminderList.add(reminder);
                Log.i("ReminderDatabase", "getAllReminders() add reminder " + reminder.getId() + ", " + reminder.getTitle() + ", " +
                reminder.getHour() + ":" + reminder.getMinute() + ", " + reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear() +
                ", every " + reminder.getRepeatType() + ", and " + reminder.getColor());
            } while (cursor.moveToNext());
        }
        return reminderList;
    }

    public void updateReminder(Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderEntry.REMINDER_TITLE, reminder.getTitle());
        values.put(ReminderEntry.REMINDER_HOUR, reminder.getHour());
        values.put(ReminderEntry.REMINDER_MINUTE, reminder.getMinute());
        values.put(ReminderEntry.REMINDER_MONTH, reminder.getMonth());
        values.put(ReminderEntry.REMINDER_DAY, reminder.getDay());
        values.put(ReminderEntry.REMINDER_YEAR, reminder.getYear());
        values.put(ReminderEntry.REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());
        values.put(ReminderEntry.REMINDER_REPEAT_TYPE, reminder.getRepeatType());
        values.put(ReminderEntry.REMINDER_COLOR, reminder.getColor());
        database.update(ReminderDatabase.ReminderEntry.TABLE_NAME, values, REMINDER_ID + " = ?", new String[] {"" + reminder.getId()});
        Log.i("ReminderDatabase", "updateReminder() with reminder title " + reminder.getTitle() + " and id " + reminder.getId());
    }

    public void deleteAllReminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }
    public void deleteReminder(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, ReminderEntry.REMINDER_ID + " = ?", new String[] {"" + id});
        database.close();
    }

    static class ReminderEntry implements BaseColumns {
        static final String TABLE_NAME = "ReminderTable";
        static final String REMINDER_ID = "id";
        static final String REMINDER_TITLE = "title";
        static final String REMINDER_HOUR = "hour";
        static final String REMINDER_MINUTE = "minute";
        static final String REMINDER_MONTH = "month";
        static final String REMINDER_DAY = "day";
        static final String REMINDER_YEAR = "year";
        static final String REMINDER_REPEAT_NUMBER = "repeat_number";
        static final String REMINDER_REPEAT_TYPE = "repeat_type";
        static final String REMINDER_COLOR = "color";
    }
}
