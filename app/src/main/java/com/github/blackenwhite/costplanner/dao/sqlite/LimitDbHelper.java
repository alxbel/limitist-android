package com.github.blackenwhite.costplanner.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitDailyTable;

public class LimitDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "limits.db";

    public LimitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LimitMonthlyTable.NAME + "(" +
                LimitMonthlyTable.Cols.ID + " text primary key, " +
                LimitMonthlyTable.Cols.YEAR + " integer, " +
                LimitMonthlyTable.Cols.MONTH + " integer, " +
                LimitMonthlyTable.Cols.LIMIT_MONTHLY + " integer" +
                " )"
        );

        db.execSQL("create table " + LimitDailyTable.NAME + "(" +
                LimitDailyTable.Cols.ID + " integer primary key autoincrement, " +
                LimitDailyTable.Cols.MONTH_ID + " text, " +
                LimitDailyTable.Cols.DAY + ", " +
                LimitDailyTable.Cols.LIMIT_DAILY + ", " +
                "foreign key(" + LimitDailyTable.Cols.MONTH_ID + ") references " + LimitMonthlyTable.NAME + "(" + LimitMonthlyTable.Cols.ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
