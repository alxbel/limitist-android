package com.github.blackenwhite.costplanner.controller;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.blackenwhite.costplanner.R;

public class LimitsActivity extends AppCompatActivity {

    private FloatingActionButton mButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                final Spinner monthSpinner = (Spinner) dialogLayout.findViewById(R.id.month_spinner);
                ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(LimitsActivity.this,
                        R.array.months_array, android.R.layout.simple_spinner_item);
                monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                monthSpinner.setAdapter(monthAdapter);

                CheckBox wholeYearSwitch = (CheckBox) dialogLayout.findViewById(R.id.checkbox_for_whole_year);
                wholeYearSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        monthSpinner.setEnabled(!isChecked);
                    }
                });

                alertDialog.setView(dialogLayout);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                alertDialog.setNegativeButton("NO",
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
