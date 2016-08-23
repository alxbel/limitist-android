package com.github.blackenwhite.costplanner.model;

import java.util.UUID;

public class Expense {
    private String id;
    private String limitDailyId;
    private String title;
    private String category;
    private int value;

    public Expense() {
        id = UUID.randomUUID().toString();
        category = "";
        title = "";
    }

    public Expense(String id, String limitDailyId, String title, String category, int value) {
        this.id = id;
        this.limitDailyId = limitDailyId;
        this.title = title;
        this.category = category;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", limitDailyId='" + limitDailyId + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLimitDailyId() {
        return limitDailyId;
    }

    public void setLimitDailyId(String limitDailyId) {
        this.limitDailyId = limitDailyId;
    }
}
