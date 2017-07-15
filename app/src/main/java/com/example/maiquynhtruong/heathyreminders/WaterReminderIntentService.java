package com.example.maiquynhtruong.heathyreminders;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WaterReminderIntentService extends IntentService {
    public WaterReminderIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // do background work
    }
}
