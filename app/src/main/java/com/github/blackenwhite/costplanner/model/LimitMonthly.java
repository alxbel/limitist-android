package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.dao.file.Settings;

import java.util.ArrayList;
import java.util.List;


public class LimitMonthly {
    private static final String TAG = "LimitMonthly";
    private String id;
    private int month;
    private int year;
    private int limitValue;
    private int days;

    public LimitMonthly(){}

    public LimitMonthly(int year, int month, int limitValue) {
        id = String.format("%d%d", year, month);
        this.year = year;
        this.month = month;
        this.limitValue = limitValue;
        days = DateManager.get().getDaysInMonth(this.year, this.month);
    }

    public LimitMonthly(String id, int year, int month, int limitValue) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.limitValue = limitValue;
        days = DateManager.get().getDaysInMonth(this.year, this.month);
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(),
                "[id=%s] %s %d m:%d",
                id, DateManager.get().getMonth(month), year, limitValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LimitMonthly limitMonthly = (LimitMonthly) o;

        if (month != limitMonthly.month) return false;
        if (year != limitMonthly.year) return false;
        if (limitValue != limitMonthly.limitValue) return false;
        return id != null ? id.equals(limitMonthly.id) : limitMonthly.id == null;
    }

    public static String createID(Integer year, Integer month) {
        return String.format("%d%d", year, month);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }

    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return DateManager.get().getMonth(month);
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDays() {
        return days;
    }

    public int calculateLimitDailyValue() {
        return limitValue / days;
    }

    public List<LimitDaily> createDailyLimits() {
        List<LimitDaily> limitDailyList = new ArrayList<>();
        int limitValue = calculateLimitDailyValue();
        for (int day = 1; day <= days; day++) {
            LimitDaily limit = new LimitDaily(id, limitValue, day);
            limitDailyList.add(limit);
        }
        return limitDailyList;
    }

    public static String generateIdForCurrentDate() {
        String id = String.format("%s%s",
                DateManager.get().getCurrentYear(),
                DateManager.get().getCurrentMonthIndex());
        return id;
    }
}
