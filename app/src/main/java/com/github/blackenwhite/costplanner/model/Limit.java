package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.provider.file.Settings;


public class Limit {
    private String mId;
    private int mMonth;
    private int mYear;
    private int mLimVal;

    public Limit(String id) {
        mId = id;
    }

    public Limit(int year, int month, int limVal) {
        mId = String.format("%d%d", year, month);
        mYear = year;
        mMonth = month;
        mLimVal = limVal;
    }

    public Limit(String id, int year, int month, int limVal) {
        mId = id;
        mLimVal = limVal;
        mMonth = month;
        mYear = year;
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(), "[id=%s] %s %d: %d", mId, Date.get().getMonth(mMonth), mYear, mLimVal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Limit limit = (Limit) o;

        if (mMonth != limit.mMonth) return false;
        if (mYear != limit.mYear) return false;
        if (mLimVal != limit.mLimVal) return false;
        return mId != null ? mId.equals(limit.mId) : limit.mId == null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getLimVal() {
        return mLimVal;
    }

    public void setLimVal(int limVal) {
        mLimVal = limVal;
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
