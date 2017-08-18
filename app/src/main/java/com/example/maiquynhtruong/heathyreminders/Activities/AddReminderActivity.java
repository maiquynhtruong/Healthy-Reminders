package com.example.maiquynhtruong.heathyreminders.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Receivers.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    Spinner frequencySpinner;
    TextInputEditText title;
    TextInputLayout titleLayout;
    TextView atTime, onDate;
    Toolbar toolbar;
    int repeatNumber;
    boolean repeat;
    String repeatType;
    ReminderDatabase database;
    Calendar calendar;
    FancyButton saveBtn, cancelBtn;
    public static final String TAG = "AddActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frequencySpinner = (Spinner) findViewById(R.id.reminder_frequency_spinner);
        title = (TextInputEditText) findViewById(R.id.reminder_title);
        titleLayout = (TextInputLayout) findViewById(R.id.reminder_title_layout);
        atTime = (TextView) findViewById(R.id.timePicker);
        onDate = (TextView) findViewById(R.id.datePicker);
        saveBtn = (FancyButton) findViewById(R.id.btn_save);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);

        getSupportActionBar().setTitle(getString(R.string.app_add_reminder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReminder(view);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // error for empty title
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().trim().isEmpty()) {
                    titleLayout.setError(getString(R.string.title_empty_error));
                } else {
                    titleLayout.setErrorEnabled(false);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(this);

        // set the current time as the default time when first showed
        calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, calendar.get(Calendar.MINUTE), isPM ? "PM" : "AM"));
        onDate.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));

        database = new ReminderDatabase(getBaseContext());

        // recover states on device rotation
        if (savedInstanceState != null) {

        }
    }

    public void showDatePicker(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void showTimePicker(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(this));
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence frequency = (CharSequence) adapterView.getItemAtPosition(i);
        Toast.makeText(adapterView.getContext(), frequency, Toast.LENGTH_LONG).show();
        repeatType = frequency.toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        onDate.setText(month + "/" + dayOfMonth + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void saveReminder(View view) {
        String title = this.title.getText().toString();
        int reminderID = (int) database.setReminder(new Reminder(title, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), repeat, repeatNumber, repeatType));
        Toast.makeText(this, "Save reminder as " + title + " at " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " on " + calendar.get(Calendar.MONTH) +
        "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR), Toast.LENGTH_LONG).show();

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

    public void cancelReminder(View view) {
        onBackPressed();
    }
}
