package com.example.maiquynhtruong.heathyreminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

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
                ReminderEntry.REMINDER_ID + " INTEGER PRIMARY KEY," +
                ReminderEntry.REMINDER_TITLE + " TEXT," +
                ReminderEntry.REMINDER_HOUR + " INTEGER," +
                ReminderEntry.REMINDER_MINUTE + " INTEGER," +
                ReminderEntry.REMINDER_MONTH + " INTEGER," +
                ReminderEntry.REMINDER_DAY + " INTEGER," +
                ReminderEntry.REMINDER_YEAR + " INTEGER," +
                ReminderEntry.REMINDER_REPEAT + " BOOLEAN," +
                ReminderEntry.REMINDER_REPEAT_NUMBER + " INTEGER," +
                ReminderEntry.REMINDER_REPEAT_TYPE + " TEXT" +
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
        values.put(ReminderEntry.REMINDER_TITLE, reminder.getTitle());
        values.put(ReminderEntry.REMINDER_HOUR, reminder.getHour());
        values.put(ReminderEntry.REMINDER_MINUTE, reminder.getMinute());
        values.put(ReminderEntry.REMINDER_MONTH, reminder.getMonth());
        values.put(ReminderEntry.REMINDER_DAY, reminder.getDay());
        values.put(ReminderEntry.REMINDER_YEAR, reminder.getYear());
        values.put(ReminderEntry.REMINDER_REPEAT, reminder.isRepeat());
        values.put(ReminderEntry.REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());
        values.put(ReminderEntry.REMINDER_REPEAT_TYPE, reminder.getRepeatType());
        long newRowId = database.insert(ReminderDatabase.ReminderEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public Reminder getReminder(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(ReminderDatabase.ReminderEntry.TABLE_NAME,
                new String[]{
                        ReminderEntry.REMINDER_TITLE,
                        ReminderEntry.REMINDER_HOUR,
                        ReminderEntry.REMINDER_MINUTE,
                        ReminderEntry.REMINDER_MONTH,
                        ReminderEntry.REMINDER_DAY,
                        ReminderEntry.REMINDER_YEAR,
                        ReminderEntry.REMINDER_REPEAT,
                        ReminderEntry.REMINDER_REPEAT_NUMBER,
                        ReminderEntry.REMINDER_REPEAT_TYPE
                }, ReminderDatabase.ReminderEntry.REMINDER_ID + " = ?",
                new String[]{"" + id}, null, null, null);
        Reminder reminder = new Reminder(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                Boolean.parseBoolean(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), cursor.getString(7));
        return reminder;
    }

    public List<Reminder> getAllReminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(ReminderDatabase.ReminderEntry.TABLE_NAME,
                new String[]{
                        ReminderEntry.REMINDER_TITLE,
                        ReminderEntry.REMINDER_HOUR,
                        ReminderEntry.REMINDER_MINUTE,
                        ReminderEntry.REMINDER_MONTH,
                        ReminderEntry.REMINDER_DAY,
                        ReminderEntry.REMINDER_YEAR,
                        ReminderEntry.REMINDER_REPEAT,
                        ReminderEntry.REMINDER_REPEAT_NUMBER,
                        ReminderEntry.REMINDER_REPEAT_TYPE
                }, null, null, null, null, null);
        List<Reminder> reminderList = new ArrayList<>();
        while (cursor.moveToNext()) {
            reminderList.add(new Reminder(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                    Boolean.parseBoolean(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), cursor.getString(7)));
        }
        return reminderList;
    }

    public void updateReminder(long id, Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderEntry.REMINDER_TITLE, reminder.getTitle());
        values.put(ReminderEntry.REMINDER_HOUR, reminder.getHour());
        values.put(ReminderEntry.REMINDER_MINUTE, reminder.getMinute());
        values.put(ReminderEntry.REMINDER_MONTH, reminder.getMonth());
        values.put(ReminderEntry.REMINDER_DAY, reminder.getDay());
        values.put(ReminderEntry.REMINDER_YEAR, reminder.getYear());
        values.put(ReminderEntry.REMINDER_REPEAT, reminder.isRepeat());
        values.put(ReminderEntry.REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());
        values.put(ReminderEntry.REMINDER_REPEAT_TYPE, reminder.getRepeatType());
        database.update(ReminderDatabase.ReminderEntry.TABLE_NAME, values, REMINDER_ID + " = ?", new String[] {"" + reminder.getId()});
    }

    public void deleteReminder(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, ReminderEntry.REMINDER_ID + " = ?", new String[] {"" + id});
    }

    public static class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "ReminderEntries";
        public static final String REMINDER_ID = "id";
        public static final String REMINDER_TITLE = "title";
        public static final String REMINDER_HOUR = "hour";
        public static final String REMINDER_MINUTE = "minute";
        public static final String REMINDER_MONTH = "month";
        public static final String REMINDER_DAY = "day";
        public static final String REMINDER_YEAR = "year";
        public static final String REMINDER_REPEAT = "repeat";
        public static final String REMINDER_REPEAT_NUMBER = "repeat_number";
        public static final String REMINDER_REPEAT_TYPE = "repeat_type";
    }
}
