package com.example.maiquynhtruong.heathyreminders;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerPreference;

public class SettingsActivity extends PreferenceActivity {
    public static final String PREF_KEY_COLOR_PICKER = "color_picker_preference";
    Preference preference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
//        preference = findPreference(PREF_KEY_COLOR_PICKER);
        Toast.makeText(getApplicationContext(), "Lol", Toast.LENGTH_SHORT).show();
    }
}
