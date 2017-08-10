package com.example.maiquynhtruong.heathyreminders.Receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.Activities.MainActivity;
import com.example.maiquynhtruong.heathyreminders.Activities.ReminderDetailsActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

public class ReminderReceiver extends BroadcastReceiver {
    public static final int REMINDER_PENDING_INTENT_ID = 2;
    public static final int FINISH_NOTIFICATION_PENDING_INTENT = 3;
    public static final int POSTPONE_NOTIFICATION_PENDDING_INTENT = 4;
    public static final String REMINDER_REPEAT_TYPE = "ReminderType";
    public static final String REMINDER_ID = "ReminderID";
    public static final String REMINDER_TIME_MILLIS = "ReminderMillis";
    public static final String TAG = "AddReminderActivity";
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(REMINDER_REPEAT_TYPE);
        int millis = intent.getIntExtra(REMINDER_TIME_MILLIS, 0);
        long reminderId = intent.getLongExtra(REMINDER_ID, 0);
        reminderNotify(context, reminderId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        if (type.equals(Reminder.YEARLY)) {
            calendar.add(Calendar.YEAR, 1);
            Log.i(TAG, "Added one year. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.YEARLY);
        } else if (type.equals(Reminder.MONTHLY)) {
            calendar.add(Calendar.MONTH, 1);
            Log.i(TAG, "Added one month. Now calendar is " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) +
                    calendar.get(Calendar.YEAR));
            setReminderMonthOrYear(context, calendar.getTimeInMillis(), reminderId, Reminder.MONTHLY);
        }
    }

    public static void setReminderMonthOrYear(Context context, long timeInMillis, long reminderID, String repeatType) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(REMINDER_REPEAT_TYPE, repeatType);
        intent.putExtra(REMINDER_TIME_MILLIS, timeInMillis);
        intent.putExtra(REMINDER_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public static void setReminderHourOrDayOrWeek(Context context, long timeInMillis, long reminderID, long interval) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, pendingIntent);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void reminderNotify(Context context, long reminderID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context, reminderID)) // supply PendingIntent to send when the notification is clicked
                .setAutoCancel(true)
                .addAction(postponeReminder(context))
                .addAction(finishReminderAction(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(REMINDER_PENDING_INTENT_ID, builder.build());
    }

    public static NotificationCompat.Action finishReminderAction(Context context) {
        Intent ignoreIntent = new Intent(context, ReminderDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, FINISH_NOTIFICATION_PENDING_INTENT, ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_done_black_24px, context.getText(R.string.reminder_finished),  pendingIntent);
        return action;
    }

    public static NotificationCompat.Action postponeReminder(Context context) {
        Intent postponeReminder  = new Intent(context, ReminderDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, POSTPONE_NOTIFICATION_PENDDING_INTENT, postponeReminder, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px, context.getString(R.string.reminder_postponed), pendingIntent);
        return action;
    }

    // should show the reminder that is showed in the notification
    public static PendingIntent contentIntent(Context context, long reminderID) {
        Intent intent = new Intent(context, ReminderDetailsActivity.class);
        intent.putExtra(ReminderDatabase.ReminderEntry.REMINDER_ID, reminderID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REMINDER_PENDING_INTENT_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_alarm);
        return bitmap;
    }
}
