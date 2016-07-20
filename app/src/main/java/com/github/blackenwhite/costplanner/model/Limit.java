package com.github.blackenwhite.costplanner.model;

import android.util.Log;

public class Limit {
    private long mId;
    private int mMonth;
    private int mYear;
    private int mLimit;

    public Limit(int year, int month, int limit) {
        mYear = year;
        mMonth = month;
        mLimit = limit;
    }

    @Override
    public String toString() {
        return String.format("%s %d: %d", Date.INSTANCE.getMonth(mMonth), mYear, mLimit);
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        mLimit = limit;
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
}
