package com.github.blackenwhite.costplanner.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.DateManager;
import com.github.blackenwhite.costplanner.dao.file.Settings;
import com.github.blackenwhite.costplanner.model.Expense;
import com.github.blackenwhite.costplanner.model.ExpenseStorage;
import com.github.blackenwhite.costplanner.model.LimitDaily;
import com.github.blackenwhite.costplanner.model.LimitDailyStorage;
import com.github.blackenwhite.costplanner.model.LimitMonthly;
import com.github.blackenwhite.costplanner.model.LimitMonthlyStorage;
import com.github.blackenwhite.costplanner.util.Factory;
import com.github.blackenwhite.costplanner.util.Helper;
import com.github.blackenwhite.costplanner.util.ResourceManager;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ExpensesActivityImpl extends AppCompatActivity implements ExpensesActivity,
        View.OnClickListener, View.OnTouchListener
//        View.OnLongClickListener
{
    private static final String TAG = "ExpensesActivityImpl";
    private static final int REQUEST_CODE_SETTINGS = 10;
    private static final int REQUEST_CODE_LIMITS = 11;
    private static final int REQUEST_CODE_ABOUT = 12;
    private static final String FORMAT_DATA = "%s";

    private static String langString;

    private LinearLayout layout;
    private TextView dateText;
    private int dayToday;

    private LimitDaily limitDaily;
    private List<Expense> expenses;
    private TableLayout limitDailyCalculationsLayout;
    private TableRow limitDailySpentLayout;
    private ImageView iconInfoLimitSpent;

    private TableRow limitDailyBalanceLayout;
    private TextView limitDailyBalanceText;
    private TableRow limitDailyBalanceNegativeLayout;
    private TextView limitDailyBalanceNegativeText;

    private TextView limitDailyText;
    private TextView limitDailySpentText;
    private TextView noDailyLimitLabel;

    private LimitMonthly limitMonthly;
    private TableLayout limitMonthlyCalculationsLayout;
    private TextView monthYearText;
    private TextView limitMonthlyText;
    private TextView noMonthlyLimitLabel;

    private FloatingActionButton buttonAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        JodaTimeAndroid.init(this);
        DateManager.init(this);
        ResourceManager.init(this);
        Settings.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.expenses_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Content layout
        layout = (LinearLayout) findViewById(R.id.layout_main);
        layout.setOnTouchListener(this);

        // Add expense button
        buttonAddExpense = (FloatingActionButton) findViewById(R.id.main_button_add_expense);
        buttonAddExpense.setOnClickListener(this);

        dayToday = DateManager.get().getCurrentDayOfMonth();

        setDailyLimitView();
        setDailyLimitForDay(dayToday);
        setMonthlyLimitView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateView();
        updateLocale();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                updateLocale();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                return true;

            case R.id.action_limits:
                startLimitsSetupActivity();
                return true;

            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ABOUT);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_button_add_expense:
                if (noDailyLimitLabel.getVisibility() == View.VISIBLE) {
                    ResourceManager.showMessage(R.string.error_no_monthly_limit);
                    return;
                }
                Helper.showKeyboard(this);
                getAddExpenseDialog().show();
                break;
            case R.id.icon_info_spent:
                new DailyExpensesDialog(this, expenses).show();
                break;
            case R.id.text_main_date:
                openDaySelectDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Helper.hideKeyboard(this);
        return false;
    }

    @Override
    public void killEmAll(List<Expense> expenses) {
        ExpenseStorage.get(this).deleteExpenses(expenses);
        this.expenses.removeAll(expenses);
        updateView();
    }

    @Override
    public void setViewForDay(int day) {
        setDailyLimitForDay(day);
        updateView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getDays() {
        return DateManager.get().getDaysInCurrentMonth();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        langString = lang;
        res.updateConfiguration(conf, dm);
        DateManager.get().setLocale(Settings.getLangPref(this));
        Intent refresh = new Intent(this, ExpensesActivityImpl.class);
        startActivity(refresh);
        finish();
    }

    private void updateLocale() {
        if (langString == null) {
            langString = Settings.getLangPref(this);
            setLocale(langString);
            return;
        }
        if (!langString.equalsIgnoreCase(Settings.getLangPref(this))) {
            langString = Settings.getLangPref(this);
            setLocale(langString);
        }
    }

    private void startLimitsSetupActivity() {
        Intent intent = new Intent(this, LimitsSetupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LIMITS);
    }

    private void updateView() {
        if (limitDaily == null) {
            setDailyLimitForDay(DateManager.get().getCurrentDayOfMonth());
        }

        if (limitDaily != null) {
            noDailyLimitLabel.setVisibility(View.GONE);
            limitDailyCalculationsLayout.setVisibility(View.VISIBLE);
            limitDailyText.setText(String.format(FORMAT_DATA, limitDaily.getLimitValue()));

            Integer spentSum = ExpenseStorage.get(this).getSum(limitDaily.getId());
            if (spentSum != null && !spentSum.equals(0)) {
                limitDaily.setSpent(spentSum);
                limitDailySpentLayout.setVisibility(View.VISIBLE);
                limitDailySpentText.setText(String.format(FORMAT_DATA, limitDaily.getSpent()));

                if (limitDaily.getBalance() > 0) {
                    limitDailyBalanceLayout.setVisibility(View.VISIBLE);
                    limitDailyBalanceText.setText(String.format(FORMAT_DATA, limitDaily.getBalance()));
                    limitDailyBalanceNegativeLayout.setVisibility(View.GONE);
                } else {
                    limitDailyBalanceLayout.setVisibility(View.GONE);
                    limitDailyBalanceNegativeLayout.setVisibility(View.VISIBLE);
                    limitDailyBalanceNegativeText.setText(String.format(FORMAT_DATA, limitDaily.getBalance()));
                }
            } else {
                limitDailySpentLayout.setVisibility(View.GONE);
                limitDailyBalanceLayout.setVisibility(View.GONE);
                limitDailyBalanceNegativeLayout.setVisibility(View.GONE);
            }
        } else {
            noDailyLimitLabel.setVisibility(View.VISIBLE);
            limitDailyCalculationsLayout.setVisibility(View.GONE);
        }

        if (limitMonthly == null) {
            limitMonthly = LimitMonthlyStorage.get(this).getLimitMonthly(LimitMonthly.generateIdForCurrentDate());
        }
        if (limitMonthly != null) {
            noMonthlyLimitLabel.setVisibility(View.GONE);
            limitMonthlyCalculationsLayout.setVisibility(View.VISIBLE);
            limitMonthlyText.setText(String.format(FORMAT_DATA, limitMonthly.getLimitValue()));
        } else {
            noMonthlyLimitLabel.setVisibility(View.VISIBLE);
            limitMonthlyCalculationsLayout.setVisibility(View.GONE);
        }
    }

    private AlertDialog getAddExpenseDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.dialog_main_add_expense_title);

        LayoutInflater inflater = getLayoutInflater();

        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_main_add_expense, null);

        final EditText expenseValueInput = (EditText) dialogLayout.findViewById(R.id.limit_input);

        final TextView detailsButton = (TextView) dialogLayout.findViewById(R.id.dialog_add_expense_btn_details);

        final LinearLayout detailsLayout = (LinearLayout) dialogLayout.findViewById(R.id.dialog_add_expense_details_layout);
        final EditText expenseTitleInput = (EditText) detailsLayout.findViewById(R.id.dialog_add_expense_title);
        final Spinner categorySpinner = Factory.createSpinner(
                this, dialogLayout,
                R.id.dialog_add_expense_spinner_category, R.array.array_categories);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsButton.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
            }
        });

        alertDialog.setView(dialogLayout);

        alertDialog.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Integer value = Integer.valueOf(expenseValueInput.getText().toString());
                            Expense expense = new Expense();
                            expense.setLimitDailyId(limitDaily.getId());
                            expense.setValue(value);
                            if (detailsLayout.getVisibility() == View.VISIBLE) {
                                expense.setTitle(expenseTitleInput.getText().toString());
                                expense.setCategory(categorySpinner.getSelectedItem().toString());
                            }

                            if (ExpenseStorage.get(getApplicationContext()).addExpense(expense) != -1) {
                                if (expenses != null) {
                                    expenses.add(expense);
                                }
                                updateView();
                            }
                        } catch (NullPointerException | NumberFormatException e) {
                            e.printStackTrace();
                        }

                        Helper.hideKeyboard(ExpensesActivityImpl.this);
                    }
                });

        alertDialog.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.hideKeyboard(ExpensesActivityImpl.this);
                        dialog.cancel();
                    }
                });
        return alertDialog.create();
    }

    private void setMonthlyLimitView() {
        // Current month and year upper label
        monthYearText = (TextView) findViewById(R.id.text_main_month_year);
        String monthYearString = String.format("%s %s", DateManager.get().getCurrentMonth(), DateManager.get().getCurrentYear());
        monthYearText.setText(monthYearString);

        // Monthly limit calculations area
        limitMonthlyCalculationsLayout = (TableLayout) findViewById(R.id.main_limit_monthly_calculations);
        limitMonthlyText = (TextView) findViewById(R.id.text_main_month_limit);
        noMonthlyLimitLabel = (TextView) findViewById(R.id.main_label_month_no_limit);
    }

    private void openDaySelectDialog() {
        new DaySelectDialog(this).show();
    }

    private void setDailyLimitView() {

        dateText = (TextView) findViewById(R.id.text_main_date);
        dateText.setOnClickListener(this);

        // Daily limit calculations area
        limitDailyCalculationsLayout = (TableLayout) findViewById(R.id.main_limit_daily_calculations);
        limitDailySpentLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_spent);

        iconInfoLimitSpent = (ImageView) findViewById(R.id.icon_info_spent);
        iconInfoLimitSpent.setOnClickListener(this);

        limitDailyText = (TextView) findViewById(R.id.text_main_day_limit);
        limitDailySpentText = (TextView) findViewById(R.id.text_main_daily_spent);

        limitDailyBalanceLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_balance);
        limitDailyBalanceText = (TextView) findViewById(R.id.text_main_daily_balance);
        limitDailyBalanceNegativeLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_balance_negative);
        limitDailyBalanceNegativeText = (TextView) findViewById(R.id.text_main_daily_balance_negative);

        noDailyLimitLabel = (TextView) findViewById(R.id.main_label_day_no_limit);
    }

    private void setDailyLimitForDay(int day) {
        // Current date (ddd dd.mm.yyyy) center label
        dateText.setText(DateManager.get().getDateString(day));

        limitDaily = LimitDailyStorage.get(this).getLimitDaily(LimitDaily.generateIdForDay(day));
        if (expenses == null) {
            expenses = new LinkedList<>();
        }
        expenses = ExpenseStorage.get(this).getExpenses(limitDaily.getId());
    }
}