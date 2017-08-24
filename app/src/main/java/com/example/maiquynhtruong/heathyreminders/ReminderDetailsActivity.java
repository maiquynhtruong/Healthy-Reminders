package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;


public class ReminderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText reminderTitle;
    TextView date, time;
    int hourOfDay, minute, month, dayOfMonth, year, repeatNumber;
    int reminderID;
    boolean repeat;
    String repeatType;
    Reminder reminder;
    FancyButton cancelBtn, updateBtn;
    ReminderDatabase database;
    Calendar calendar;
    ReminderReceiver receiver;
    public static final String REMINDER_DETAILS_ID = "reminder-id";
    public static final int EDIT_REMINDER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        date = (TextView) findViewById(R.id.datePicker);
        time = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (TextInputEditText) findViewById(R.id.reminder_title);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);
        updateBtn = (FancyButton) findViewById(R.id.btn_save);
        database = new ReminderDatabase(this);


        getSupportActionBar().setTitle(getString(R.string.app_edit_reminder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // get stuff from intent calling this activity
        reminderID = Integer.parseInt(getIntent().getStringExtra(ReminderDetailsActivity.REMINDER_DETAILS_ID));
        Log.i("ReminderDetailsActivity", String.valueOf(reminderID));
        reminder = database.getReminder(reminderID);
        if (reminder != null) {
            reminderTitle.setText(reminder.getTitle());
            date.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
            time.setText(reminder.getHour() + ":" + reminder.getMinute());
        }
        updateBtn.setText("UPDATE");
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReminder(view);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        receiver = new ReminderReceiver();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public void updateReminder(View view) {
        reminder.setTitle(reminderTitle.toString());
        reminder.setHour(hourOfDay);
        reminder.setMinute(minute);
        reminder.setDay(dayOfMonth);
        reminder.setMonth(month);
        reminder.setYear(year);
        reminder.setRepeat(repeat);
        reminder.setRepeatNumber(repeatNumber);
        reminder.setRepeatType(repeatType);

        // update reminder in database
        database.updateReminder(reminder);

        // update calendar with new time
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // cancel old notification
        receiver.cancelAlarm(getApplicationContext(), reminderID);
        // create new notification
        if (repeatType.equals(Reminder.MONTHLY) || repeatType.equals(Reminder.YEARLY)) new ReminderReceiver().setReminderMonthOrYear(this, calendar.getTimeInMillis(),
                reminderID, repeatType);
        else if (repeatType.equals(Reminder.HOURLY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_HOUR);
        else if (repeatType.equals(Reminder.DAILY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_DAY);
        else if (repeatType.equals(Reminder.WEEKLY)) new ReminderReceiver().setReminderHourOrDayOrWeek(this, calendar.getTimeInMillis(),
                reminderID, AlarmManager.INTERVAL_DAY*7);

        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
