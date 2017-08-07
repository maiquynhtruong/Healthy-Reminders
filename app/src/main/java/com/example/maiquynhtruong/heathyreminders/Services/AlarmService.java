package com.example.maiquynhtruong.heathyreminders.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;

import com.example.maiquynhtruong.heathyreminders.Receivers.AlarmReceiver;
import com.example.maiquynhtruong.heathyreminders.Receivers.BootBroadcastReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderApplication;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import java.util.ArrayList;
import java.util.List;


public class AlarmService extends IntentService {
    public static final String CREATE_ALARM = "create-alarm";
    public static final String DELETE_ALARM = "delete-alarm";
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
    }

    public static void setReminder(Context context, Reminder reminder, Calendar calendar) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        ContentValues values = new ContentValues();
        values.put(ReminderDatabase.ReminderEntry.REMINDER_TITLE, reminder.getTitle());
        values.put(ReminderDatabase.ReminderEntry.REMINDER_DATE, reminder.getDate());
        values.put(ReminderDatabase.ReminderEntry.REMINDER_HOUR, reminder.getHour());
        values.put(ReminderDatabase.ReminderEntry.REMINDER_MINUTE, reminder.getMinute());
        values.put(ReminderDatabase.ReminderEntry.REMINDER_REPEAT, reminder.isRepeat());
        values.put(ReminderDatabase.ReminderEntry.REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());

        long newRowId = ReminderApplication.database.insert(ReminderDatabase.ReminderEntry.TABLE_NAME, null, values);

        Intent intent = new Intent(context, AlarmReceiver.class);
        // Retrieve a PendingIntent that will perform a broadcast, like calling Context.sendBroadcast()
        // sendBroadcast() will broadcast the Intent; all receivers matching this Intent will receive the broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static Reminder getReminder(long id) {
        Cursor cursor = ReminderApplication.database.query(ReminderDatabase.ReminderEntry.TABLE_NAME,
                new String[] {
                        ReminderDatabase.ReminderEntry.REMINDER_TITLE,
                        ReminderDatabase.ReminderEntry.REMINDER_DATE,
                        ReminderDatabase.ReminderEntry.REMINDER_HOUR,
                        ReminderDatabase.ReminderEntry.REMINDER_MINUTE,
                        ReminderDatabase.ReminderEntry.REMINDER_REPEAT,
                        ReminderDatabase.ReminderEntry.REMINDER_REPEAT_NUMBER,
                }, ReminderDatabase.ReminderEntry.REMINDER_ID + " = ?",
                new String[] {"" + id}, null, null, null);
        Reminder reminder = new Reminder("Default reminder");
        if (cursor.moveToFirst()) {
            reminder.setTitle(cursor.getString(1));
            reminder.setDate(cursor.getString(2));
            reminder.setHour(Integer.parseInt(cursor.getString(3)));
            reminder.setMinute(Integer.parseInt(cursor.getString(4)));
            reminder.setRepeat(Boolean.parseBoolean(cursor.getString(5)));
            reminder.setRepeatNumber(Integer.parseInt(cursor.getString(6)));
        }
        return reminder;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<Reminder>();
    }
    }
    public void cancelReminder(Context context, int requestCode) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}

