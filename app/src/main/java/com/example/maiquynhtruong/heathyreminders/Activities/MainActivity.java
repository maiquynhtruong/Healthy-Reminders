package com.example.maiquynhtruong.heathyreminders.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.Adapters.ReminderAdapter;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SharedPreferences preferences;
//    @BindView(R.id.main_recycler_view) RecyclerView mainRecyclerView;
    RecyclerView mainRecyclerView;
    TextView noReminders, hello, timeLeft;
    RecyclerView.LayoutManager layoutManager;
    ReminderAdapter adapter;
    ReminderDatabase database;
    public static final int ADD_REMINDER_REQUEST_CODE = 147;
    public static final String TAG = "MainActivity";
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
        noReminders = (TextView) findViewById(R.id.no_reminders_text);
        hello = (TextView) findViewById(R.id.hello);
        timeLeft = (TextView) findViewById(R.id.time_left);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(layoutManager);
        adapter = new ReminderAdapter();
        mainRecyclerView.setAdapter(adapter);
        database = new ReminderDatabase(this);
        database.deleteAllReminders();

        setTimeLeft();

        List<Reminder> reminders = database.getAllReminders();
        if (reminders.isEmpty()) {
            noReminders.setVisibility(View.VISIBLE);
        } else {
            noReminders.setVisibility(View.GONE);
            adapter.setUpReminders(reminders);
        }
    }

    public void setTimeLeft() {
        Calendar calendar = Calendar.getInstance();
        int time = 24 - calendar.get(Calendar.HOUR_OF_DAY);
        timeLeft.setText("You have " + time + " left before end of day");
    }
    public void showAddReminder() {
        Intent addReminderIntent = new Intent(this, AddReminderActivity.class);
        startActivityForResult(addReminderIntent, ADD_REMINDER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REMINDER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data.getLongExtra(ReminderDatabase.ReminderEntry.REMINDER_ID, 0);
            }
        }
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
        List<Reminder> reminders = database.getAllReminders();
        Log.i(TAG + ", onResume", Arrays.toString(reminders.toArray()));
        if (reminders.isEmpty()) {
            noReminders.setVisibility(View.VISIBLE);
        } else {
            noReminders.setVisibility(View.GONE);
            adapter.setUpReminders(reminders);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
