package com.example.maiquynhtruong.heathyreminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    Spinner frequencySpinner;
    TextInputEditText titleText;
    TextInputLayout titleLayout;
    TextView atTime, onDate, repeatNumberTv;
    Button colorBtn;
    Toolbar toolbar;
    int repeatNumber, currentColor = android.R.color.holo_blue_dark;
    ReminderDatabase database;
    Calendar calendar;
    FancyButton saveBtn, cancelBtn;
    int month, dayOfMonth, year, hourOfDay, minute;
    String title, repeatType, color;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_HOUR = "hour_key";
    private static final String KEY_MINUTE = "minute_key";
    private static final String KEY_YEAR = "year_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_MONTH = "month_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_COLOR = "color_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frequencySpinner = (Spinner) findViewById(R.id.reminder_frequency_spinner);
        titleText = (TextInputEditText) findViewById(R.id.reminder_title);
        titleLayout = (TextInputLayout) findViewById(R.id.reminder_title_layout);
        atTime = (TextView) findViewById(R.id.timePicker);
        onDate = (TextView) findViewById(R.id.datePicker);
        repeatNumberTv = (TextView) findViewById(R.id.reminder_repeat_number_text_view);
        saveBtn = (FancyButton) findViewById(R.id.btn_save);
        cancelBtn = (FancyButton) findViewById(R.id.btn_cancel);
        colorBtn = (Button) findViewById(R.id.reminder_color_btn);

        database = new ReminderDatabase(getApplicationContext());

        getSupportActionBar().setTitle(getString(R.string.app_add_reminder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleText.getText().toString().trim().isEmpty())
                    titleLayout.setError(getString(R.string.title_empty_error));
                else
                    saveReminder(view);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });

        // error for empty title
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (titleText.getText().toString().trim().isEmpty()) {
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
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        repeatNumber = 1;
        repeatType = Reminder.HOURLY;
        boolean isPM = (hourOfDay >= 12);
        atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, calendar.get(Calendar.MINUTE), isPM ? "PM" : "AM"));
        onDate.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
        colorBtn.setBackgroundColor(getResources().getColor(currentColor));
        Log.i("AddReminderActivity", "colorBtn.setBackgroundColor() " + getResources().getColor(currentColor) + " or " + Integer.toHexString(getResources().getColor(currentColor)));
        color = "#" + Integer.toHexString(getResources().getColor(currentColor));

        repeatNumberTv.setText("1");
        repeatNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumberPicker();
            }
        });
        // recover states on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            titleText.setText(savedTitle);
            title = savedTitle;

            String savedHour = savedInstanceState.getString(KEY_HOUR);
            hourOfDay = Integer.parseInt(savedHour);
            String savedMinute = savedInstanceState.getString(KEY_MINUTE);
            minute = Integer.parseInt(savedMinute);
            isPM = (hourOfDay >= 12);
            atTime.setText(String.format(Locale.US, "%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, calendar.get(Calendar.MINUTE), isPM ? "PM" : "AM"));

            String savedYear = savedInstanceState.getString(KEY_YEAR);
            year = Integer.parseInt(savedYear);
            String savedMonth = savedInstanceState.getString(KEY_MONTH);
            month = Integer.parseInt(savedMonth);
            String savedDate = savedInstanceState.getString(KEY_DATE);
            dayOfMonth = Integer.parseInt(savedDate);
            onDate.setText(month + "/" + dayOfMonth + "/" + year);

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            repeatNumber = Integer.parseInt(savedRepeatNo);

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            repeatType = savedRepeatType;

            String savedColor = savedInstanceState.getString(KEY_COLOR);
            color = savedColor;
        }
    }

    public void showColorPicker() {
        final Context context = AddReminderActivity.this;

        ColorPickerDialogBuilder
                .with(context)
                .setTitle(R.string.color_dialog_title)
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        // Handle on color change
                        Log.i("AddReminderActivity", "showColorPicker() at setOnColorChangedListener: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("AddReminderActivity", "showColorPicker() at setOnColorSelectedListener: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .showColorPreview(true)
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(context, android.R.color.background_dark))
                .build()
                .show();
    }

    public void changeBackgroundColor(int selectedColor) {
        Log.i("AddReminderActivity", "changeBackgroundColor(): #" + Integer.toHexString(selectedColor));
        color = "#" + Integer.toHexString(selectedColor);
        colorBtn.setBackgroundColor(selectedColor);
    }

    public void showNumberPicker() {
        final Dialog d = new Dialog(this);
        d.setTitle(getString(R.string.reminder_repeat_number_dialog_title));
        d.setContentView(R.layout.dialog_number_picker);
        Button setBtn = d.findViewById(R.id.reminder_repeat_number_set);
        Button cancelBtn = d.findViewById(R.id.reminder_repeat_number_cancel);
        final NumberPicker np = d.findViewById(R.id.reminder_repeat_number_picker);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                repeatNumber = i1;
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatNumberTv.setText(String.valueOf(repeatNumber));
                d.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, titleText.getText());
        outState.putCharSequence(KEY_HOUR, String.valueOf(hourOfDay));
        outState.putCharSequence(KEY_MINUTE, String.valueOf(minute));
        outState.putCharSequence(KEY_YEAR, String.valueOf(year));
        outState.putCharSequence(KEY_DATE, String.valueOf(dayOfMonth));
        outState.putCharSequence(KEY_MONTH, String.valueOf(month));
        outState.putCharSequence(KEY_REPEAT_NO, String.valueOf(repeatNumber));
        outState.putCharSequence(KEY_REPEAT_TYPE, String.valueOf(repeatType));
    }

    public void saveReminder(View view) {

        title = this.titleText.getText().toString();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        int reminderID = (int) database.setReminder(new Reminder(title, hourOfDay, minute, month,
                dayOfMonth, year, repeatNumber, repeatType, color));

        // Need to set calendar in case user doesn't choose date and time, aka current time

        Log.i("AddReminderActivity", "Setting reminder " + title + " with id " + reminderID);
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
