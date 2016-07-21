package com.github.blackenwhite.costplanner.controller;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.Date;
import com.github.blackenwhite.costplanner.model.Limit;
import com.github.blackenwhite.costplanner.model.LimitStorage;
import com.github.blackenwhite.costplanner.util.Factory;
import com.github.blackenwhite.costplanner.util.MessageManager;

public class LimitsActivity extends AppCompatActivity {
    private static final String TAG = "LimitsActivity";

    private static final int WHOLE_YEAR_FLAG = 13;
    private static final int JANUARY = 1;

    private LimitStorage mLimitStorage;
    private FloatingActionButton mButtonAdd;
    private int mLimitValue;
    private int mSelectedMonthIndex;
    private int mSelectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init members
        mLimitValue = 0;
        mSelectedMonthIndex = JANUARY;
        mSelectedYear = Date.get().getCurrentYear();
        mLimitStorage = LimitStorage.get(getApplicationContext());

        setContentView(R.layout.activity_limits);

        Toolbar toolbar = (Toolbar) findViewById(R.id.limits_toolbar);
        toolbar.setTitle(R.string.toolbar_limits);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mButtonAdd = (FloatingActionButton) findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewLimitDialog().show();
            }
        });
    }

    private AlertDialog getNewLimitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LimitsActivity.this);
        alertDialog.setTitle(R.string.dialog_title_limits_input);
        alertDialog.setMessage(R.string.dialog_message_limits_input);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_limits_input_layout, null);
        final EditText limitInput = (EditText) dialogLayout.findViewById(R.id.limit_input);

        final Spinner monthSpinner = Factory.createSpinner(
                LimitsActivity.this, dialogLayout,
                R.id.month_spinner, R.array.months_array);

        final Spinner yearSpinner = Factory.createSpinner(
                LimitsActivity.this, dialogLayout,
                R.id.year_spinner, R.array.years_array);

        final CheckBox wholeYearSwitch = (CheckBox) dialogLayout.findViewById(R.id.checkbox_whole_year);
        wholeYearSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                monthSpinner.setEnabled(!isChecked);
            }
        });

        alertDialog.setView(dialogLayout);

        alertDialog.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mSelectedYear = Integer.valueOf(yearSpinner.getSelectedItem().toString());
                            mSelectedMonthIndex = Date.get().getMonthIndex((String)monthSpinner.getSelectedItem());
                            mLimitValue = Integer.valueOf(limitInput.getText().toString());
                        } catch (NumberFormatException | NullPointerException e){
                            e.printStackTrace();
                            mLimitValue = 0;
                        }
                        if (wholeYearSwitch.isChecked()) {
                            mSelectedMonthIndex = WHOLE_YEAR_FLAG;
                        } else {
                            Limit limit = new Limit(mSelectedYear, mSelectedMonthIndex, mLimitValue);
                            Limit old = mLimitStorage.addLimit(limit);
                            if (old != null) {
                                if (!old.equals(limit)) {
                                    getUpdLimitConfirmDialog(old, limit).show();
                                } else {
                                    MessageManager.showMessage(R.string.record_no_changes);
                                }
                            } else {
                                Log.d(TAG, String.format("%s", limit));
                                MessageManager.showMessage(R.string.record_added);
                            }
                            mLimitStorage.printAllLimits();
                        }
                    }
                });

        alertDialog.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return alertDialog.create();
    }

    private AlertDialog getUpdLimitConfirmDialog(Limit old, final Limit updated) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(String.format(
                MessageManager.getString(R.string.fmt_record_exists),
                Date.get().getMonth(old.getMonth()), old.getYear()
        ));

        alertDialog.setMessage(String.format(
                MessageManager.getString(R.string.fmt_question_update_record),
                MessageManager.getString(R.string.current_lim), " ", old.getLimVal(),
                MessageManager.getString(R.string.new_lim), " ", updated.getLimVal()));

        alertDialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLimitStorage.updateLimit(updated);
            }
        });

        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return alertDialog.create();
    }
}
