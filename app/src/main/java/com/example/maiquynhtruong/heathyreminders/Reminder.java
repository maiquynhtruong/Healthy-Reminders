package com.example.maiquynhtruong.heathyreminders;

public class Reminder {
    private int id;
    private String title;
    private int hour;
    private int minute;

    private int month;
    private int day;
    private int year;
    private int repeatNumber;
    private boolean repeat;

    public Reminder(String title, int hour, int minute, int month, int day, int year, boolean repeat, int repeatNumber) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.day = day;
        this.year = year;
        this.repeat = repeat;
        this.repeatNumber = repeatNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
