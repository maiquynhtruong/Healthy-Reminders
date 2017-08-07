package com.example.maiquynhtruong.heathyreminders.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Services.AlarmService;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner frequencySpinner;
    Spinner datePickerSpinner;
    EditText name;
    EditText description;
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
    }

    public void saveReminder(View view) {
        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        intent.setAction(AlarmService.CREATE_ALARM);
        getApplicationContext().startService(intent);
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
}
