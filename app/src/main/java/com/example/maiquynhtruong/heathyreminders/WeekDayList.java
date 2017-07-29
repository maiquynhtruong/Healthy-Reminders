package com.example.maiquynhtruong.heathyreminders;

/**
 * Created by maiquynhtruong on 7/29/17.
 */

public class WeekDayList {
    private boolean[] weekdays;
    public final WeekDayList EVERY_DAY = new WeekDayList(127);
    public WeekDayList(int bitMap) {
        weekdays = new boolean[7];
        int current = 1;
        for (int i = 0; i < 7; i++) {
            if ((current & bitMap) != 0) weekdays[i] = true;
            current <<= 1;
        }
    }
}