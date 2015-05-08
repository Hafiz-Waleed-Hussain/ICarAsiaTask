package com.icarasia2.helper;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.icarasia2.R;
import com.icarasia2.database.DatabaseContract;
import com.icarasia2.ui.fragments.GridViewFragment;
import com.icarasia2.utils.PhotoUtils;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_IMAGE_NAME;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_PRIORITY;

/**
* Created by Hafiz Waleed Hussain on 4/30/2015.
*/
public class FooterHelper {

    private GridViewFragment mGridViewFragment;
    private LinearLayout mLinearLayout;
    private Button mDeleteButton;
    private Button mMakeAsMainButton;
    private Button mEditButton;
    private boolean mIsReplace;
    private String mImageName;
    private int mImageId;
    private int mPriority;

    public FooterHelper(GridViewFragment gridViewFragment, LinearLayout linearLayout) {
        mGridViewFragment = gridViewFragment;
        mLinearLayout = linearLayout;
        mDeleteButton = (Button) linearLayout.findViewById(R.id.Footer_delete_button);
        mMakeAsMainButton = (Button) linearLayout.findViewById(R.id.Footer_make_as_main_button);
        mEditButton = (Button) linearLayout.findViewById(R.id.Footer_edit_button);

        mDeleteButton.setOnClickListener(mDeleteOnClickListener);
        mMakeAsMainButton.setOnClickListener(mMakeAsMainOnClickListener);
        mEditButton.setOnClickListener(mEditButtonOnClickListener);
    }

    public int getImageId() {
        return mImageId;
    }

    public String getImageName() {
        return mImageName;
    }

    public boolean isReplace() {
        return mIsReplace;
    }

    public void setReplace(boolean isReplace) {
        mIsReplace = isReplace;
    }

    public void setCursor(Cursor cursor) {
        if(cursor != null) {
            mImageName = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_NAME));
            mImageId = cursor.getInt(cursor.getColumnIndex(_ID));
            mPriority = cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY));
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    // Private methods

    private View.OnClickListener mDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridViewFragment.getActivity().getContentResolver().delete(DatabaseContract.Gallery.CONTENT_URI,
                    DatabaseContract.Gallery.COLUMN_IMAGE_NAME+" = ?",new String[]{mImageName});
            setCursor(null);
            setReplace(false);
            mLinearLayout.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener mMakeAsMainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor cursor = mGridViewFragment.getActivity().getContentResolver().query(DatabaseContract.Gallery.CONTENT_URI,
                    null, null, null, _ID + " DESC limit 1");

            Cursor maximumPriorityCursor = mGridViewFragment.getActivity().getContentResolver().query(DatabaseContract.Gallery.CONTENT_URI,
                    null,null,null,COLUMN_PRIORITY+" DESC limit 1");

            cursor.moveToFirst();
            maximumPriorityCursor.moveToFirst();

            int maxPriority = maximumPriorityCursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY));
            int maximumPriorityImageId = maximumPriorityCursor.getInt(cursor.getColumnIndex(_ID));

            ContentValues currentMaximumPriorityImage = new ContentValues();
            currentMaximumPriorityImage.put(COLUMN_PRIORITY, mPriority);

            ContentValues currentSelectedImage = new ContentValues();
            currentSelectedImage.put(COLUMN_PRIORITY, maxPriority);
            swapImagesLocations(maximumPriorityImageId, currentMaximumPriorityImage, currentSelectedImage);
        }
    };

    private void swapImagesLocations(int maximumPriorityImageId, ContentValues currentMaximumPriority, ContentValues currentSelecctedImage) {
        ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();
        contentProviderOperations.add(ContentProviderOperation
                .newUpdate(DatabaseContract.Gallery.CONTENT_URI)
                .withValues(currentMaximumPriority)
                .withSelection(_ID + "=?", new String[]{maximumPriorityImageId + ""}).build());
        contentProviderOperations.add(ContentProviderOperation
                .newUpdate(DatabaseContract.Gallery.CONTENT_URI)
                .withValues(currentSelecctedImage)
                .withSelection(_ID + "=?", new String[]{mImageId + ""}).build());
        try {
            mGridViewFragment.getActivity().getContentResolver().applyBatch(DatabaseContract.AUTHORITY,contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener mEditButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setReplace(true);
            PhotoUtils.getInstance().createImageChooseDialog(mGridViewFragment,
                    new PhotoUtils.DialogState() {
                        @Override
                        public void onCancel() {
                            setReplace(false);
                        }
                    });
        }
    };

}
