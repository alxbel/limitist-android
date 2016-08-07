package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.dao.file.Settings;


public class LimitMonthly {
    private String mID;
    private int mMonth;
    private int mYear;
    private int mLimitValue;

    public LimitMonthly(){}

    public LimitMonthly(int year, int month, int limitValue) {
        mID = String.format("%d%d", year, month);
        mYear = year;
        mMonth = month;
        mLimitValue = limitValue;
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(),
                "[id=%s] %s %d m:%d",
                mID, DateManager.get().getMonth(mMonth), mYear, mLimitValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LimitMonthly limitMonthly = (LimitMonthly) o;

        if (mMonth != limitMonthly.mMonth) return false;
        if (mYear != limitMonthly.mYear) return false;
        if (mLimitValue != limitMonthly.mLimitValue) return false;
        return mID != null ? mID.equals(limitMonthly.mID) : limitMonthly.mID == null;
    }

    public static String createID(Integer year, Integer month) {
        return String.format("%d%d", year, month);
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
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
