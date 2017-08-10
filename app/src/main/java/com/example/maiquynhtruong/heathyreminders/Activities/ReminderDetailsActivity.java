package com.example.maiquynhtruong.heathyreminders.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Receivers.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import butterknife.ButterKnife;

public class ReminderDetailsActivity extends AppCompatActivity {
    EditText reminderTitle;
    TextView date, time;
    Reminder reminder;
    Button cancelBtn, saveBtn;
    ReminderDatabase database;
    long reminderID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        date = (TextView) findViewById(R.id.datePicker);
        time = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (EditText) findViewById(R.id.reminder_title);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        saveBtn = (Button) findViewById(R.id.btn_save);
        database = new ReminderDatabase(this);

        // get stuff from intent calling this activity
        reminderID = getIntent().getIntExtra(ReminderReceiver.REMINDER_ID, 0);
        reminder = database.getReminder(reminderID);
        reminderTitle.setText(reminder.getTitle());
        date.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
        time.setText(reminder.getHour() + ":" + reminder.getMinute());
    }

    public void cancelReminder(View view) {
        database.deleteReminder(reminderID);
    }

    public void saveReminder(View view) {
        Reminder reminder = new Reminder(reminderTitle.getText().toString(), );
        database.updateReminder(reminderID, reminder);
    }
}
