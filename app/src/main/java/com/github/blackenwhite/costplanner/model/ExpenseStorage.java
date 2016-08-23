package com.github.blackenwhite.costplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.blackenwhite.costplanner.dao.sqlite.ExpenseCursorWrapper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbHelper;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema;

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
                LimitDbSchema.ExpenseTable.Cols.LIMIT_DAILY_ID + " = ?",
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
        return database.insert(LimitDbSchema.ExpenseTable.NAME, null, values);
    }

    public void deleteExpenses(List<Expense> expenses) {
        database.beginTransaction();
        for (Expense expense : expenses) {
            database.delete(
                    LimitDbSchema.ExpenseTable.NAME,
                    LimitDbSchema.ExpenseTable.Cols.ID + " = ?",
                    new String[]{expense.getId()});
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public Integer getSum(String dailyId) {
        Cursor cursor = database.rawQuery(
                "SELECT SUM(" + LimitDbSchema.ExpenseTable.Cols.EXPENSE_VALUE + ") FROM " +
                        LimitDbSchema.ExpenseTable.NAME + " WHERE " + LimitDbSchema.ExpenseTable.Cols.LIMIT_DAILY_ID + " = ?",
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

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                LimitDbSchema.ExpenseTable.NAME,
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
        values.put(LimitDbSchema.ExpenseTable.Cols.ID, expense.getId());
        values.put(LimitDbSchema.ExpenseTable.Cols.LIMIT_DAILY_ID, expense.getLimitDailyId());
        values.put(LimitDbSchema.ExpenseTable.Cols.EXPENSE_TITLE, expense.getTitle());
        values.put(LimitDbSchema.ExpenseTable.Cols.EXPENSE_CATEGORY, expense.getCategory());
        values.put(LimitDbSchema.ExpenseTable.Cols.EXPENSE_VALUE, expense.getValue());
        return values;
    }
}
