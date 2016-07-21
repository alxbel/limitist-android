package com.github.blackenwhite.costplanner.provider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.blackenwhite.costplanner.provider.database.LimitDbSchema.LimitTable;

public class LimitDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "limits.db";

    public LimitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LimitTable.NAME + "(" +
                LimitTable.Cols.ID + " text primary key, " +
                LimitTable.Cols.YEAR + ", " +
                LimitTable.Cols.MONTH + ", " +
                LimitTable.Cols.LIMIT_VALUE +
                " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
