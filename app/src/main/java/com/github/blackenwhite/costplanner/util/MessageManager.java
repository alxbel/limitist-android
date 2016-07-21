package com.github.blackenwhite.costplanner.util;

import android.widget.Toast;

import com.github.blackenwhite.costplanner.controller.MainActivity;

public class MessageManager {

    public static String getString(int strId) {
        return MainActivity.getContext().getString(strId);
    }

    public static void showMessage(String msg) {
        Toast.makeText(MainActivity.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(int msgId) {
        Toast.makeText(MainActivity.getContext(), MainActivity.getContext().getString(msgId), Toast.LENGTH_LONG).show();
    }
}
