package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import static com.example.maiquynhtruong.heathyreminders.ReminderDetailsActivity.REMINDER_DETAILS_ID;

public class ReminderReceiver extends BroadcastReceiver {
    public static final int REMINDER_PENDING_INTENT_ID = 2;
    public static final int FINISH_NOTIFICATION_PENDING_INTENT = 3;
    public static final int POSTPONE_NOTIFICATION_PENDING_INTENT = 4;
    public static final String REMINDER_REPEAT_TYPE = "ReminderType";
    public static final String REMINDER_TIME_MILLIS = "ReminderMillis";
    public static final String TAG = "ReminderDetailsActivity";
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(REMINDER_REPEAT_TYPE);
        int millis = intent.getIntExtra(REMINDER_TIME_MILLIS, 0);
        int reminderId = intent.getIntExtra(REMINDER_DETAILS_ID, 0);

        Log.i("Receiver-onReceived", "Received reminder with ID " + reminderId);
        // Get reminder from database
        ReminderDatabase database = new ReminderDatabase(context);
        Reminder reminder = database.getReminder(reminderId);
        createNotification(context, reminder); // start notification

        // if the reminder is repeating yearly or monthly we have to set a new one
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        if (type.equals(Reminder.YEARLY)) {
            calendar.add(Calendar.YEAR, 1); // add a year to the clock
            Log.i("ReminderReceiver", "onReceive() Added one year. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.YEARLY);
        } else if (type.equals(Reminder.MONTHLY)) {
            calendar.add(Calendar.MONTH, 1); // add a month to the clock
            Log.i("ReminderReceiver", "onReceive() Added one month. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.MONTHLY);
        }
    }

    public static void setReminderMonthOrYear(Context context, long timeInMillis, int reminderID, String repeatType) {
        Log.i("ReminderReceiver", "setReminderMonthOrYear() Reminder set with id: " + reminderID);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(REMINDER_REPEAT_TYPE, repeatType);
        intent.putExtra(REMINDER_TIME_MILLIS, timeInMillis);
        intent.putExtra(REMINDER_DETAILS_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void setReminderHourOrDayOrWeek(Context context, long timeInMillis, int reminderID, long interval) {
        Log.i("ReminderReceiver", "setReminderHourOrDayOrWeek() Reminder set with id: " + reminderID);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(REMINDER_REPEAT_TYPE, "null");
        intent.putExtra(REMINDER_TIME_MILLIS, timeInMillis);
        intent.putExtra(REMINDER_DETAILS_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                        2 * 1000, pendingIntent);
        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void cancelAlarm(Context context, int reminderID) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = PendingIntent.getBroadcast(context, reminderID, new Intent(context, ReminderReceiver.class), 0);
        manager.cancel(intent);

        // disable automatic alarm restart
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void createNotification(Context context, Reminder reminder) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(largeIcon(context))
                .setTicker(reminder.getTitle())
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.reminder_notification_title) + reminder.getTitle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context, reminder.getId())) // supply PendingIntent to send when the notification is clicked
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(postponeReminder(context, reminder.getId()))
                .addAction(finishReminder(context, reminder.getId()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(REMINDER_PENDING_INTENT_ID, builder.build());
    }

    // should show the reminder that is showed in the notification
    public static PendingIntent contentIntent(Context context, int reminderID) {
        Intent intent = new Intent(context, ReminderDetailsActivity.class);
        intent.putExtra(REMINDER_DETAILS_ID, reminderID);
        Log.i("ReminderReceiver", "contentIntent is pasisng id " + reminderID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reminderID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        clearAllNotifications(context);
        return pendingIntent;
    }

    public static NotificationCompat.Action finishReminder(Context context, int reminderID) {
        Intent finishIntent = new Intent(context, ReminderDetailsActivity.class);
        finishIntent.putExtra(REMINDER_DETAILS_ID, reminderID);
        Log.i("finishedReminderAction", "Called with ID " + reminderID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, FINISH_NOTIFICATION_PENDING_INTENT,
                finishIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_done_black_24px, context.getText(R.string.reminder_finished),  pendingIntent);
        clearAllNotifications(context);
        return action;
    }

    public static NotificationCompat.Action postponeReminder(Context context, int reminderID) {
        Intent postponeReminder  = new Intent(context, ReminderDetailsActivity.class);
        Log.i("postponeReminder", "Called with ID " + reminderID);
        postponeReminder.putExtra(REMINDER_DETAILS_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, POSTPONE_NOTIFICATION_PENDING_INTENT,
                postponeReminder, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px, context.getString(R.string.reminder_postponed), pendingIntent);
        clearAllNotifications(context);
        return action;
    }

    public static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_alarm);
        return bitmap;
    }
}
