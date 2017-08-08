package com.example.maiquynhtruong.heathyreminders.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;

public class ReminderDetailsActivity extends AppCompatActivity {
    EditText reminderTitle;
    TextView date, time;
    Reminder reminder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);
        date = (TextView) findViewById(R.id.datePicker);
        time = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (EditText) findViewById(R.id.reminder_title);
        fillTexts();
    }
    public void fillTexts(){
        reminderTitle.setText(reminder.getTitle());
        date.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
        time.setText(reminder.getHour() + ":" + reminder.getMinute());
    }

}
