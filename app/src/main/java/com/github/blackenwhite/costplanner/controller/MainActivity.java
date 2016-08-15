package com.github.blackenwhite.costplanner.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.common.CustomEditText;
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
import com.github.blackenwhite.costplanner.common.Callback;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_SETTINGS = 10;
    private static final int REQUEST_CODE_LIMITS = 11;
    private static final int REQUEST_CODE_ABOUT = 12;
    private static final String FORMAT_DATA = "%s";

    private static String sLang;

    private LinearLayout mLayout;
    private TextView mDateText;
    private int mDay;

    private LimitDaily mLimitDaily;
    private TableLayout mLimitDailyCalculationsLayout;
    private TableRow mLimitDailySpentLayout;
    private ImageView mIconInfoLimitSpent;

    private TableRow mLimitDailyBalanceLayout;
    private TextView mLimitDailyBalanceText;
    private TableRow mLimitDailyBalanceNegativeLayout;
    private TextView mLimitDailyBalanceNegativeText;

    private TextView mLimitDailyText;
    private KeyListener mSpentKeyListener;
    private CustomEditText mLimitDailySpentText;
    private TextView mNoDailyLimitLabel;

    private LimitMonthly mLimitMonthly;
    private TableLayout mLimitMonthlyCalculationsLayout;
    private TextView mMonthYearText;
    private TextView mLimitMonthlyText;
    private TextView mNoMonthlyLimitLabel;

    private FloatingActionButton mButtonAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JodaTimeAndroid.init(this);
        DateManager.init(this);
        ResourceManager.init(this);
        Settings.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.expenses_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Content layout
        mLayout = (LinearLayout) findViewById(R.id.layout_main);
        mLayout.setOnTouchListener(this);

        // Current month and year upper label
        mMonthYearText = (TextView) findViewById(R.id.text_main_month_year);
        String monthYearString = String.format("%s %s", DateManager.get().getCurrentMonth(), DateManager.get().getCurrentYear());
        mMonthYearText.setText(monthYearString);

        // Monthly limit calculations area
        mLimitMonthlyCalculationsLayout = (TableLayout)findViewById(R.id.main_limit_monthly_calculations);
        mLimitMonthlyText = (TextView) findViewById(R.id.text_main_month_limit);
        mNoMonthlyLimitLabel = (TextView)findViewById(R.id.main_label_month_no_limit);

        // Current date (ddd dd.mm.yyyy) center label
        mDateText = (TextView) findViewById(R.id.text_main_date);
        mDateText.setText(DateManager.get().getDate());
        mDay = DateManager.get().getCurrentDayOfMonth();

        // Daily limit calculations area
        mLimitDailyCalculationsLayout = (TableLayout) findViewById(R.id.main_limit_daily_calculations);
        mLimitDailySpentLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_spent);

        mIconInfoLimitSpent = (ImageView) findViewById(R.id.icon_info_spent);
        mIconInfoLimitSpent.setOnClickListener(this);

        mLimitDailyText = (TextView) findViewById(R.id.text_main_day_limit);
        mLimitDailySpentText = (CustomEditText) findViewById(R.id.text_main_daily_spent);
        mLimitDailySpentText.setCustomSelectionActionModeCallback(Factory.createTextSelectionDisablerCallback());

        mSpentKeyListener = mLimitDailySpentText.getKeyListener();

        mLimitDailySpentText.setKeyListener(null);
        //mLimitDailySpentText.setOnLongClickListener(this);
        //mLimitDailySpentLayout.setOnLongClickListener(this);

        mLimitDailySpentText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    acceptManualInput();
                }
                return false;
            }
        });

        mLimitDailyBalanceLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_balance);
        mLimitDailyBalanceText = (TextView) findViewById(R.id.text_main_daily_balance);
        mLimitDailyBalanceNegativeLayout = (TableRow) findViewById(R.id.layout_main_limit_daily_balance_negative);
        mLimitDailyBalanceNegativeText = (TextView) findViewById(R.id.text_main_daily_balance_negative);

        mNoDailyLimitLabel = (TextView) findViewById(R.id.main_label_day_no_limit);

        // Add expense button
        mButtonAddExpense = (FloatingActionButton) findViewById(R.id.main_button_add_expense);
        mButtonAddExpense.setOnClickListener(this);
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
                startLimitsActivity();
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
                if (mNoDailyLimitLabel.getVisibility() == View.VISIBLE) {
                    ResourceManager.showMessage(R.string.error_no_monthly_limit);
                    return;
                }
                Helper.showKeyboard(this);
                getAddExpenseDialog(new Callback<Integer>() {
                    @Override
                    public void call(Integer spent) {
                        Helper.hideKeyboard(MainActivity.this);

                        try {
                            Expense expense = new Expense();
                            expense.setLimitDailyId(mLimitDaily.getId());
                            expense.setValue(spent);
                            if (ExpenseStorage.get(getApplicationContext()).addExpense(expense) != -1) {
                                updateView();
                            }
                        } catch (NullPointerException ignored){}

//                        spent += mLimitDaily.getSpent();
//                        mLimitDaily.setSpent(spent);
//                        LimitDailyStorage.get(MainActivity.this).updateLimit(mLimitDaily);

//                        updateView();
                    }
                }).show();
                break;
            case R.id.icon_info_spent:
                ResourceManager.showQuickMessage(R.string.toast_spent_hint);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.layout_main_limit_daily_spent || v.getId() == R.id.text_main_daily_spent) {
            if (mLimitDailySpentText.getKeyListener() == null) {
                mLimitDailySpentText.setKeyListener(mSpentKeyListener);
                mLimitDailySpentText.setText(Factory.createUnderlinedString(mLimitDaily.getSpent()));
                mLimitDailySpentText.setSelection(String.valueOf(mLimitDaily.getSpent()).length());
                Helper.showKeyboard(getApplicationContext());
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        acceptManualInput();
        Helper.hideKeyboard(this);
        return false;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        sLang = lang;
        res.updateConfiguration(conf, dm);
        DateManager.get().setLocale(Settings.getLangPref(this));
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private void updateLocale() {
        if (sLang == null) {
            sLang = Settings.getLangPref(this);
            setLocale(sLang);
            return;
        }
        if (!sLang.equalsIgnoreCase(Settings.getLangPref(this))) {
            sLang = Settings.getLangPref(this);
            setLocale(sLang);
        }
    }

    private void startLimitsActivity() {
        Intent intent = new Intent(this, LimitsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LIMITS);
    }

    private void acceptManualInput() {
        mLimitDailySpentText.setKeyListener(null);
        Helper.hideKeyboard(this);
        try {
            Integer spent = Integer.valueOf(mLimitDailySpentText.getText().toString());
            if (mLimitDaily.getSpent() != spent) {
                mLimitDaily.setSpent(spent);
                LimitDailyStorage.get(MainActivity.this).updateLimit(mLimitDaily);
            }
        } catch (NumberFormatException e) {
            if (mLimitDailySpentText.getText().toString().equals("")) {
                mLimitDaily.setSpent(0);
                LimitDailyStorage.get(MainActivity.this).updateLimit(mLimitDaily);
            }
        }
        mLimitDailySpentText.setText(String.format(FORMAT_DATA, mLimitDaily.getSpent()));
        updateView();
    }

    private void updateView() {
        if (mLimitDaily == null) {
            mLimitDaily = LimitDailyStorage.get(this).getLimitDaily(LimitDaily.generateIdForCurrentDate());
        }

        if (mLimitDaily != null) {
            // debug message
            ExpenseStorage.get(this).printExpensesForDailyLimit(mLimitDaily.getId());
            Log.d(TAG, "cursor: " + String.valueOf(ExpenseStorage.get(this).getSum(mLimitDaily.getId())));

            mNoDailyLimitLabel.setVisibility(View.GONE);
            mLimitDailyCalculationsLayout.setVisibility(View.VISIBLE);
            mLimitDailyText.setText(String.format(FORMAT_DATA, mLimitDaily.getLimitValue()));

//            if (mLimitDaily.getSpent() != 0) {
//                mLimitDailySpentLayout.setVisibility(View.VISIBLE);
//                mLimitDailySpentText.setText(String.format(FORMAT_DATA, mLimitDaily.getSpent()));
//
//                if (mLimitDaily.getBalance() > 0) {
//                    mLimitDailyBalanceLayout.setVisibility(View.VISIBLE);
//                    mLimitDailyBalanceText.setText(String.format(FORMAT_DATA, mLimitDaily.getBalance()));
//                    mLimitDailyBalanceNegativeLayout.setVisibility(View.GONE);
//                } else {
//                    mLimitDailyBalanceLayout.setVisibility(View.GONE);
//                    mLimitDailyBalanceNegativeLayout.setVisibility(View.VISIBLE);
//                    mLimitDailyBalanceNegativeText.setText(String.format(FORMAT_DATA, mLimitDaily.getBalance()));
//                }
//            } else {
//                mLimitDailySpentLayout.setVisibility(View.GONE);
//                mLimitDailyBalanceLayout.setVisibility(View.GONE);
//                mLimitDailyBalanceNegativeLayout.setVisibility(View.GONE);
//            }

            Integer spentSum = ExpenseStorage.get(this).getSum(mLimitDaily.getId());
            if (spentSum != null && !spentSum.equals(0)) {
                mLimitDaily.setSpent(spentSum);
                mLimitDailySpentLayout.setVisibility(View.VISIBLE);
                mLimitDailySpentText.setText(String.format(FORMAT_DATA, mLimitDaily.getSpent()));

                if (mLimitDaily.getBalance() > 0) {
                    mLimitDailyBalanceLayout.setVisibility(View.VISIBLE);
                    mLimitDailyBalanceText.setText(String.format(FORMAT_DATA, mLimitDaily.getBalance()));
                    mLimitDailyBalanceNegativeLayout.setVisibility(View.GONE);
                } else {
                    mLimitDailyBalanceLayout.setVisibility(View.GONE);
                    mLimitDailyBalanceNegativeLayout.setVisibility(View.VISIBLE);
                    mLimitDailyBalanceNegativeText.setText(String.format(FORMAT_DATA, mLimitDaily.getBalance()));
                }
            } else {
                mLimitDailySpentLayout.setVisibility(View.GONE);
                mLimitDailyBalanceLayout.setVisibility(View.GONE);
                mLimitDailyBalanceNegativeLayout.setVisibility(View.GONE);
            }
        } else {
            mNoDailyLimitLabel.setVisibility(View.VISIBLE);
            mLimitDailyCalculationsLayout.setVisibility(View.GONE);
        }

        if (mLimitMonthly == null) {
            mLimitMonthly = LimitMonthlyStorage.get(this).getLimitMonthly(LimitMonthly.generateIdForCurrentDate());
        }
        if (mLimitMonthly != null) {
            mNoMonthlyLimitLabel.setVisibility(View.GONE);
            mLimitMonthlyCalculationsLayout.setVisibility(View.VISIBLE);
            mLimitMonthlyText.setText(String.format(FORMAT_DATA, mLimitMonthly.getLimitValue()));
        } else {
            mNoMonthlyLimitLabel.setVisibility(View.VISIBLE);
            mLimitMonthlyCalculationsLayout.setVisibility(View.GONE);
        }
    }

    private AlertDialog getAddExpenseDialog(final Callback<Integer> callback) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.dialog_main_add_expense_title);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.dialog_main_add_expense, null);
        final EditText limitInput = (EditText) dialogLayout.findViewById(R.id.limit_input);

//        final Spinner categorySpinner = Factory.createSpinner(
//                this, dialogLayout,
//                R.id.spinner_category, R.array.array_categories);

        alertDialog.setView(dialogLayout);

        alertDialog.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Integer limitValue = Integer.valueOf(limitInput.getText().toString());
                            callback.call(limitValue);
                        } catch (NumberFormatException ignored) {
                            Helper.hideKeyboard(MainActivity.this);
                        }
                    }
                });

        alertDialog.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.hideKeyboard(MainActivity.this);
                        dialog.cancel();
                    }
                });
        return alertDialog.create();
    }
}