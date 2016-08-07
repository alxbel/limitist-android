package com.github.blackenwhite.costplanner.util;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Factory {
    private static final String TAG = "Factory";

    public static Spinner createSpinner(Context ctx, View view, int spinnerRes, int dataArrayRes) {
        final Spinner spinner = (Spinner) view.findViewById(spinnerRes);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                dataArrayRes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    public static Integer[] getIntegers(int start, int end) {
        Integer[] integers = new Integer[end - start + 1];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = start++;
        }
        return integers;
    }


}
