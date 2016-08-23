package com.github.blackenwhite.costplanner.dao.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.blackenwhite.costplanner.model.LimitMonthly;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;

public class LimitMonthlyCursorWrapper extends CursorWrapper {
    public LimitMonthlyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LimitMonthly getLimitMonthly() {
        String id = getString(getColumnIndex(LimitMonthlyTable.Cols.ID));
        int year = getInt(getColumnIndex(LimitMonthlyTable.Cols.YEAR));
        int month = getInt(getColumnIndex(LimitMonthlyTable.Cols.MONTH));
        int limit = getInt(getColumnIndex(LimitMonthlyTable.Cols.LIMIT_VALUE));

        LimitMonthly limitMonthly = new LimitMonthly(id, year, month, limit);

        return limitMonthly;
    }
}
