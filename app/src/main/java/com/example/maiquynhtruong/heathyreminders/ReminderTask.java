package com.example.maiquynhtruong.heathyreminders;

import android.app.Notification;
import android.content.Context;

/**
 * Created by maiquynhtruong on 7/14/17.
 */

public class ReminderTask {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    public  static void executeTask(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)){
            incrementWaterCount(context);
        } else if (action.equals(ACTION_DISMISS_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }

    public static void incrementWaterCount(Context context) {
        PreferenceUtils.incrementWaterCount(context);
        NotificationUtils.clearAllNotifications(context);
    }

    public static void chargingReminderNotification(Context context) {
        NotificationUtils.clearAllNotifications(context);
    }
}
