package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitMonthlyCursorWrapper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;
import com.github.blackenwhite.costplanner.util.ResourceManager;

public class LimitMonthlyStorage {
    private static final String TAG = LimitMonthlyStorage.class.getSimpleName();
    private static LimitMonthlyStorage instance;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LimitMonthlyStorage get(Context context) {
        if (instance == null) {
            instance = new LimitMonthlyStorage(context);
        }
        return instance;
    }

    private LimitMonthlyStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = LimitDbHelper.get(mContext).getWritableDatabase();
    }

    public void dPrintAllLimitsMonthly() {
        Log.d(TAG, "== Printing all limits ==");
        for (LimitMonthly limitMonthly : getLimitsMonthly()) {
            Log.d(TAG, limitMonthly.toString());
        }
        Log.d(TAG, "== End of output ==");
    }

    public boolean addLimitMonthly(LimitMonthly limitMonthly) {
        if (!isAllowed(limitMonthly.getLimitValue())) {
            return false;
        }
        ContentValues values = getContentValues(limitMonthly);
        mDatabase.insert(LimitMonthlyTable.NAME, null, values);

        return true;
    }

    public boolean updateLimitMonthly(LimitMonthly limitMonthly) {
        if (!isAllowed(limitMonthly.getLimitValue())) {
            return false;
        }
        ContentValues values = getContentValues(limitMonthly);
        mDatabase.update(LimitMonthlyTable.NAME, values,
                LimitMonthlyTable.Cols.ID + " = ?",
                new String[]{limitMonthly.getId()});

        return true;
    }

    public List<LimitMonthly> getLimitsMonthly() {
        List<LimitMonthly> limitsMonthly = new ArrayList<>();

        LimitMonthlyCursorWrapper cursor = queryLimitsMonthly(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limitsMonthly.add(cursor.getLimitMonthly());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limitsMonthly;
    }

    public List<LimitMonthly> getLimitsMonthly(int year) {
        List<LimitMonthly> limitsMonthly = new ArrayList<>();

        LimitMonthlyCursorWrapper cursor = queryLimitsMonthly(
                LimitMonthlyTable.Cols.YEAR + " = ?",
                new String[]{String.valueOf(year)}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limitsMonthly.add(cursor.getLimitMonthly());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limitsMonthly;
    }

    public List<LimitMonthly> getLimitsMonthly(int year, int monthStart) {
        List<LimitMonthly> limitsMonthly = new ArrayList<>();

        LimitMonthlyCursorWrapper cursor = queryLimitsMonthly(
                LimitMonthlyTable.Cols.YEAR + " = ? AND " +
                        LimitMonthlyTable.Cols.MONTH + " >= ?",
                new String[]{String.valueOf(year), String.valueOf(monthStart)}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limitsMonthly.add(cursor.getLimitMonthly());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limitsMonthly;
    }

    public LimitMonthly getLimitMonthly(String id) {
        LimitMonthlyCursorWrapper cursor = queryLimitsMonthly(
                LimitMonthlyTable.Cols.ID + " = ?",
                new String[]{id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLimitMonthly();
        } finally {
            cursor.close();
        }
    }

    public LimitMonthly getLimitMonthly(String year, String month) {
        LimitMonthlyCursorWrapper cursor = queryLimitsMonthly(
                LimitMonthlyTable.Cols.YEAR + " = ? AND " +
                        LimitMonthlyTable.Cols.MONTH + " = ?",
                new String[]{year, month}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLimitMonthly();
        } finally {
            cursor.close();
        }
    }

    private LimitMonthlyCursorWrapper queryLimitsMonthly(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LimitMonthlyTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new LimitMonthlyCursorWrapper(cursor);
    }

    private ContentValues getContentValues(LimitMonthly limitMonthly) {
        ContentValues values = new ContentValues();
        values.put(LimitMonthlyTable.Cols.ID, limitMonthly.getId());
        values.put(LimitMonthlyTable.Cols.YEAR, limitMonthly.getYear());
        values.put(LimitMonthlyTable.Cols.MONTH, limitMonthly.getMonth());
        values.put(LimitMonthlyTable.Cols.LIMIT_VALUE, limitMonthly.getLimitValue());
        return values;
    }

    private boolean isAllowed(int val) {
        return val >= ResourceManager.getInteger(R.integer.limit_minimum_allowed);
    }
}
