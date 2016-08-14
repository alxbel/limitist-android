package com.github.blackenwhite.costplanner.util;

import android.content.Context;
import android.widget.Toast;

public class ResourceManager {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static String getString(int strId) {
        return sContext.getString(strId);
    }

    public static int getInteger(int integerId) {
        return sContext.getResources().getInteger(integerId);
    }

    public static void showMessage(String msg) {
        Toast.makeText(sContext, msg, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(int msgId) {
        Toast.makeText(sContext, sContext.getString(msgId), Toast.LENGTH_LONG)
                .show();
    }

    public static void showQuickMessage(String msg) {
        Toast.makeText(sContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showQuickMessage(int msgId) {
        Toast.makeText(sContext, sContext.getString(msgId), Toast.LENGTH_SHORT)
                .show();
    }
}
