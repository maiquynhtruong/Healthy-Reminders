package com.example.maiquynhtruong.heathyreminders.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;


public class AlarmService extends IntentService {
    public static final String CREATE_ALARM = "create-alarm";
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
    }

    public void setAlarm(Context context, Calendar calendar) {

    }
}
