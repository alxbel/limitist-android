package com.github.blackenwhite.costplanner.controller;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.DateManager;
import com.github.blackenwhite.costplanner.model.Limit;
import com.github.blackenwhite.costplanner.model.LimitStorage;
import com.github.blackenwhite.costplanner.util.ResourceManager;

public class LimitsActivity extends AppCompatActivity {
    private static final String TAG = "LimitsActivity";

    private LimitStorage mLimitStorage;

    private TextView mYearLabel;
    private ListView mMonthListView;

    private ArrayAdapter<String> mMonthAdapter;
    private Integer mCurrentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init members
        mLimitStorage = LimitStorage.get(getApplicationContext());
        mCurrentYear = DateManager.get().getCurrentYear();

        setContentView(R.layout.activity_limits);

        Toolbar toolbar = (Toolbar) findViewById(R.id.limits_toolbar);
        toolbar.setTitle(R.string.toolbar_limits);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mYearLabel = (TextView) findViewById(R.id.limits_label_year);
        mYearLabel.setText(mCurrentYear.toString());

        setupListView();
    }

    private AlertDialog getInputLimitDialog(final String month, final Limit limit) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LimitsActivity.this);
        alertDialog.setTitle(month);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_limit_input, null);
        final EditText limitInput = (EditText) dialogLayout.findViewById(R.id.input_dialog_limit);
        if (limit != null) {
            limitInput.setHint(Integer.valueOf(limit.getLimitMonthly()).toString());
        }

        alertDialog.setView(dialogLayout);

        alertDialog.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String msgIncorrect = String.format(
                                    ResourceManager.getString(R.string.fmt_limit_minimum_allowed),
                                    ResourceManager.getInteger(R.integer.limit_minimum_allowed));
                            Integer limMonthly = Integer.valueOf(limitInput.getText().toString());
                            if (limit != null) {
                                if (!Integer.valueOf(limit.getLimitMonthly()).equals(limMonthly)) {
                                    limit.setLimitMonthly(limMonthly);
                                    if (mLimitStorage.updateLimit(limit)) {
                                        ResourceManager.showMessage(R.string.toast_record_updated);
                                    } else {
                                        ResourceManager.showMessage(msgIncorrect);
                                    }
                                }
                            } else {
                                Limit newLim = new Limit(mCurrentYear, DateManager.get().getMonthIndex(month), limMonthly);
                                if (mLimitStorage.addLimit(newLim)) {
                                    ResourceManager.showMessage(R.string.toast_record_added);
                                } else {
                                    ResourceManager.showMessage(msgIncorrect);
                                }
                            }
                        } catch (NumberFormatException | NullPointerException e) {
                            ResourceManager.showMessage(R.string.toast_incorrect_value);
                        }
                        mLimitStorage.printAllLimits();
                    }
                }
        );

        alertDialog.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        return alertDialog.create();
    }

    private void setupListView() {
        mMonthListView = (ListView)findViewById(R.id.limits_list_months);
        mMonthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, DateManager.get().getRemainsMonthNames());
        mMonthListView.setAdapter(mMonthAdapter);
        mMonthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer monthIndex = DateManager.get().getMonthIndex(mMonthAdapter.getItem(position));
                String limID = Limit.createID(mCurrentYear, monthIndex);
                Limit limit = mLimitStorage.getLimit(limID);
                getInputLimitDialog(mMonthAdapter.getItem(position), limit).show();
            }
        });
    }
}
