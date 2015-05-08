package com.icarasia2.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by Hafiz Waleed Hussain on 4/27/2015.
 */
public class DatabaseTesting extends AndroidTestCase {

    public void testIsDatabaseCreated(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        assertTrue(sqLiteDatabase.isOpen());
    }
}
