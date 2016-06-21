package com.github.blackenwhite.costplanner.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.Date;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Locale;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";

    private static String sLang;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        JodaTimeAndroid.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mDate = new Date(new Locale(SettingsActivity.getLangPref(this)));
        TextView dateLabel = (TextView) findViewById(R.id.label_date);
        dateLabel.setText(mDate.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocale();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        sLang = lang;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, InfoActivity.class);
        startActivity(refresh);
        finish();
    }

    private void updateLocale() {
        if (sLang == null) {
            sLang = SettingsActivity.getLangPref(this);
            setLocale(sLang);
            return;
        }
        if (!sLang.equalsIgnoreCase(SettingsActivity.getLangPref(this))) {
            sLang = SettingsActivity.getLangPref(this);
            setLocale(sLang);
        }
    }
}
