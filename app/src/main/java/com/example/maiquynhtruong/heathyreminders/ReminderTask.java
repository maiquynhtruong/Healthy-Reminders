package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.maiquynhtruong.heathyreminders.Receivers.AlarmReceiver;
import com.example.maiquynhtruong.heathyreminders.Utilities.NotificationUtils;

public class ReminderTask {
    public static final String ACTION_REMIND = "reminder-tag";
    public static final String ACTION_POSTPONE_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_FINISH_NOTIFICATION = "finish-notification";

    public static void executeNotificationTask(Context context, String action) {
        if (action.equals(ACTION_POSTPONE_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
            // set up a ELAPSED_REALTIME_WAKEUP for 30 minutes
        } else if (action.equals(ACTION_FINISH_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        } else NotificationUtils.reminderNotify(context);
    }

    public static void executeAlarmTask(Context context, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
