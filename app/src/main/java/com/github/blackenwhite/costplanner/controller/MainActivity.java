package com.github.blackenwhite.costplanner.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.Date;
import com.github.blackenwhite.costplanner.dao.file.Settings;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_SETTINGS = 10;
    private static final int REQUEST_CODE_LIMITS = 11;
    private static final int REQUEST_CODE_ABOUT = 12;

    private static String sLang;
    private static Context sContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JodaTimeAndroid.init(this);
        sContext = this;

        Log.d(TAG, Integer.valueOf(new DateTime(2000, 2, 14, 12, 0, 0, 000).dayOfMonth().getMaximumValue()).toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.expenses_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        TextView dateLabel = (TextView) findViewById(R.id.label_date);
        dateLabel.setText(Date.get().toString());
        TextView monthLabel = (TextView) findViewById(R.id.label_month);
        monthLabel.setText(Date.get().getCurrentMonth());

        Intent intent = new Intent(this, LimitsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LIMITS);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                intent = new Intent(this, LimitsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LIMITS);
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

    public static Context getContext() {
        return sContext;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        sLang = lang;
        res.updateConfiguration(conf, dm);
        Date.get().setLocale(Settings.getLangPref(this));
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
}
