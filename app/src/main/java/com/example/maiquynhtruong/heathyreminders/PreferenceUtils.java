package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by maiquynhtruong on 7/14/17.
 */

public class PreferenceUtils {
    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count";
    public static final int DEFAULT_COUNT = 0;
    public static void incrementWaterCount(Context context) {
        int currentWater = getWaterCount(context);
        PreferenceUtils.setWaterCount(context, ++currentWater);
    }

    public static void incrementChargingReminderCount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminder = preferences.getInt(KEY_CHARGING_REMINDER_COUNT, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminder);
        editor.commit();
    }

    public static int getChargingReminderCount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
    }
    public static int getWaterCount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
    }

    public static void setWaterCount(Context context, int currentWaterCount) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_WATER_COUNT, currentWaterCount);
        editor.commit();
    }
}
