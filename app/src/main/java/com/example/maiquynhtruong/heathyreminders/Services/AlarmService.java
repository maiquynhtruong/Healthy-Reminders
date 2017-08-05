package com.example.maiquynhtruong.heathyreminders.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;

import com.example.maiquynhtruong.heathyreminders.Receivers.AlarmReceiver;


public class AlarmService extends IntentService {
    public static final String CREATE_ALARM = "create-alarm";
    public static final String DELETE_ALARM = "delete-alarm";
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
    }

    public void setAlarm(Context context, Calendar calendar) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
