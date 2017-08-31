package com.example.maiquynhtruong.heathyreminders;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.flask.colorpicker.ColorPickerPreference;

/**
 * Created by d4truom on 8/31/2017.
 */

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ColorPickerPreference preference = (ColorPickerPreference) findPreference("color_picker_preference");
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                return false;
            }
        });
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }
}
