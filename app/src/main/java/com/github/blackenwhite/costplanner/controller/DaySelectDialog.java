package com.github.blackenwhite.costplanner.controller;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;

public class DaySelectDialog extends AlertDialog {

    private static final String TAG = "DaySelectDialog";
    private static final int WEEKS = 4;
    private AlertDialog dialog;
    final float screenDensity;

    protected DaySelectDialog(final ExpensesActivity expensesActivity, final int daySelected) {
        super(expensesActivity.getActivity());
        Activity activity = expensesActivity.getActivity();
        screenDensity = activity.getResources().getDisplayMetrics().density;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_day_select_title);
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout daySelectLayout = (LinearLayout) inflater.inflate(R.layout.dialog_day_select, null);
        TableLayout daySelectTable = (TableLayout) daySelectLayout.findViewById(R.id.table_day_select);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPixels(10), dpToPixels(10), dpToPixels(10), dpToPixels(10));
        daySelectTable.setLayoutParams(params);

        final int days = expensesActivity.getDays();
        for (int i = 0; i < days; ) {
            TableRow row = new TableRow(activity);

            for (int k = 0; k < days / WEEKS; k++) {
                if (i == days) {
                    break;
                }
                final TextView dayView = new TextView(activity);
                dayView.setPadding(dpToPixels(5), dpToPixels(5), 0, 0);
                dayView.setText(String.valueOf(i+1));
                dayView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                dayView.setTextSize(dpToPixels(7));
                if (i == daySelected-1) {
                    dayView.setBackgroundColor(Color.parseColor("#b9b7b7"));
                }
                dayView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dayView.setBackgroundColor(Color.parseColor("#b9b7b7"));
                        return false;
                    }
                });
                dayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expensesActivity.setViewForDay(Integer.valueOf(dayView.getText().toString()));
                        dialog.dismiss();
                    }
                });

                row.addView(dayView, new TableRow.LayoutParams(dpToPixels(40), dpToPixels(40)));
                i++;
            }
            daySelectTable.addView(row);
        }

        builder.setView(daySelectLayout);

        dialog = builder.create();
    }

    @Override
    public void show() {
        dialog.show();
    }

    private int dpToPixels(int dp) {
        return (int) (dp * screenDensity + 0.5f);
    }
}
