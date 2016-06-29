package com.github.blackenwhite.costplanner.utils;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Factory {
    public static final Spinner createSpinner(Context ctx, View view, int spinnerRes, int dataArrayRes) {
        final Spinner spinner = (Spinner) view.findViewById(spinnerRes);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                dataArrayRes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }
}
