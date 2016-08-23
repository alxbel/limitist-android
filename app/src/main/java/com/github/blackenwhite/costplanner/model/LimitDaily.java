package com.github.blackenwhite.costplanner.model;

public class LimitDaily {
    private String id;
    private String limitMonthlyId;
    private int day;
    private int limitValue;
    private int spent;

    public LimitDaily(String limitMonthlyId, int limitValue, int day) {
        this.limitMonthlyId = limitMonthlyId;
        this.limitValue = limitValue;
        this.day = day;
        id = String.format("%s%d", this.limitMonthlyId, this.day);
    }

    public LimitDaily(String Id, String limitMonthlyId, int day, int limitValue) {
        id = Id;
        this.limitMonthlyId = limitMonthlyId;
        this.day = day;
        this.limitValue = limitValue;
    }

    @Override
    public String toString() {
        return "LimitDaily{" +
                "id='" + id + '\'' +
                ", limitMonthlyId='" + limitMonthlyId + '\'' +
                ", limitValue=" + limitValue +
                ", day=" + day +
                ", spent=" + spent +
                '}';
    }

    public int getDay() {
        return day;
    }

    public String getId() {
        return id;
    }

    public String getLimitMonthlyId() {
        return limitMonthlyId;
    }

    public int getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLimitMonthlyId(String limitMonthlyId) {
        this.limitMonthlyId = limitMonthlyId;
    }

    public int getBalance() {
        return getLimitValue() - getSpent();
    }

    public int getSpent() {
        return spent;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public static String generateIdForCurrentDate() {
        String id = String.format("%s%s%s",
                DateManager.get().getCurrentYear(),
                DateManager.get().getCurrentMonthIndex(),
                DateManager.get().getCurrentDayOfMonth());
        return id;
    }
}
