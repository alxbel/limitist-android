package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.dao.file.Settings;

import java.util.ArrayList;
import java.util.List;


public class LimitMonthly {
    private static final String TAG = "LimitMonthly";
    private String mId;
    private int mMonth;
    private int mYear;
    private int mLimitValue;
    private int mDays;

    public LimitMonthly(){}

    public LimitMonthly(int year, int month, int limitValue) {
        mId = String.format("%d%d", year, month);
        mYear = year;
        mMonth = month;
        mLimitValue = limitValue;
        mDays = DateManager.get().getDaysInMonth(mYear, mMonth);
    }

    public LimitMonthly(String id, int year, int month, int limitValue) {
        mId = id;
        mYear = year;
        mMonth = month;
        mLimitValue = limitValue;
        mDays = DateManager.get().getDaysInMonth(mYear, mMonth);
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(),
                "[id=%s] %s %d m:%d",
                mId, DateManager.get().getMonth(mMonth), mYear, mLimitValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LimitMonthly limitMonthly = (LimitMonthly) o;

        if (mMonth != limitMonthly.mMonth) return false;
        if (mYear != limitMonthly.mYear) return false;
        if (mLimitValue != limitMonthly.mLimitValue) return false;
        return mId != null ? mId.equals(limitMonthly.mId) : limitMonthly.mId == null;
    }

    public static String createID(Integer year, Integer month) {
        return String.format("%d%d", year, month);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getLimitValue() {
        return mLimitValue;
    }

    public void setLimitValue(int limitValue) {
        mLimitValue = limitValue;
    }

    public int getMonth() {
        return mMonth;
    }

    public String getMonthName() {
        return DateManager.get().getMonth(mMonth);
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getDays() {
        return mDays;
    }

    public int calculateLimitDailyValue() {
        return mLimitValue / mDays;
    }

    public List<LimitDaily> createDailyLimits() {
        List<LimitDaily> limitDailyList = new ArrayList<>();
        int limitValue = calculateLimitDailyValue();
        for (int day = 1; day <= mDays; day++) {
            LimitDaily limit = new LimitDaily(mId, limitValue, day);
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
