package com.github.blackenwhite.costplanner.model;

import android.content.Context;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.controller.MainActivity;
import com.github.blackenwhite.costplanner.dao.file.Settings;
import com.github.blackenwhite.costplanner.util.BiMap;
import com.github.blackenwhite.costplanner.util.Factory;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateManager {
    private static DateManager sDateManager;

    private static final String TAG = "DateManager";
    private static final String ERROR_MONTH_NAME = "#error_month_name#";
    private static final String RU_LOCALE = "ru";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private BiMap<Integer, String> mMonthColl;
    private DateTimeFormatter mDateFormatter;
    private LocalDate mLocalDate;
    private Locale mLocale;

    private Context mContext;


    private DateManager() {
        mContext = MainActivity.getContext();
        mLocalDate = new LocalDate();
        mMonthColl = new BiMap<>();
        mDateFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        setLocale(Settings.getLangPref(mContext));
    }

    public static DateManager get() {
        if (sDateManager == null) {
            sDateManager = new DateManager();
        }
        return sDateManager;
    }



    public String getDate() {
        return mDateFormatter.withLocale(mLocale).print(mLocalDate);
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
        if (mLocale != null && mLocale.equals(locale)) {
            return;
        }
        mLocale = new Locale(locale);
        initMonths();
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
            return mMonthColl.getKey(month.toLowerCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return DEFAULT_MONTH_INDEX;
        }
    }

    public String getMonth(int index) {
        try {
            String month = mMonthColl.getValue(index);
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
        return mMonthColl.getValues().toArray(new String[mMonthColl.size()]);
    }

    private void initMonths() {
        mMonthColl.clear();
        mMonthColl.putAll(Factory.getInts(1,12), mContext.getResources().getStringArray(R.array.month_array));
    }
}
