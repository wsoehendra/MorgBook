package com.willnjames.android.morgbook.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by wsoeh on 10/10/2016.
 */

public class DatabaseOpener extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "MorgBookDB.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
