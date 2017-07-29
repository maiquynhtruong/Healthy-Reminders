package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;

import com.example.maiquynhtruong.heathyreminders.Utilities.NotificationUtils;
import com.example.maiquynhtruong.heathyreminders.Utilities.PreferenceUtils;

/**
 * Created by maiquynhtruong on 7/14/17.
 */

public class ReminderTask {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DONE = "done";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_REMIND_CHARGING = "remind-charging";

    public  static void executeTask(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)){
            incrementWaterCount(context);
        } else if (action.equals(ACTION_DISMISS_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (action.equals(ACTION_REMIND_CHARGING)) {
            chargingReminderNotification(context);
        }
    }

    public static void incrementWaterCount(Context context) {
        PreferenceUtils.incrementWaterCount(context);
        NotificationUtils.clearAllNotifications(context);
    }

    public static void chargingReminderNotification(Context context) {
        PreferenceUtils.incrementChargingReminderCount(context);
        NotificationUtils.reminderUserBecauseCharging(context);
    }
}
