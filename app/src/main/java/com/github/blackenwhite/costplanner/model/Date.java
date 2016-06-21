package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.controller.InfoActivity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by alexbel on 6/20/16.
 */
public class Date {
    private static final String TAG = "Date";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private LocalDate mLocalDate;
    private Locale mLocale;

    public Date(Locale locale) {
        mLocalDate = new LocalDate();
        mLocale = locale;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
        return fmt.withLocale(mLocale).print(mLocalDate);
    }
}
