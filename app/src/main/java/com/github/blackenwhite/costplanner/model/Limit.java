package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.dao.file.Settings;


public class Limit {
    private String mId;
    private int mMonth;
    private int mYear;
    private int mLimitMonthly;
    private int[] mLimitDailyArray;

    public Limit(){}

    public Limit(int year, int month, int limitMonthly) {
        mId = String.format("%d%d", year, month);
        mYear = year;
        mMonth = month;
        mLimitMonthly = limitMonthly;
//        setupDailyLimitArray();
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(),
                "[id=%s] %s %d m:%d",
                mId, DateManager.get().getMonth(mMonth), mYear, mLimitMonthly);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Limit limit = (Limit) o;

        if (mMonth != limit.mMonth) return false;
        if (mYear != limit.mYear) return false;
        if (mLimitMonthly != limit.mLimitMonthly) return false;
        return mId != null ? mId.equals(limit.mId) : limit.mId == null;
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

    public int getLimitMonthly() {
        return mLimitMonthly;
    }

    public void setLimitMonthly(int limitMonthly) {
        mLimitMonthly = limitMonthly;
    }

    public int getMonth() {
        return mMonth;
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

    private void setupDailyLimitArray() {
        final int days = DateManager.get().getDaysInMonth(mYear, mMonth);
        final Integer limitDaily = mLimitMonthly / days;
        mLimitDailyArray = new int[days];
        for (int i = 0; i < mLimitDailyArray.length; i++) {
            mLimitDailyArray[i] = limitDaily;
        }
    }
}
