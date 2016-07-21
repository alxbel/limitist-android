package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.github.blackenwhite.costplanner.provider.database.LimitCursorWrapper;
import com.github.blackenwhite.costplanner.provider.database.LimitDbHelper;
import com.github.blackenwhite.costplanner.provider.database.LimitDbSchema.LimitTable;

public class LimitStorage {
    public static final String TAG = LimitStorage.class.getSimpleName();
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

    public Limit addLimit(Limit limit) {
        Limit old = getLimit(limit.getId());
        if (old != null) {
            return old;
        } else {
            ContentValues values = getContentValues(limit);
            mDatabase.insert(LimitTable.NAME, null, values);
        }
        return null;
    }

    public void updateLimit(Limit limit) {
        ContentValues values = getContentValues(limit);
        mDatabase.update(LimitTable.NAME, values,
                LimitTable.Cols.ID + " = ?",
                new String[]{limit.getId()});
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
                LimitTable.Cols.ID + " = ?",
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
                LimitTable.NAME,
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
        values.put(LimitTable.Cols.ID, limit.getId());
        values.put(LimitTable.Cols.YEAR, limit.getYear());
        values.put(LimitTable.Cols.MONTH, limit.getMonth());
        values.put(LimitTable.Cols.LIMIT_VALUE, limit.getLimVal());
        return values;
    }
}
