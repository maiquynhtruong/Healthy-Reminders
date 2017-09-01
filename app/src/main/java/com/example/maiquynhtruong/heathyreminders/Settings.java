package com.example.maiquynhtruong.heathyreminders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerPreference;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Preference.OnPreferenceChangeListener changeListener;
    Preference.OnPreferenceClickListener clickListener;
    public static final String PREF_KEY_COLOR_PICKER = "color_picker_preference";
    SharedPreferences sharedPreferences;
    ColorPickerPreference preference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preference = (ColorPickerPreference) findPreference(PREF_KEY_COLOR_PICKER);
        changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object o) {
                Toast.makeText(getApplicationContext(), "color picked is " + Integer.toHexString((Integer) o), Toast.LENGTH_SHORT).show();
//                sharedPreferences.edit().putString(Settings.PREF_KEY_COLOR_PICKER, Integer.toHexString((Integer) o)).apply();
                sharedPreferences.edit().putInt(Settings.PREF_KEY_COLOR_PICKER, (Integer) o).apply();
                return true;
            }
        };
        preference.setOnPreferenceChangeListener(changeListener);
//        clickListener = new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                return true;
//            }
//        };
//        preference.setOnPreferenceClickListener(clickListener);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
//    }
//
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        Log.i("Settings", "onSharedPreferenceChanged() with key " + sharedPreferences.getString(s, "null"));
    }
}
