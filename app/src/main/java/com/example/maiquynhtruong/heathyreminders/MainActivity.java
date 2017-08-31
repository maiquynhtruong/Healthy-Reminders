package com.example.maiquynhtruong.heathyreminders;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
//    @BindView(R.id.main_recycler_view) RecyclerView mainRecyclerView;
    public RecyclerView mainRecyclerView;
    public TextView noReminders;
    RecyclerView.LayoutManager layoutManager;
    public ReminderAdapter adapter;
    public ReminderDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        setUpNavigationDrawer();
        toolbar.setTitle(R.string.app_name);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReminder();
            }
        });

        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        noReminders = (TextView) findViewById(R.id.no_reminders_text);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(layoutManager);
        adapter = new ReminderAdapter(this);
        mainRecyclerView.setAdapter(adapter);
        database = new ReminderDatabase(getApplicationContext());

        List<Reminder> reminders = database.getAllReminders();
        if (reminders.isEmpty()) {
            noReminders.setVisibility(View.VISIBLE);
        } else {
            noReminders.setVisibility(View.GONE);
            adapter.setUpReminders(reminders);
        }

        // ensures that your application is properly initialized with default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    public void showAddReminder() {
        Intent addReminderIntent = new Intent(this, AddReminderActivity.class);
        startActivity(addReminderIntent);
    }

    public void showEditReminder(int clickID) {
        Intent intent = new Intent(this, ReminderDetailsActivity.class);
        intent.putExtra(ReminderDetailsActivity.REMINDER_DETAILS_ID, clickID);
        Log.i("MainActivity", "showEditReminder() reminder passed iwth id " + clickID);
        startActivityForResult(intent, ReminderDetailsActivity.EDIT_REMINDER_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MainActivity", "onActivityResult() returned with request code " + requestCode);
        adapter.setUpReminders(database.getAllReminders());
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
        for (Reminder reminder : reminders) {
            Log.i("MainActivity","onResume with " + reminder.getTitle());
        }
        if (reminders.isEmpty()) {
            noReminders.setVisibility(View.VISIBLE);
        } else {
            noReminders.setVisibility(View.GONE);
            adapter.setUpReminders(reminders);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                // Display the fragment as the main content.
//                getFragmentManager().beginTransaction()
//                        .addToBackStack(null)
//                        .replace(android.R.id.content, new Settings())
//                        .commit();
                startActivity(new Intent(MainActivity.this, Settings.class));
                return true;
            case android.R.id.home:
                onBackPressed();
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

        } else if (id == R.id.nav_reminders) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
