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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.Date;
import com.github.blackenwhite.costplanner.model.Limit;
import com.github.blackenwhite.costplanner.util.Factory;

public class LimitsActivity extends AppCompatActivity {
    private static final String TAG = "LimitsActivity";

    private static final int WHOLE_YEAR_FLAG = 13;
    private static final int JANUARY = 1;

    private FloatingActionButton mButtonAdd;
    private int mSelectedMonth;
    private int mSelectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init members
        mSelectedMonth = JANUARY;
        mSelectedYear = Date.INSTANCE.getCurrentYear();

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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LimitsActivity.this);
                alertDialog.setTitle(R.string.dialog_title_limits_input);
                alertDialog.setMessage(R.string.dialog_message_limits_input);

                LayoutInflater inflater = getLayoutInflater();
                LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_limits_input_layout, null);

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
                            public void onClick(DialogInterface dialog, int which) {
                                Integer selectedYear = Date.INSTANCE.getCurrentYear();
                                Integer selectedMonthIndice = 1;
                                try {
                                    selectedYear = new Integer(yearSpinner.getSelectedItem().toString());
                                    selectedMonthIndice = new Integer(Date.INSTANCE.getMonthIndex((String)monthSpinner.getSelectedItem()));
                                } catch (NumberFormatException e){}

                                if (wholeYearSwitch.isChecked()) {
                                    mSelectedMonth = WHOLE_YEAR_FLAG;
                                } else {
                                    Limit limit = new Limit(selectedYear, selectedMonthIndice, 10000);
                                    Log.d(TAG, String.format("%s", limit));
                                }
                            }
                        });

                alertDialog.setNegativeButton(R.string.dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });
    }
}
