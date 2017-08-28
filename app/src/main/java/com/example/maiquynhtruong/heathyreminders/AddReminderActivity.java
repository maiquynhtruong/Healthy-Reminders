package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.ReminderReceiver;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    Spinner frequencySpinner;
    TextInputEditText title;
    TextInputLayout titleLayout;
    TextView atTime, onDate, repeatNumberTv;
    Toolbar toolbar;
    int repeatNumber;
    boolean repeat;
    String repeatType;
    ReminderDatabase database;
    Calendar calendar;
    FancyButton saveBtn, cancelBtn;
    int month, dayOfMonth, year, hourOfDay, minute;
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
        repeatNumberTv = (TextView) findViewById(R.id.reminder_repeat_no);
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
        this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.repeat = true;
        this.repeatNumber = 1;
        this.repeatType = Reminder.HOURLY;
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, calendar.get(Calendar.MINUTE), isPM ? "PM" : "AM"));
        onDate.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));

        database = new ReminderDatabase(getApplicationContext());

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
        repeatType = frequency.toString();
        Log.i("ReminderAddActivity", "repeatType set as: " + repeatType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.repeatType = Reminder.HOURLY; // get the first one
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
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
    }

    public void onRepeatNumberSet(View view) {
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
                            repeatNumber = 1;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void saveReminder(View view) {

        String title = this.title.getText().toString();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        int reminderID = (int) database.setReminder(new Reminder(title, hourOfDay, minute, month,
                dayOfMonth, year, repeat, repeatNumber, repeatType));

        // Need to set calendar in case user doesn't choose date and time, aka current time

        Log.i("AddReminderActivity", "Setting reminder " + title + " with id " + reminderID);
        if (repeatType.equals(Reminder.DAILY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_DAY);
        else if (repeatType.equals(Reminder.WEEKLY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_DAY*7);
        if (repeatType.equals(Reminder.MONTHLY) || repeatType.equals(Reminder.YEARLY))
            ReminderReceiver.setReminderMonthOrYear(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, repeatType);
        else //if (repeatType.equals(Reminder.HOURLY))
            ReminderReceiver.setReminderHourOrDayOrWeek(getApplicationContext(),
                    calendar.getTimeInMillis(), reminderID, AlarmManager.INTERVAL_HOUR);
//        else
//            Log.i("AddReminderActivity", "Setting reminder but none of the above type!");
        onBackPressed();
    }

    public void cancelReminder(View view) {
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
