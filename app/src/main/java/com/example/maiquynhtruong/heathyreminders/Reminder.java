package com.example.maiquynhtruong.heathyreminders;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Job;

enum ReminderMethod {
    EMAIL, NOTIFICATION, ALARM
}
public class Reminder {
    String nameTag;
    String title;
    public int[] constraints;
    private int hour;
    private int minute;
    WeekDayList weekDayList;
    ReminderMethod method;

    public Reminder(String nameTag, String title) {
        this.nameTag = nameTag;
        this.title = title;
        constraints = new int[]{Constraint.DEVICE_CHARGING};
        hour = 12;
        minute = 0;
        weekDayList = new WeekDayList(127); // 127 for 1111111 aka everyday
        method = ReminderMethod.NOTIFICATION;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
