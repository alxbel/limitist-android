package com.github.blackenwhite.costplanner.dao.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.blackenwhite.costplanner.model.Expense;
import com.github.blackenwhite.costplanner.dao.sqlite.LimitDbSchema.ExpenseTable;

public class ExpenseCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ExpenseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Expense getExpense() {
        String id = getString(getColumnIndex(ExpenseTable.Cols.ID));
        String limitDailyId = getString(getColumnIndex(ExpenseTable.Cols.LIMIT_DAILY_ID));
        String title = getString(getColumnIndex(ExpenseTable.Cols.EXPENSE_TITLE));
        String category = getString(getColumnIndex(ExpenseTable.Cols.EXPENSE_CATEGORY));
        int value = getInt(getColumnIndex(ExpenseTable.Cols.EXPENSE_VALUE));

        Expense expense = new Expense(id, limitDailyId, title, category, value);
        return expense;
    }
}
