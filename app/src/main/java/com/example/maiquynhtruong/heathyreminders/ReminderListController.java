package com.example.maiquynhtruong.heathyreminders;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maiquynhtruong on 7/30/17.
 */

public class ReminderListController {
    ReminderListData data;

    public ReminderListController() {
        data = new ReminderListData();
    }

    public Reminder getReminderByPosition(int id) {
//        return data.idToReminder.get(id);
        Log.d("reminderList", "" + data.reminders.get(id));
        return data.reminders.get(0);
    }

    public void addReminder(String nameTag, String title) {
        Log.i("add-remidner", "name:" + title);
        data.reminders.add(new Reminder(nameTag, title));
    }
    public void removeReminder(int id) {
        Reminder reminder = data.idToReminder.get(id);

        int position = data.reminders.indexOf(reminder);
        data.reminders.remove(position);
        data.idToReminder.remove(id);
    }

    public interface ReminderListListener {
        public void onReminderChange(int position);
        public void onReminderInsert(int position);
        public void onReminderRemove(int position);
    }
    private class ReminderListData {
        public List<Reminder> reminders;
        public HashMap<Integer, Reminder> idToReminder;
        public ReminderListData() {
            reminders = new LinkedList<>();
            idToReminder = new HashMap<>();
        }
    }
}
