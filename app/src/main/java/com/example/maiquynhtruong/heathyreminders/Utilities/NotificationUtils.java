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

import com.example.maiquynhtruong.heathyreminders.MainActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.ReminderTask;
import com.example.maiquynhtruong.heathyreminders.Services.ReminderIntentService;

public class NotificationUtils {
    public static final int DRINKING_REMINDER_NOTIFICATION = 1234;
    public static final int WATER_REMINDER_PENDING_INTENT_ID = 1111;
    public static final int DRINKING_WATER_PENDING_INTENT = 4321;
    public static final int IGNORE_WATER_PENDING_INTENT = 3412;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void reminderUserBecauseCharging(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_local_drink_black_24px)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .addAction(ignoreReminderAction(context))
                .addAction(drinkWaterAction(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(WATER_REMINDER_PENDING_INTENT_ID, builder.build());
    }

    public static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreIntent = new Intent(context, ReminderIntentService.class);
        ignoreIntent.setAction(ReminderTask.ACTION_DISMISS_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, IGNORE_WATER_PENDING_INTENT, ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
                "No, I won't", pendingIntent);
        return action;
    }

    public static NotificationCompat.Action drinkWaterAction(Context context) {
        Intent incrementWaterIntent = new Intent(context, ReminderIntentService.class);
        incrementWaterIntent.setAction(ReminderTask.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, DRINKING_WATER_PENDING_INTENT, incrementWaterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_local_drink_black_24px,
                "Drank water", pendingIntent);
        return action;
    }

    public static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, WATER_REMINDER_PENDING_INTENT_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_local_drink_black_24px);
        return bitmap;
    }

}
