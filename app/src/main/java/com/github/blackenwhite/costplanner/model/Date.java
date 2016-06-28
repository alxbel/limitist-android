package com.github.blackenwhite.costplanner.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by alexbel on 6/28/16.
 */
public class Date {
    private static final String TAG = "Date";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

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

        try {
            LocalDate d = dtf.parseLocalDate(month.toLowerCase(mLocale));
            return d.getMonthOfYear();
        } catch (IllegalArgumentException e) {
            return DEFAULT_MONTH_INDEX;
        }
    }
}
