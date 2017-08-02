package com.example.maiquynhtruong.heathyreminders.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.maiquynhtruong.heathyreminders.ReminderTask;
import com.example.maiquynhtruong.heathyreminders.Utilities.NotificationUtils;

public class ReminderIntentService extends IntentService {
    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
//        ReminderTask.executeTask(this, action);
//        ReminderTask.executeTask(this);
        NotificationUtils.reminderNotify(this);
    }
}
