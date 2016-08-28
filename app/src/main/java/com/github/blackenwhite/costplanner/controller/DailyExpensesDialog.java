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

public class DailyExpensesDialog extends AlertDialog {

    private static final String TAG = "DailyExpensesDialog";
    private AlertDialog dialog;
    private List<Expense> expensesToDelete;
    private ExpensesActivity expensesActivity;
    private Activity activity;

    protected DailyExpensesDialog(ExpensesActivity expensesActivity, List<Expense> expenses) {
        super(expensesActivity.getActivity());

        this.expensesActivity = expensesActivity;
        activity = expensesActivity.getActivity();
        expensesToDelete = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title_expenses);
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout expensesView = (LinearLayout) inflater.inflate(R.layout.dialog_expenses, null);
        ListView expenseListView = (ListView) expensesView.findViewById(R.id.dialog_expenses_list);

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(activity, new ArrayList<>(expenses));

        expenseListView.setAdapter(expenseAdapter);
        builder.setView(expensesView);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DailyExpensesDialog.this.expensesActivity.killEmAll(expensesToDelete);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cancel();
            }
        });

        dialog = builder.create();
    }

    @Override
    public void show() {
        dialog.show();
    }

    class ExpenseAdapter extends ArrayAdapter<Expense> {

        public ExpenseAdapter(Context context, List<Expense> expenses) {
            super(context, -1, expenses);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_expense, parent, false);
            TextView categoryView = (TextView) rowView.findViewById(R.id.item_expense_category);
            TextView titleView = (TextView) rowView.findViewById(R.id.item_expense_title);
            TextView valueView = (TextView) rowView.findViewById(R.id.item_expense_value);
            ImageView trashView = (ImageView) rowView.findViewById(R.id.item_expense_delete);

            categoryView.setText(getItem(position).getCategory());
            titleView.setText(String.valueOf(getItem(position).getTitle()));
            valueView.setText(String.valueOf(getItem(position).getValue()));
            trashView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Expense del = getItem(position);
                    remove(del);
                    expensesToDelete.add(del);
                }
            });

            return rowView;
        }
    }
}
