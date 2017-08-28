package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;


public class ReminderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener{
    TextInputEditText reminderTitle;
    TextView onDate, atTime, repeatNumberTv;
    int hourOfDay, minute, month, dayOfMonth, year, repeatNumber;
    int reminderID;
    boolean repeat;
    String repeatType;
    Reminder reminder;
    FancyButton cancelBtn, updateBtn;
    ReminderDatabase database;
    Spinner frequencySpinner;
    Calendar calendar;
    public static final String REMINDER_DETAILS_ID = "reminder-id";
    public static final int EDIT_REMINDER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        onDate = (TextView) findViewById(R.id.datePicker);
        atTime = (TextView) findViewById(R.id.timePicker);
        reminderTitle = (TextInputEditText) findViewById(R.id.reminder_title);
        frequencySpinner = (Spinner) findViewById(R.id.reminder_frequency_spinner);
        atTime = (TextView) findViewById(R.id.timePicker);
        onDate = (TextView) findViewById(R.id.datePicker);
        repeatNumberTv = (TextView) findViewById(R.id.reminder_repeat_no);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);
        updateBtn = (FancyButton) findViewById(R.id.btn_save);

        database = new ReminderDatabase(getApplicationContext());

        getSupportActionBar().setTitle(getString(R.string.app_edit_reminder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        calendar = Calendar.getInstance();

        // get stuff from intent calling this activity
        int reminderID = getIntent().getIntExtra(ReminderDetailsActivity.REMINDER_DETAILS_ID,0);

        Log.i("ReminderDetailsActivity", "onCreate() The reminder passed has id " + reminderID);
        reminder = database.getReminder(reminderID);

        if (reminder != null) {
            reminderTitle.setText(reminder.getTitle());
            hourOfDay = reminder.getHour();
            minute = reminder.getMinute();
            month = reminder.getMonth();
            dayOfMonth = reminder.getDay();
            year = reminder.getYear();
            repeatNumber = reminder.getRepeatNumber();
            repeatType = reminder.getRepeatType();
            boolean isPM = (hourOfDay >= 12);
            atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, calendar.get(Calendar.MINUTE), isPM ? "PM" : "AM"));
            onDate.setText(month + "/" + dayOfMonth + "/" + year);
        } else {
            Log.i("ReminderDetailsActivity", "The reminder passed with id " + reminderID + " is null !");
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
        repeatNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRepeatNumberSet();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        onDate.setText(++month + "/" + dayOfMonth + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
    }

    public void onRepeatNumberSet() {
        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Enter Repeat Number")
                .setView(input)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (input.getText().toString().length() == 0) {
                                    repeatNumberTv.setText(repeatNumber);
                                }
                                else {
                                    repeatNumber = Integer.parseInt(input.getText().toString().trim());
                                    repeatNumberTv.setText(repeatNumber);
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                });
        alert.show();
    }

    public void updateReminder(View view) {
        reminder.setTitle(reminderTitle.getText().toString());
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
        ReminderReceiver.cancelAlarm(getApplicationContext(), reminderID);
        // create new notification
        if (repeatType.equals(Reminder.MONTHLY) || repeatType.equals(Reminder.YEARLY))
            ReminderReceiver.setReminderMonthOrYear(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, repeatNumber, repeatType);
        else if (repeatType.equals(Reminder.DAILY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, repeatNumber * AlarmManager.INTERVAL_DAY);
        else if (repeatType.equals(Reminder.WEEKLY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, repeatNumber * AlarmManager.INTERVAL_DAY*7);
        else //if (repeatType.equals(Reminder.HOURLY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, repeatNumber * AlarmManager.INTERVAL_HOUR);
//        else
//            Log.i("AddReminderActivity", "Setting reminder but none of the above type!");
        onBackPressed();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
