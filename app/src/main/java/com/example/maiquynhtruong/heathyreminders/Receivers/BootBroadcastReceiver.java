package com.example.maiquynhtruong.heathyreminders.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.maiquynhtruong.heathyreminders.Services.AlarmService;

/**
 * Created by d4truom on 8/3/2017.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent setAlarmIntent = new Intent(context, AlarmService.class);
            setAlarmIntent.setAction(AlarmService.CREATE_ALARM);
            context.startService(setAlarmIntent);
        }
    }
}
