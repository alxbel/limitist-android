package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.dao.file.Settings;

public class LimitDaily {
    private String mID;
    private String mLimitMonthlyID;
    private int mDay;
    private int mLimitValue;

    public LimitDaily(){}

    public LimitDaily(String limitMonthlyID, int limitValue, int day) {
        mLimitMonthlyID = limitMonthlyID;
        mLimitValue = limitValue;
        mDay = day;
        mID = String.format("%d%d", mLimitMonthlyID, mDay);
    }

    @Override
    public String toString() {
        return String.format(Settings.getLocale(),
                "[id=%s] d:%d",
                mID, mLimitValue);
    }

    public int getDay() {
        return mDay;
    }

    public String getID() {
        return mID;
    }

    public String getLimitMonthlyID() {
        return mLimitMonthlyID;
    }

    public int getLimitValue() {
        return mLimitValue;
    }

    public void setLimitValue(int limitValue) {
        mLimitValue = limitValue;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public void setLimitMonthlyID(String limitMonthlyID) {
        mLimitMonthlyID = limitMonthlyID;
    }
}
