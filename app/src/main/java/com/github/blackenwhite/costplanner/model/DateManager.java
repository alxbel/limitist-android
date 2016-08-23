package com.github.blackenwhite.costplanner.model;

import android.content.Context;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.dao.file.Settings;
import com.github.blackenwhite.costplanner.util.Factory;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DateManager {
    private static DateManager dateManager;

    private static final String TAG = "DateManager";

    private static final String ERROR_MONTH_NAME = "#error_month_name#";
    private static final String RU_LOCALE = "ru";
    private static final int DEFAULT_MONTH_INDEX = 1;
    private static final String MONTH_FORMAT = "MMMM";
    private static final String DATE_FORMAT = "E dd.MM.yyyy";

    private Context context;
    private BiMap<Integer, String> monthColl;
    private DateTimeFormatter dateFormatter;
    private LocalDate localDate;
    private Locale locale;

    private DateManager(Context context) {
        this.context = context;
        localDate = new LocalDate();
        monthColl = new BiMap<>();
        dateFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        setLocale(Settings.getLangPref(this.context));
    }

    public static DateManager get() {
        return dateManager;
    }

    public static void init(Context context) {
        if (dateManager == null) {
            dateManager = new DateManager(context);
        }
    }

    public String getDate() {
        return dateFormatter.withLocale(locale).print(localDate);
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Locale getLocale() {
        return locale;
    }

//    public void setLocale(Locale locale) {
//        locale = locale;
//    }

    public void setLocale(String locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            return;
        }
        this.locale = new Locale(locale);
        initMonths();
    }

    public Integer getCurrentYear() {
        if (localDate == null) {
            localDate = new LocalDate();
        }
        return localDate.getYear();
    }

    public Integer getCurrentMonthIndex() {
        if (localDate == null) {
            localDate = new LocalDate();
        }
        return localDate.getMonthOfYear();
    }

    public Integer getCurrentDayOfMonth() {
        if (localDate == null) {
            localDate = new LocalDate();
        }
        return localDate.getDayOfMonth();
    }

    public String getCurrentMonth() {
        return getMonth(localDate.getMonthOfYear());
    }

    public Integer getMonthIndex(String month) {
        try {
            return monthColl.getKey(month);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return DEFAULT_MONTH_INDEX;
        }
    }

    public String getMonth(int index) {
        try {
            return monthColl.getValue(index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ERROR_MONTH_NAME;
        }
    }

    public int getDaysInMonth(int year, int month) {
        return new DateTime(year, month, 14, 12, 0, 0, 000).dayOfMonth().getMaximumValue();
    }

    public String[] getMonthNames() {
        return monthColl.getValues().toArray(new String[monthColl.size()]);
    }

    public String[] getRemainsMonthNames() {
        final List<String> all = monthColl.getValues();
        final int cur = getCurrentMonthIndex() - 1;
        String[] remains = Arrays.copyOfRange(all.toArray(new String[monthColl.size()]), cur, monthColl.size());
        return remains;
    }

    private void initMonths() {
        monthColl.clear();
        monthColl.putAll(Factory.getIntegers(1,12), context.getResources().getStringArray(R.array.month_array));
    }
}
