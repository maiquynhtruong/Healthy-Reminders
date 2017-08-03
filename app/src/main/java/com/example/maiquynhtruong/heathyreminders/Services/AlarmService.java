package com.example.maiquynhtruong.heathyreminders.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class AlarmService extends IntentService {
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
    }
}
