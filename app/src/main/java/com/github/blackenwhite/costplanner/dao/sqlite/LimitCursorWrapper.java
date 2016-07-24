package com.github.blackenwhite.costplanner.dao.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.blackenwhite.costplanner.model.Limit;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;

public class LimitCursorWrapper extends CursorWrapper {
    public LimitCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Limit getLimit() {
        String id = getString(getColumnIndex(LimitMonthlyTable.Cols.ID));
        int year = getInt(getColumnIndex(LimitMonthlyTable.Cols.YEAR));
        int month = getInt(getColumnIndex(LimitMonthlyTable.Cols.MONTH));
        int limitMonthly = getInt(getColumnIndex(LimitMonthlyTable.Cols.LIMIT_MONTHLY));

        Limit limit = new Limit();
        limit.setId(id);
        limit.setYear(year);
        limit.setMonth(month);
        limit.setLimitMonthly(limitMonthly);

        return limit;
    }
}
