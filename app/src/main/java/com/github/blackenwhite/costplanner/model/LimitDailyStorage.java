package com.github.blackenwhite.costplanner.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;

public class LimitDailyStorage {
    private static LimitDailyStorage instance;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LimitDailyStorage get(Context context) {
        if (instance == null) {
            instance = new LimitDailyStorage(context);
        }
        return instance;
    }

    private LimitDailyStorage(Context context) {
        mContext = context;
        mDatabase = LimitDbHelper.get(context).getWritableDatabase();
    }
}
