package com.github.blackenwhite.costplanner.dao.file;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.blackenwhite.costplanner.R;

import java.util.Locale;

public class Settings {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

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

    public static Locale getLocale() {
        return sContext.getResources().getConfiguration().locale;
    }
}
