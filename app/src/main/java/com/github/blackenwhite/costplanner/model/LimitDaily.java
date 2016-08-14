package com.github.blackenwhite.costplanner.model;

public class LimitDaily {
    private String mId;
    private String mLimitMonthlyId;
    private int mDay;
    private int mLimitValue;
    private int mSpent;

    public LimitDaily(String limitMonthlyId, int limitValue, int day) {
        mLimitMonthlyId = limitMonthlyId;
        mLimitValue = limitValue;
        mDay = day;
        mId = String.format("%s%d", mLimitMonthlyId, mDay);
    }

    public LimitDaily(String Id, String limitMonthlyId, int day, int limitValue) {
        mId = Id;
        mLimitMonthlyId = limitMonthlyId;
        mDay = day;
        mLimitValue = limitValue;
    }

    @Override
    public String toString() {
        return "LimitDaily{" +
                "mId='" + mId + '\'' +
                ", mLimitMonthlyId='" + mLimitMonthlyId + '\'' +
                ", mLimitValue=" + mLimitValue +
                ", mDay=" + mDay +
                ", mSpent=" + mSpent +
                '}';
    }

    public int getDay() {
        return mDay;
    }

    public String getId() {
        return mId;
    }

    public String getLimitMonthlyId() {
        return mLimitMonthlyId;
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

    public void setId(String id) {
        mId = id;
    }

    public void setLimitMonthlyId(String limitMonthlyId) {
        mLimitMonthlyId = limitMonthlyId;
    }

    public int getBalance() {
        return getLimitValue() - getSpent();
    }

    public int getSpent() {
        return mSpent;
    }

    public void setSpent(int spent) {
        mSpent = spent;
    }

    public static String generateIdForCurrentDate() {
        String id = String.format("%s%s%s",
                DateManager.get().getCurrentYear(),
                DateManager.get().getCurrentMonthIndex(),
                DateManager.get().getCurrentDayOfMonth());
        return id;
    }
}
