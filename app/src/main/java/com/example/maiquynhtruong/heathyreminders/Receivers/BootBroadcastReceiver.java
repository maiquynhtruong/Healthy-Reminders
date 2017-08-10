package com.example.maiquynhtruong.heathyreminders.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

/**
 * Created by d4truom on 8/3/2017.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ReminderDatabase database = new ReminderDatabase(context);

            Intent setAlarmIntent = new Intent(context, ReminderReceiver.class);
            // set action create alarm for the intent and send it
            context.startService(setAlarmIntent);
        }
    }
}
