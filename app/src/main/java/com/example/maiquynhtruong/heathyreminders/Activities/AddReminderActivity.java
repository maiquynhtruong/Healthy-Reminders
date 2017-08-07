package com.example.maiquynhtruong.heathyreminders.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;
import com.example.maiquynhtruong.heathyreminders.Services.AlarmService;
import com.example.maiquynhtruong.heathyreminders.Services.ReminderIntentService;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    Spinner frequencySpinner;
    Spinner datePickerSpinner;
    EditText name;
    EditText description;

    String date;
    int hour, minute, repeateNumber;
    boolean repeat;
    ReminderDatabase database;
    Calendar calendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        frequencySpinner = (Spinner) findViewById(R.id.reminder_frequency_spinner);
        datePickerSpinner = (Spinner) findViewById(R.id.reminder_date_spinner);
        name = (EditText) findViewById(R.id.reminder_name);
        description = (EditText) findViewById(R.id.reminder_description);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(this);
        calendar = Calendar.getInstance();
        database = new ReminderDatabase(getBaseContext());
    }

    public void saveReminder(View view) {
        String title = name.getText().toString();
        String date =
        int reminderID = database.setReminder(new Reminder(title));

        Intent createReminderIntent = new Intent(getBaseContext(), ReminderIntentService.class);
        createReminderIntent.setAction("CREATE");
        createReminderIntent.putExtra("ReminderId", reminderID);
        startService(createReminderIntent);
    }

    public void cancelReminder(View view) {

        onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence frequency = (CharSequence) adapterView.getItemAtPosition(i);
        Toast.makeText(adapterView.getContext(), frequency, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
