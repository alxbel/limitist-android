package com.github.blackenwhite.costplanner.model;

import java.util.UUID;

public class Expense {
    private String mId;
    private String mLimitDailyId;
    private String mTitle;
    private String mCategory;
    private int mValue;

    public Expense() {
        mId = UUID.randomUUID().toString();
        mCategory = "";
        mTitle = "";
    }

    public Expense(String id, String limitDailyId, String title, String category, int value) {
        mId = id;
        mLimitDailyId = limitDailyId;
        mTitle = title;
        mCategory = category;
        mValue = value;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "mId='" + mId + '\'' +
                ", mLimitDailyId='" + mLimitDailyId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mValue=" + mValue +
                '}';
    }

    public String getId() {
        return mId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public String getLimitDailyId() {
        return mLimitDailyId;
    }

    public void setLimitDailyId(String limitDailyId) {
        mLimitDailyId = limitDailyId;
    }
}
