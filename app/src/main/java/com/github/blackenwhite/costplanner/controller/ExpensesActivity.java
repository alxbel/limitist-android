package com.github.blackenwhite.costplanner.controller;

import android.app.Activity;

import com.github.blackenwhite.costplanner.model.Expense;

import java.util.List;

public interface ExpensesActivity {
    void killEmAll(List<Expense> expenses);
    void setViewForDay(int day);

    Activity getActivity();
    int getDays();
}
