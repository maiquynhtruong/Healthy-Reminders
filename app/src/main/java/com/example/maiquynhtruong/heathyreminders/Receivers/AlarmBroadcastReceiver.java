package com.example.maiquynhtruong.heathyreminders.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.maiquynhtruong.heathyreminders.Utilities.NotificationUtils;

/**
 * Created by d4truom on 8/3/2017.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        NotificationUtils.reminderNotify(context);
    }
}
