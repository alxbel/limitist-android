package com.github.blackenwhite.costplanner.controller;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.dao.file.Settings;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle(R.string.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mButtonSave = (FloatingActionButton) findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_en:
                    Settings.writeLangPref(this, getString(R.string.lang_en));
                    break;
            case R.id.radio_ru:
                if (checked)
                    Settings.writeLangPref(this, getString(R.string.lang_ru));
                    break;
        }
    }
}
