package com.github.blackenwhite.costplanner.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDialog extends AlertDialog {

    private static final String TAG = "ExpensesDialog";
    private AlertDialog mDialog;
    private List<Expense> mExpensesToDelete;
    private ExpensesActivity mExpensesActivity;
    private Activity mActivity;

    protected ExpensesDialog(ExpensesActivity expensesActivity, List<Expense> expenses) {
        super(expensesActivity.getActivity());

        mExpensesActivity = expensesActivity;
        mActivity = expensesActivity.getActivity();
        mExpensesToDelete = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.dialog_title_expenses);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        LinearLayout expensesView = (LinearLayout) inflater.inflate(R.layout.dialog_expenses, null);
        ListView expenseListView = (ListView) expensesView.findViewById(R.id.dialog_expenses_list);

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(mActivity, expenses);

        expenseListView.setAdapter(expenseAdapter);
        builder.setView(expensesView);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mExpensesActivity.killEmAll(mExpensesToDelete);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mExpensesActivity.updateExpenseListForDailyLimit();
            }
        });

        mDialog = builder.create();
    }

    @Override
    public void show() {
        mDialog.show();
    }

    class ExpenseAdapter extends ArrayAdapter<Expense> {
        List<Expense> mExpenses;

        public ExpenseAdapter(Context context, List<Expense> expenses) {
            super(context, -1, expenses);
            mExpenses = expenses;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_expense, parent, false);
            TextView categoryView = (TextView) rowView.findViewById(R.id.item_expense_category);
            TextView titleView = (TextView) rowView.findViewById(R.id.item_expense_title);
            TextView valueView = (TextView) rowView.findViewById(R.id.item_expense_value);
            ImageView trashView = (ImageView) rowView.findViewById(R.id.item_expense_delete);

            categoryView.setText(mExpenses.get(position).getCategory());
            titleView.setText(String.valueOf(mExpenses.get(position).getTitle()));
            valueView.setText(String.valueOf(mExpenses.get(position).getValue()));
            trashView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Expense del = mExpenses.get(position);
                    remove(del);
                    mExpenses.remove(del);
                    mExpensesToDelete.add(del);
                }
            });

            return rowView;
        }
    }
}
