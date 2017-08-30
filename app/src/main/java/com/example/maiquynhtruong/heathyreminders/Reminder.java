package com.example.maiquynhtruong.heathyreminders;

public class Reminder {
    public static final String HOURLY = "everyhour";
    public static final String DAILY = "everyday";
    public static final String WEEKLY = "everyweek";
    public static final String MONTHLY = "everymonth";
    public static final String YEARLY = "everyyear";

    private int id;
    private String title;
    private int hour;
    private int minute;
    private int month;
    private int day;
    private int year;
    private boolean repeat;
    private int repeatNumber;
    private String repeatType;

    public Reminder(String title, int hour, int minute, int month, int day, int year, boolean repeat, int repeatNumber, String repeatType) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.day = day;
        this.year = year;
        this.repeat = repeat;
        this.repeatNumber = repeatNumber;
        this.repeatType = repeatType;
    }

    public Reminder(int id, String title, int hour, int minute, int month, int day, int year, boolean repeat, int repeatNumber, String repeatType) {
        this.id = id;
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.day = day;
        this.year = year;
        this.repeat = repeat;
        this.repeatNumber = repeatNumber;
        this.repeatType = repeatType;
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

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

}
