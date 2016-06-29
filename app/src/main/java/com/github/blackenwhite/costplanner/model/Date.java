package com.github.blackenwhite.costplanner.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by alexbel on 6/28/16.
 */
public class Date {
    private static final String TAG = "Date";
    private static final String RU_LOCALE = "ru";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private static Map<String, Integer> months;
    static {
        months = new HashMap<>();

        months.put("январь",    1);
        months.put("февраль",   2);
        months.put("март",      3);
        months.put("апрель",    4);
        months.put("май",       5);
        months.put("июнь",      6);
        months.put("июль",      7);
        months.put("август",    8);
        months.put("сентябрь",  9);
        months.put("октябрь",   10);
        months.put("ноябрь",    11);
        months.put("декабрь",   12);
    }


    public static final Date INSTANCE = new Date();

    private Date() {
        mLocalDate = new LocalDate();
    }

    private LocalDate mLocalDate;
    private Locale mLocale;

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

    public int getMonthIndex(String month) {
        DateTimeFormatter dtf =
                DateTimeFormat.forPattern(MONTH_FORMAT).withLocale(mLocale);

        int index = DEFAULT_MONTH_INDEX;
        try {
            if (mLocale.toString().equals(RU_LOCALE)) {
                index = months.get(month.toLowerCase());
            } else {
                LocalDate d = dtf.parseLocalDate(month);
                index = d.getMonthOfYear();
            }
        } catch (IllegalArgumentException e) {}
        return index;
    }
}
