package com.icarasia2.contentprovider;

import android.test.AndroidTestCase;

/**
 * Created by Hafiz Waleed Hussain on 4/27/2015.
 */
public class CarAsiaContentProviderTest extends AndroidTestCase{

/*
    private final String TEST_IMAGE_PATH = "test/gallery/image";

    public void testCRUDOnContentProvider(){

        Cursor cursor;

        // Insertion
        Uri uri = mContext.getContentResolver()
                .insert(DatabaseContract.Gallery.CONTENT_URI, getContentValues());
        assertEquals(1, ContentUris.parseId(uri));

        // Data matching
        cursor = mContext
                .getContentResolver()
                .query(DatabaseContract.Gallery.CONTENT_URI, null, null, null, null);

        cursor.moveToNext();
        assertEquals(cursor
                        .getString(cursor.getColumnIndex(
                                DatabaseContract.Gallery.COLUMN_IMAGE_NAME)),
                TEST_IMAGE_PATH);

        // Updation
        int updateRows = mContext.getContentResolver().update(DatabaseContract.Gallery.CONTENT_URI,
                getContentValues(),"_id=?",new String[]{"1"});
        assertEquals(1,updateRows);

        // Deletion
        int deletion = mContext.getContentResolver()
                .delete(DatabaseContract.Gallery.CONTENT_URI,null,null);
        assertEquals(1,deletion);

        cursor = mContext
                .getContentResolver()
                .query(DatabaseContract.Gallery.CONTENT_URI, null, null, null, null);
        assertTrue(cursor.getCount() == 0);

    }


    private ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Gallery.COLUMN_IMAGE_NAME, TEST_IMAGE_PATH);
        return contentValues;
    }
*/
}
