package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitCursorWrapper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;
import com.github.blackenwhite.costplanner.util.ResourceManager;

public class LimitStorage {
    private static final String TAG = LimitStorage.class.getSimpleName();
    private static LimitStorage sLimitStorage;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LimitStorage get(Context context) {
        if (sLimitStorage == null) {
            sLimitStorage = new LimitStorage(context);
        }
        return sLimitStorage;
    }

    private LimitStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LimitDbHelper(mContext).getWritableDatabase();
    }

    public void printAllLimits() {
        Log.d(TAG, "== Printing all limits ==");
        for (Limit limit : getLimits()) {
            Log.d(TAG, limit.toString());
        }
        Log.d(TAG, "== End of output ==");
    }

    public boolean addLimit(Limit limit) {
        if (!isAllowed(limit.getLimitMonthly())) {
            return false;
        }
//        Limit old = getLimit(limit.getId());
//        if (old != null) {
//            return old;
//        } else {
//            ContentValues values = getContentValues(limit);
//            mDatabase.insert(LimitMonthlyTable.NAME, null, values);
//        }
        ContentValues values = getContentValues(limit);
        mDatabase.insert(LimitMonthlyTable.NAME, null, values);

        return true;
    }

    public boolean updateLimit(Limit limit) {
        if (!isAllowed(limit.getLimitMonthly())) {
            return false;
        }
        ContentValues values = getContentValues(limit);
        mDatabase.update(LimitMonthlyTable.NAME, values,
                LimitMonthlyTable.Cols.ID + " = ?",
                new String[]{limit.getId()});

        return true;
    }

    public List<Limit> getLimits() {
        List<Limit> limits = new ArrayList<>();

        LimitCursorWrapper cursor = queryLimits(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limits.add(cursor.getLimit());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return limits;
    }

    public Limit getLimit(String id) {
        LimitCursorWrapper cursor = queryLimits(
                LimitMonthlyTable.Cols.ID + " = ?",
                new String[]{id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLimit();
        } finally {
            cursor.close();
        }
    }

    private LimitCursorWrapper queryLimits(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LimitMonthlyTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new LimitCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Limit limit) {
        ContentValues values = new ContentValues();
        values.put(LimitMonthlyTable.Cols.ID, limit.getId());
        values.put(LimitMonthlyTable.Cols.YEAR, limit.getYear());
        values.put(LimitMonthlyTable.Cols.MONTH, limit.getMonth());
        values.put(LimitMonthlyTable.Cols.LIMIT_MONTHLY, limit.getLimitMonthly());
        return values;
    }

    private boolean isAllowed(int val) {
        return val >= ResourceManager.getInteger(R.integer.limit_minimum_allowed);
    }
}
