package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {
    int repeatNumber;
    int reminderID;
    String repeatType;
    ReminderDatabase database;
    Calendar calendar;
    List<Reminder> reminderList;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            database = new ReminderDatabase(context.getApplicationContext());
            calendar = Calendar.getInstance();
            reminderList = database.getAllReminders();

            for (Reminder reminder : reminderList) {
                repeatType = reminder.getRepeatType();
                calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
                calendar.set(Calendar.MINUTE, reminder.getMinute());
                calendar.set(Calendar.DAY_OF_MONTH, reminder.getDay());
                calendar.set(Calendar.YEAR, reminder.getYear());
                calendar.set(Calendar.MONTH, reminder.getMonth());
                calendar.set(Calendar.SECOND, 0);
                if (repeatType.equals(Reminder.MONTHLY) || repeatType.equals(Reminder.YEARLY))
                    ReminderReceiver.setReminderMonthOrYear(context.getApplicationContext(), calendar.getTimeInMillis(), reminderID, repeatNumber, repeatType);
                else if (repeatType.equals(Reminder.DAILY))
                    ReminderReceiver.setReminderHourOrDayOrWeek(context.getApplicationContext(), calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_DAY);
                else if (repeatType.equals(Reminder.WEEKLY))
                    ReminderReceiver.setReminderHourOrDayOrWeek(context.getApplicationContext(), calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_DAY*7);
                else
                    ReminderReceiver.setReminderHourOrDayOrWeek(context.getApplicationContext(), calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_HOUR);
            }
        }
    }
}
