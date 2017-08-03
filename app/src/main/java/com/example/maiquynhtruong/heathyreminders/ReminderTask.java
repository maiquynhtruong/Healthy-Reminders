package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;

import com.example.maiquynhtruong.heathyreminders.Utilities.NotificationUtils;
import com.example.maiquynhtruong.heathyreminders.Utilities.PreferenceUtils;

public class ReminderTask {
    public static final String ACTION_REMIND = "reminder-tag";
    public static final String ACTION_POSTPONE_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_FINISH_NOTIFICATION = "finish-notification";

    public static void executeTask(Context context, String action) {
        if (action.equals(ACTION_POSTPONE_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (action.equals(ACTION_FINISH_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        } else NotificationUtils.reminderNotify(context);
    }
}
