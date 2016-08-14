package com.github.blackenwhite.costplanner.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitDailyTable;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.ExpenseTable;

public class LimitDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "limits.db";
    private static LimitDbHelper instance;

    public static LimitDbHelper get(Context context) {
        if (instance == null) {
            instance = new LimitDbHelper(context);
        }
        return instance;
    }

    private LimitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLimitMonthlyTable(db);
        createLimitDailyTable(db);
        createExpenseTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    private void createLimitMonthlyTable(SQLiteDatabase db) {
        db.execSQL("create table " + LimitMonthlyTable.NAME + "(" +
                LimitMonthlyTable.Cols.ID + " text primary key, " +
                LimitMonthlyTable.Cols.YEAR + " integer, " +
                LimitMonthlyTable.Cols.MONTH + " integer, " +
                LimitMonthlyTable.Cols.LIMIT_VALUE + " integer" +
                ")"
        );
    }

    private void createLimitDailyTable(SQLiteDatabase db) {
        db.execSQL("create table " + LimitDailyTable.NAME + "(" +
                LimitDailyTable.Cols.ID + " text primary key, " +
                LimitDailyTable.Cols.LIMIT_MONTHLY_ID + " text, " +
                LimitDailyTable.Cols.DAY + " integer, " +
                LimitDailyTable.Cols.LIMIT_VALUE + " integer, " +
                LimitDailyTable.Cols.SPENT + " integer, " +
                "foreign key(" + LimitDailyTable.Cols.LIMIT_MONTHLY_ID + ") references " + LimitMonthlyTable.NAME + "(" + LimitMonthlyTable.Cols.ID + "))"
        );
    }

    private void createExpenseTable(SQLiteDatabase db) {
        db.execSQL("create table " + ExpenseTable.NAME + "(" +
                ExpenseTable.Cols.ID + " text primary key, " +
                ExpenseTable.Cols.LIMIT_DAILY_ID + " text, " +
                ExpenseTable.Cols.EXPENSE_VALUE + " integer, " +
                ExpenseTable.Cols.EXPENSE_TITLE + " text, " +
                ExpenseTable.Cols.EXPENSE_CATEGORY + " text, " +
                "foreign key(" + ExpenseTable.Cols.LIMIT_DAILY_ID + ") " +
                "references " + LimitDailyTable.NAME + "(" + LimitDailyTable.Cols.ID + "))"
        );
    }
}
