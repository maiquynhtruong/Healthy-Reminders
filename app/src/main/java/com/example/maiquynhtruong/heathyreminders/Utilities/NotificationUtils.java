package com.example.maiquynhtruong.heathyreminders.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.maiquynhtruong.heathyreminders.Activities.MainActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.ReminderTask;
import com.example.maiquynhtruong.heathyreminders.Services.ReminderIntentService;

public class NotificationUtils {
//    public static final int DRINKING_REMINDER_NOTIFICATION = 1234;
    public static final int REMINDER_PENDING_INTENT_ID = 2;
    public static final int FINISH_NOTIFICATION_PENDING_INTENT = 3;
    public static final int POSTPONE_NOTIFICATION_PENDDING_INTENT = 4;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void reminderNotify(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
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
        Intent ignoreIntent = new Intent(context, ReminderIntentService.class);
        ignoreIntent.setAction(ReminderTask.ACTION_FINISH_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, FINISH_NOTIFICATION_PENDING_INTENT, ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_done_black_24px, context.getText(R.string.reminder_finished),  pendingIntent);
        return action;
    }

    public static NotificationCompat.Action postponeReminder(Context context) {
        Intent postponeReminder  = new Intent(context, ReminderIntentService.class);
        postponeReminder.setAction(ReminderTask.ACTION_POSTPONE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, POSTPONE_NOTIFICATION_PENDDING_INTENT, postponeReminder, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px, context.getString(R.string.reminder_postponed), pendingIntent);
        return action;
    }

    public static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
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
