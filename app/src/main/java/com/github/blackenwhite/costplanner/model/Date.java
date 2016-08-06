package com.github.blackenwhite.costplanner.model;

import com.github.blackenwhite.costplanner.controller.MainActivity;
import com.github.blackenwhite.costplanner.dao.file.Settings;
import com.github.blackenwhite.costplanner.util.BiMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class Date {
    private static Date sDate;

    private static final String TAG = "Date";
    private static final String ERROR_MONTH_NAME = "#error_month_name#";
    private static final String RU_LOCALE = "ru";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private static final BiMap<Integer, String> sMonthCollRu = new BiMap<>();
    private static final BiMap<Integer, String> sMonthCollEn = new BiMap<>();
    static {
        sMonthCollRu.put(1, "январь");
        sMonthCollRu.put(2, "февраль");
        sMonthCollRu.put(3, "март");
        sMonthCollRu.put(4, "апрель");
        sMonthCollRu.put(5, "май");
        sMonthCollRu.put(6, "июнь");
        sMonthCollRu.put(7, "июль");
        sMonthCollRu.put(8, "август");
        sMonthCollRu.put(9, "сентябрь");
        sMonthCollRu.put(10, "октябрь");
        sMonthCollRu.put(11, "ноябрь");
        sMonthCollRu.put(12, "декабрь");


        sMonthCollEn.put(1, "january");
        sMonthCollEn.put(2, "february");
        sMonthCollEn.put(3, "march");
        sMonthCollEn.put(4, "april");
        sMonthCollEn.put(5, "may");
        sMonthCollEn.put(6, "june");
        sMonthCollEn.put(7, "july");
        sMonthCollEn.put(8, "august");
        sMonthCollEn.put(9, "september");
        sMonthCollEn.put(10, "october");
        sMonthCollEn.put(11, "november");
        sMonthCollEn.put(12, "december");
    }

    private LocalDate mLocalDate = new LocalDate();
    private Locale mLocale;

    private Date() {}

    public static Date get() {
        if (sDate == null) {
            sDate = new Date();
            sDate.setLocale(Settings.getLangPref(MainActivity.getContext()));
        }
        return sDate;
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
        try {
            if (mLocale.toString().equals(RU_LOCALE)) {
                return sMonthCollRu.getKey(month.toLowerCase());
            } else {
                return sMonthCollEn.getKey(month.toLowerCase());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return DEFAULT_MONTH_INDEX;
        }
    }

    public String getMonth(int index) {
        try {
            String month;
            if (mLocale.toString().equals(RU_LOCALE)) {
                month = sMonthCollRu.getValue(index);
            } else {
                month = sMonthCollEn.getValue(index);
            }
            char c = month.charAt(0);
            return String.valueOf(Character.toUpperCase(c)) + month.substring(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ERROR_MONTH_NAME;
        }
    }

    public int getDaysInMonth(int year, int month) {
        return new DateTime(year, month, 14, 12, 0, 0, 000).dayOfMonth().getMaximumValue();
    }

    public String[] getMonthNames() {
        return sMonthCollRu.getValues().toArray(new String[sMonthCollRu.size()]);
    }
}
