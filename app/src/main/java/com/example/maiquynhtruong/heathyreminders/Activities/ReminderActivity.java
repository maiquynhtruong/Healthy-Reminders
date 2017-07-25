package com.example.maiquynhtruong.heathyreminders.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.maiquynhtruong.heathyreminders.Fragments.RecommendationFragment;
import com.example.maiquynhtruong.heathyreminders.R;

/**
 * Created by maiquynhtruong on 7/25/17.
 */

public class ReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

    }

    public void getRecommendation() {
        RecommendationFragment recommendationFragment = new RecommendationFragment();
        FragmentManager manager = getSupportFragmentManager();
    }
}
