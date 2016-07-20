package com.github.blackenwhite.costplanner.model;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Date {
    public static final Date INSTANCE = new Date();

    private static final String TAG = "Date";
    private static final String RU_LOCALE = "ru";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private static Map<String, Integer> sMonths;
    static {
        sMonths = new HashMap<>();

        sMonths.put("январь",    1);
        sMonths.put("февраль",   2);
        sMonths.put("март",      3);
        sMonths.put("апрель",    4);
        sMonths.put("май",       5);
        sMonths.put("июнь",      6);
        sMonths.put("июль",      7);
        sMonths.put("август",    8);
        sMonths.put("сентябрь",  9);
        sMonths.put("октябрь",   10);
        sMonths.put("ноябрь",    11);
        sMonths.put("декабрь",   12);
    }

    private LocalDate mLocalDate;
    private Locale mLocale;

    private Date() {
        mLocalDate = new LocalDate();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
        return fmt.withLocale(mLocale).print(mLocalDate);
    }

    public LocalDate getLocalDate() {
        return mLocalDate;
    }

    public void setLocalDate(LocalDate localDate) {
        mLocalDate = localDate;
    }

    public Locale getLocale() {
        return mLocale;
    }

    public void setLocale(Locale locale) {
        mLocale = locale;
    }

    public void setLocale(String locale) {
        mLocale = new Locale(locale);
    }

    public int getCurrentYear() {
        return mLocalDate.getYear();
    }

    public int getCurrentMonthIndex() {
        return mLocalDate.getMonthOfYear();
    }

    public String getCurrentMonth() {
        return getMonth(mLocalDate.getMonthOfYear());
    }

    public int getMonthIndex(String month) {
        DateTimeFormatter dtf =
                DateTimeFormat.forPattern(MONTH_FORMAT).withLocale(mLocale);

        int index = DEFAULT_MONTH_INDEX;
        try {
            if (mLocale.toString().equals(RU_LOCALE)) {
                index = sMonths.get(month.toLowerCase());
            } else {
                LocalDate d = dtf.parseLocalDate(month);
                index = d.getMonthOfYear();
            }
        } catch (IllegalArgumentException e) {}
        return index;
    }

    public String getMonth(int index) {
        String month = "month";
        try {
            if (mLocale.toString().equals(RU_LOCALE)) {
                for (Map.Entry<String, Integer> entry : sMonths.entrySet()) {
                    if (entry.getValue() == index) {
                        month = entry.getKey();
                        break;
                    }
                }
            } else {
                YearMonth ym = new YearMonth(1990, index);
                month = ym.monthOfYear().getAsText(mLocale);
            }
        } catch (IllegalArgumentException e) { e.printStackTrace(); }

        char c = month.charAt(0);
        return new StringBuilder().append(Character.toUpperCase(c)).append(month.substring(1)).toString();
    }
}
