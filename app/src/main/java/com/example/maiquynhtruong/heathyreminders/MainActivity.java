package com.example.maiquynhtruong.heathyreminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.Adapters.ReminderAdapter;
import com.example.maiquynhtruong.heathyreminders.Fragments.NewReminderActivity;
import com.example.maiquynhtruong.heathyreminders.Services.ReminderIntentService;
import com.example.maiquynhtruong.heathyreminders.Utilities.PreferenceUtils;
import com.example.maiquynhtruong.heathyreminders.Utilities.ReminderUtils;
import com.firebase.jobdispatcher.Constraint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SharedPreferences preferences;
    IntentFilter filter;
    ChargingBroadcastReceiver receiver;

//    @BindView(R.id.main_recycler_view) RecyclerView mainRecyclerView;

    RecyclerView mainRecyclerView;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Showing add reminder dialog", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showAddReminder();
            }
        });

        setUpNavigationDrawer();
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(layoutManager);
        adapter = new ReminderAdapter(createReminders());
        mainRecyclerView.setAdapter(adapter);

        ReminderUtils.scheduleReminder(this, ReminderTask.ACTION_REMIND, new int[] {Constraint.DEVICE_IDLE});

        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    public void showAddReminder() {
        Intent addReminderIntent = new Intent(this, NewReminderActivity.class);
        startActivity(addReminderIntent);
//        startActivityForResult();
    }

    public ReminderListController createReminders() {
        ReminderListController controller = new ReminderListController();
        controller.addReminder("buy-grocery", "Buy Grocery");
        controller.addReminder("pay-rent", "Pay the rent");
        controller.addReminder("pay-insurance", "Pay insurance");
        return controller;
    }

    public void incrementWater(View view) {
        Intent incrementWaterCountIntent = new Intent(this, ReminderIntentService.class);
//        incrementWaterCountIntent.setAction(ReminderTask.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterCountIntent);
    }

    public void setUpNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // for future state changes, we still need the receiver
//        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_finished) {

        } else if (id == R.id.nav_recommendations){

        } else if (id == R.id.nav_reminders) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ChargingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
//            showCharging(isCharging);
        }
    }
}
