package com.github.blackenwhite.costplanner.util;

import android.content.Context;
import android.widget.Toast;

public class ResourceManager {

    private static Context context;

    public static void init(Context context) {
        ResourceManager.context = context;
    }

    public static String getString(int strId) {
        return context.getString(strId);
    }

    public static int getInteger(int integerId) {
        return context.getResources().getInteger(integerId);
    }

    public static void showMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(int msgId) {
        Toast.makeText(context, context.getString(msgId), Toast.LENGTH_LONG)
                .show();
    }

    public static void showQuickMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showQuickMessage(int msgId) {
        Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT)
                .show();
    }
}
