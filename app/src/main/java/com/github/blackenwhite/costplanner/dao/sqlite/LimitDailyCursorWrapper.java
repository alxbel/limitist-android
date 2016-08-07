package com.github.blackenwhite.costplanner.dao.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.blackenwhite.costplanner.model.LimitDaily;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitDailyTable;

public class LimitDailyCursorWrapper extends CursorWrapper {
    public LimitDailyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LimitDaily getLimitDaily() {
        String id = getString(getColumnIndex(LimitDailyTable.Cols.ID));
        String limitMonthlyID = getString(getColumnIndex(LimitDailyTable.Cols.LIMIT_MONTHLY_ID));
        int day = getInt(getColumnIndex(LimitDailyTable.Cols.DAY));
        int value = getInt(getColumnIndex(LimitDailyTable.Cols.LIMIT_VALUE));

        LimitDaily limitDaily = new LimitDaily();
        limitDaily.setID(id);
        limitDaily.setLimitMonthlyID(limitMonthlyID);
        limitDaily.setDay(day);
        limitDaily.setLimitValue(value);

        return limitDaily;
    }
}
