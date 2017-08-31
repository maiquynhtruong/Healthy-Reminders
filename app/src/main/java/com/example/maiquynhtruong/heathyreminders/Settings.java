package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

/**
 * Created by d4truom on 8/31/2017.
 */

public class SettingsFragment extends PreferenceActivity {
    public static final String PREF_KEY_COLOR_PICKER = "pref_key_color_picker";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

////    @Override
//    public void onDisplayPreferenceDialog(Preference preference) {
////        super.onDisplayPreferenceDialog(preference);
//        ColorPickerDialogBuilder
//                .with(getActivity())
//                .setTitle("Choose color")
//                .initialColor(Integer.parseInt("#E1BEE7"))
//                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                .density(12)
//                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                    @Override
//                    public void onColorSelected(int selectedColor) {
//                        Toast.makeText(getActivity(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
//                    }
//                })
//                .setPositiveButton("ok", new ColorPickerClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
////                            changeBackgroundColor(selectedColor);
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .build()
//                .show();
//    }
}
