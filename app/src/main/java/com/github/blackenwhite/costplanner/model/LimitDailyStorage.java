package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.blackenwhite.costplanner.dao.sqlite.LimitDailyCursorWrapper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema;

import java.util.ArrayList;
import java.util.List;

public class LimitDailyStorage {
    private static final String TAG = "LimitDailyStorage";
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

    public void dPrintAllLimitsDaily() {
        Log.d(TAG, "== Printing all daily limits ==");
        for (LimitDaily limitDaily : getLimitsDaily()) {
            Log.d(TAG, limitDaily.toString());
        }
        Log.d(TAG, "== End of output ==");
    }

    public void dPrintAllLimitsDaily(String limitMonthlyId) {
        LimitMonthly limitMonthly = LimitMonthlyStorage.get(mContext).getLimitMonthly(limitMonthlyId);
        Log.d(TAG, "== Printing all daily limits for [" + limitMonthly.getYear() + " " + limitMonthly.getMonthName() + "] ==");
        for (LimitDaily limitDaily : getLimitsDaily(limitMonthlyId)) {
            Log.d(TAG, limitDaily.toString());
        }
        Log.d(TAG, "== End of output ==");
    }

    public void addLimits(List<LimitDaily> limits) {
        mDatabase.beginTransaction();
        for (LimitDaily limit : limits) {
            ContentValues values = getContentValues(limit);
            mDatabase.insert(LimitDbSchema.LimitDailyTable.NAME, null, values);
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void updateLimits(List<LimitDaily> limits) {
        mDatabase.beginTransaction();
        for (LimitDaily limit : limits) {
            ContentValues contentValue = getContentValuesLimitValue(limit);
            mDatabase.update(
                    LimitDbSchema.LimitDailyTable.NAME,
                    contentValue,
                    LimitDbSchema.LimitDailyTable.Cols.ID + " = ?",
                    new String[]{limit.getId()});
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void updateLimit(LimitDaily limitDaily) {
        Log.d(TAG, limitDaily.toString());
        ContentValues contentValue = getContentValuesLimitSpent(limitDaily);
        mDatabase.update(
                LimitDbSchema.LimitDailyTable.NAME,
                contentValue,
                LimitDbSchema.LimitDailyTable.Cols.ID + " = ?",
                new String[]{limitDaily.getId()}
        );
    }

    public List<LimitDaily> getLimitsDaily() {
        List<LimitDaily> limitsDaily = new ArrayList<>();

        LimitDailyCursorWrapper cursor = queryLimitsDaily(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limitsDaily.add(cursor.getLimitDaily());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limitsDaily;
    }

    public List<LimitDaily> getLimitsDaily(String monthlyLimitId) {
        List<LimitDaily> limitsDaily = new ArrayList<>();

        LimitDailyCursorWrapper cursor = queryLimitsDaily(
                LimitDbSchema.LimitDailyTable.Cols.LIMIT_MONTHLY_ID + " = ?",
                new String[]{monthlyLimitId}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limitsDaily.add(cursor.getLimitDaily());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limitsDaily;
    }

    public LimitDaily getLimitDaily(String id) {
        LimitDailyCursorWrapper cursor = queryLimitsDaily(
                LimitDbSchema.LimitDailyTable.Cols.ID + " = ?",
                new String[]{id}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLimitDaily();
        } finally {
            cursor.close();
        }
    }

    private LimitDailyCursorWrapper queryLimitsDaily(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LimitDbSchema.LimitDailyTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new LimitDailyCursorWrapper(cursor);
    }

    private ContentValues getContentValues(LimitDaily limitDaily) {
        ContentValues values = new ContentValues();
        values.put(LimitDbSchema.LimitDailyTable.Cols.ID, limitDaily.getId());
        values.put(LimitDbSchema.LimitDailyTable.Cols.LIMIT_MONTHLY_ID, limitDaily.getLimitMonthlyId());
        values.put(LimitDbSchema.LimitDailyTable.Cols.DAY, limitDaily.getDay());
        values.put(LimitDbSchema.LimitDailyTable.Cols.LIMIT_VALUE, limitDaily.getLimitValue());
        return values;
    }

    private ContentValues getContentValuesLimitValue(LimitDaily limitDaily) {
        ContentValues values = new ContentValues();
        values.put(LimitDbSchema.LimitDailyTable.Cols.LIMIT_VALUE, limitDaily.getLimitValue());
        return values;
    }

    private ContentValues getContentValuesLimitSpent(LimitDaily limitDaily) {
        ContentValues values = new ContentValues();
        values.put(LimitDbSchema.LimitDailyTable.Cols.SPENT, limitDaily.getSpent());
        return values;
    }
}
