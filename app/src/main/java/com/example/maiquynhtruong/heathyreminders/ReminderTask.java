package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;

/**
 * Created by maiquynhtruong on 7/14/17.
 */

public class ReminderTask {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public  static void executeTask(Context context, String action) {
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)){
            incrementWaterCount(context);
        }
    }

    public static void incrementWaterCount(Context context) {
        PreferenceUtils.incrementWaterCount(context);
    }
}
