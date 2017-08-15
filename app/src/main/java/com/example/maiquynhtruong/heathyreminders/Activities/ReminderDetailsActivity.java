package com.example.maiquynhtruong.heathyreminders.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Receivers.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import mehdi.sakout.fancybuttons.FancyButton;


public class ReminderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText reminderTitle;
    TextView date, time;
    Reminder reminder;
    FancyButton cancelBtn, saveBtn;
    ReminderDatabase database;
    Calendar calendar;
    long reminderID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        date = (TextView) findViewById(R.id.datePicker);
        time = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (TextInputEditText) findViewById(R.id.reminder_title);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);
        saveBtn = (FancyButton) findViewById(R.id.btn_save);
        database = new ReminderDatabase(this);

        // get stuff from intent calling this activity
        reminderID = getIntent().getIntExtra(ReminderReceiver.REMINDER_ID, 0);
        reminder = database.getReminder(reminderID);
        reminderTitle.setText(reminder.getTitle());
        date.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
        time.setText(reminder.getHour() + ":" + reminder.getMinute());
    }

//    public void cancelReminder(View view) {
//        database.deleteReminder(reminderID);
//    }
//
//    public void saveReminder(View view) {
//        Reminder toBeSaved = new Reminder(reminderTitle.getText().toString(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), reminder.isRepeat(), reminder.getRepeatNumber(),
//                reminder.getRepeatType());
//        database.updateReminder(reminderID, toBeSaved);
//    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
    }
}
