package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.blackenwhite.costplanner.dao.sqlite.ExpenseCursorWrapper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.ExpenseTable;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitDailyTable;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.LimitMonthlyTable;

import java.util.ArrayList;
import java.util.List;

public class ExpenseStorage {
    private static final String TAG = "ExpenseStorage";
    private static ExpenseStorage instance;

    private SQLiteDatabase database;

    public static ExpenseStorage get(Context context) {
        if (instance == null) {
            instance = new ExpenseStorage(context);
        }
        return instance;
    }

    private ExpenseStorage(Context context) {
        database = LimitDbHelper.get(context).getWritableDatabase();
    }

    public void printExpensesForDailyLimit(String dailyId) {
        Log.d(TAG, "== Printing all expenses for daily limit [id=" + dailyId + "]");
        for (Expense expense : getExpenses(dailyId)) {
            Log.d(TAG, expense.toString());
        }
        Log.d(TAG, "== End of output ==");
    }

    public List<Expense> getExpenses(String dailyId) {
        List<Expense> expenses = new ArrayList<>();

        ExpenseCursorWrapper cursor = queryExpenses(
                ExpenseTable.Cols.LIMIT_DAILY_ID + " = ?",
                new String[]{dailyId}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return expenses;
    }

    public long addExpense(Expense expense) {
        ContentValues values = getContentValues(expense);
        return database.insert(ExpenseTable.NAME, null, values);
    }

    public void deleteExpenses(List<Expense> expenses) {
        database.beginTransaction();
        for (Expense expense : expenses) {
            database.delete(
                    ExpenseTable.NAME,
                    ExpenseTable.Cols.ID + " = ?",
                    new String[]{expense.getId()});
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public Integer getDailySum(String dailyId) {
        Cursor cursor = database.rawQuery(
                "SELECT SUM(" + ExpenseTable.Cols.EXPENSE_VALUE + ") FROM " +
                        ExpenseTable.NAME + " WHERE " + ExpenseTable.Cols.LIMIT_DAILY_ID + " = ?",
                new String[]{dailyId});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    public Integer getMonthlySpent(String monthlyId) {
        Cursor cursor = database.rawQuery(
                String.format("SELECT SUM(%s.%s) " +
                        "FROM %s " +
                        "JOIN %s " +
                        "ON %s.%s = %s.%s " +
                        "JOIN %s " +
                        "ON %s.%s = %s.%s " +
                        "WHERE %s.%s = ?",
                        ExpenseTable.NAME, ExpenseTable.Cols.EXPENSE_VALUE,
                        ExpenseTable.NAME,
                        LimitDailyTable.NAME,
                        LimitDailyTable.NAME, LimitDailyTable.Cols.ID,
                        ExpenseTable.NAME, ExpenseTable.Cols.LIMIT_DAILY_ID,
                        LimitMonthlyTable.NAME,
                        LimitMonthlyTable.NAME, LimitMonthlyTable.Cols.ID,
                        LimitDailyTable.NAME, LimitDailyTable.Cols.LIMIT_MONTHLY_ID,
                        LimitMonthlyTable.NAME, LimitMonthlyTable.Cols.ID),
                new String[]{monthlyId});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                ExpenseTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ExpenseCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.Cols.ID, expense.getId());
        values.put(ExpenseTable.Cols.LIMIT_DAILY_ID, expense.getLimitDailyId());
        values.put(ExpenseTable.Cols.EXPENSE_TITLE, expense.getTitle());
        values.put(ExpenseTable.Cols.EXPENSE_CATEGORY, expense.getCategory());
        values.put(ExpenseTable.Cols.EXPENSE_VALUE, expense.getValue());
        return values;
    }
}
