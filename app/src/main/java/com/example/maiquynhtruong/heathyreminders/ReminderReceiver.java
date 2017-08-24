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
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

        // Get reminder from database
        ReminderDatabase database = new ReminderDatabase(context);
        Reminder reminder = database.getReminder(reminderId);
        createNotification(context, reminder); // start notification

        // if the reminder is repeating yearly or monthly we have to set a new one
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        if (type.equals(Reminder.YEARLY)) {
            calendar.add(Calendar.YEAR, 1); // add a year to the clock
            Log.i(TAG, "Added one year. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.YEARLY);
        } else if (type.equals(Reminder.MONTHLY)) {
            calendar.add(Calendar.MONTH, 1); // add a month to the clock
            Log.i(TAG, "Added one month. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.MONTHLY);
        }
    }

    public static void setReminderMonthOrYear(Context context, long timeInMillis, int reminderID, String repeatType) {
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
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.HOUR/60, interval, pendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int reminderID) {
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
                .addAction(postponeReminder(context))
                .addAction(finishReminderAction(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(REMINDER_PENDING_INTENT_ID, builder.build());
    }

    // should show the reminder that is showed in the notification
    public static PendingIntent contentIntent(Context context, long reminderID) {
        Toast.makeText(context, "Notificaiton clicked, opening reminder", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ReminderDetailsActivity.class);
        intent.putExtra(REMINDER_DETAILS_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REMINDER_PENDING_INTENT_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        clearAllNotifications(context);
        return pendingIntent;
    }

    public static NotificationCompat.Action finishReminderAction(Context context) {
        Intent finishIntent = new Intent(context, ReminderDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, FINISH_NOTIFICATION_PENDING_INTENT, finishIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_done_black_24px, context.getText(R.string.reminder_finished),  pendingIntent);
        clearAllNotifications(context);
        return action;
    }

    public static NotificationCompat.Action postponeReminder(Context context) {
        Intent postponeReminder  = new Intent(context, ReminderDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, POSTPONE_NOTIFICATION_PENDING_INTENT, postponeReminder, PendingIntent.FLAG_UPDATE_CURRENT);
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