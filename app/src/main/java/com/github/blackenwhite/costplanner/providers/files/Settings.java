package com.github.blackenwhite.costplanner.providers.files;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.blackenwhite.costplanner.R;

public class Settings {
    public static String getLangPref(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.settings_file_key), Context.MODE_PRIVATE);
        String defLang = ctx.getString(R.string.def_lang);
        String lang = sharedPref.getString(ctx.getString(R.string.lang), defLang);
        return lang;
    }

    public static void writeLangPref(Context ctx, String newLang) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.settings_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ctx.getString(R.string.lang), newLang);
        editor.commit();
    }
}
