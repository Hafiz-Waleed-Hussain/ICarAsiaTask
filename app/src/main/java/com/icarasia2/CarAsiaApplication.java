package com.icarasia2;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import com.icarasia2.database.DatabaseContract;

import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_IMAGE_NAME;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_PRIORITY;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_VIEW_TYPE;
import static com.icarasia2.database.DatabaseContract.Gallery.CONTENT_URI;
import static com.icarasia2.database.DatabaseContract.Gallery.ViewTypes;

/**
 * Created by Hafiz Waleed Hussain on 4/29/2015.
 */
public class CarAsiaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        addImageAddButton();
    }

    private void addImageAddButton() {
        Cursor cursor = getContentResolver().query(DatabaseContract.Gallery.buildUriForSearchViewType(ViewTypes.ADD_BUTTON),
                null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_IMAGE_NAME, "add_button");
            contentValues.put(COLUMN_VIEW_TYPE, ViewTypes.ADD_BUTTON);
            contentValues.put(COLUMN_PRIORITY, 0);
            getContentResolver().insert(CONTENT_URI, contentValues);
        }
    }
}
