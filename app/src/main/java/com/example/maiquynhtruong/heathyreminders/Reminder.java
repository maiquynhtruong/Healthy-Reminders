package com.example.maiquynhtruong.heathyreminders;

import com.firebase.jobdispatcher.Job;

enum ReminderMethod {
    EMAIL, NOTIFICATION, ALARM
}
public class Reminder {
    String nameTag;
    public int[] constraints;
    private int hour;
    private int minute;
    WeekDayList weekDayList;
    ReminderMethod method = ReminderMethod.NOTIFICATION;

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public int[] getConstraints() {
        return constraints;
    }

    public void setConstraints(int[] constraints) {
        this.constraints = constraints;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
