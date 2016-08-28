package com.github.blackenwhite.costplanner.controller;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.DateManager;
import com.github.blackenwhite.costplanner.model.LimitDailyStorage;
import com.github.blackenwhite.costplanner.model.LimitMonthly;
import com.github.blackenwhite.costplanner.model.LimitMonthlyStorage;
import com.github.blackenwhite.costplanner.util.ResourceManager;

public class LimitsSetupActivity extends AppCompatActivity {
    private static final String TAG = "LimitsSetupActivity";

    private LimitMonthlyStorage limitMonthlyStorage;
    private LimitDailyStorage limitDailyStorage;

    private TextView yearLabel;
    private ListView monthListView;

    private LimitAdapter monthAdapter;
    private Integer currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init members
        limitMonthlyStorage = LimitMonthlyStorage.get(getApplicationContext());
        limitDailyStorage = LimitDailyStorage.get(getApplicationContext());
        currentYear = DateManager.get().getCurrentYear();

        setContentView(R.layout.activity_limits);

        Toolbar toolbar = (Toolbar) findViewById(R.id.limits_toolbar);
        toolbar.setTitle(R.string.toolbar_limits);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        yearLabel = (TextView) findViewById(R.id.limits_label_year);
        yearLabel.setText(currentYear.toString());

        setupListView();
    }

    public AlertDialog getInputLimitDialog(final String month, final LimitMonthly limitMonthly) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LimitsSetupActivity.this);
        alertDialog.setTitle(month);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_limit_input, null);
        final EditText limitInput = (EditText) dialogLayout.findViewById(R.id.input_dialog_limit);
        if (limitMonthly != null) {
            limitInput.setHint(Integer.valueOf(limitMonthly.getLimitValue()).toString());
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
                            if (limitMonthly != null) {
                                if (!Integer.valueOf(limitMonthly.getLimitValue()).equals(limMonthly)) {
                                    limitMonthly.setLimitValue(limMonthly);

                                    /***************** UPDATE MONTHLY LIMIT *******************/
                                    if (limitMonthlyStorage.updateLimitMonthly(limitMonthly)) {

                                        ResourceManager.showMessage(R.string.toast_record_updated);

                                        /******************** UPDATE DAILY LIMITS ***********************/
                                        limitDailyStorage.updateLimits(limitMonthly.createDailyLimits());

                                        limitMonthlyStorage.dPrintAllLimitsMonthly();
                                        limitDailyStorage.printAllLimitsDaily(limitMonthly.getId());
                                    } else {
                                        ResourceManager.showMessage(msgIncorrect);
                                    }
                                }
                            } else {
                                LimitMonthly newLimitMonthly = new LimitMonthly(currentYear, DateManager.get().getMonthIndex(month), limMonthly);

                                /****************** INSERT MONTHLY LIMIT ******************/
                                if (limitMonthlyStorage.addLimitMonthly(newLimitMonthly)) {

                                    ResourceManager.showMessage(R.string.toast_record_added);

                                    /****************** INSERT DAILY LIMITS *****************/
                                    limitDailyStorage.addLimits(newLimitMonthly.createDailyLimits());

                                    limitMonthlyStorage.dPrintAllLimitsMonthly();
                                    limitDailyStorage.printAllLimitsDaily(newLimitMonthly.getId());
                                } else {
                                    ResourceManager.showMessage(msgIncorrect);
                                }
                            }
                            monthAdapter.notifyDataSetChanged();
                        } catch (NumberFormatException | NullPointerException e) {
                            ResourceManager.showMessage(R.string.toast_incorrect_value);
                        }

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
        monthListView = (ListView)findViewById(R.id.limits_list_months);
        monthAdapter = new LimitAdapter(this, DateManager.get().getRemainsMonthNames());
        monthListView.setAdapter(monthAdapter);
    }
}
