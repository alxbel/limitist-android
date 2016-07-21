package com.github.blackenwhite.costplanner.provider.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.blackenwhite.costplanner.model.Limit;
import com.github.blackenwhite.costplanner.provider.database.LimitDbSchema.LimitTable;

public class LimitCursorWrapper extends CursorWrapper {
    public LimitCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Limit getLimit() {
        String id = getString(getColumnIndex(LimitTable.Cols.ID));
        int year = getInt(getColumnIndex(LimitTable.Cols.YEAR));
        int monthIndex = getInt(getColumnIndex(LimitTable.Cols.MONTH));
        int limVal = getInt(getColumnIndex(LimitTable.Cols.LIMIT_VALUE));

        Limit limit = new Limit(id, year, monthIndex, limVal);

        return limit;
    }
}
