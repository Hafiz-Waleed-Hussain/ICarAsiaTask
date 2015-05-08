package com.icarasia2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.icarasia2.R;
import com.icarasia2.database.DatabaseContract;
import com.icarasia2.utils.PhotoUtils;

import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_PRIORITY;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_VIEW_TYPE;

/**
 * Created by Hafiz Waleed Hussain on 4/27/2015.
 */
public class GalleryAdapter extends SimpleCursorAdapter{

    private LayoutInflater mInflater;
    private int mLayout;
    private PhotoUtils mPhotoUtils;
    private int mMaxImagesLimit = 10;

    public GalleryAdapter(Context context,int layout,String[] from, int[] to, int maximumImagesLimit) {
        super(context, layout, null, from,to,0);
        mLayout = layout;
        mInflater = LayoutInflater.from(context);
        mPhotoUtils = PhotoUtils.getInstance();
        mMaxImagesLimit = maximumImagesLimit <= 0 ? mMaxImagesLimit: maximumImagesLimit;
        mMaxImagesLimit++;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(mLayout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY));
        if(cursor.getString(cursor.getColumnIndex(COLUMN_VIEW_TYPE))
                .equals(DatabaseContract.Gallery.ViewTypes.ADD_BUTTON)){
            imageView.setImageResource(android.R.drawable.ic_input_add);
        }else{
            imageView.setImageBitmap(mPhotoUtils.getImageBitmap(context,cursor.getString(cursor.getColumnIndex(DatabaseContract.Gallery.COLUMN_IMAGE_NAME))));
        }
    }

    @Override
    public boolean isEnabled(int position) {
        if(getItemViewType(position) == 0 && getCount() == mMaxImagesLimit){
            showMaximumLimitExceed();
            return false;
        }
        return super.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        String viewType = cursor.getString(cursor.getColumnIndex(COLUMN_VIEW_TYPE));
        if( viewType.equals(DatabaseContract.Gallery.ViewTypes.ADD_BUTTON)){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    private void showMaximumLimitExceed() {
        Toast.makeText(mInflater.getContext(),
                mInflater.getContext().getString(R.string.maximum_limit_exceed), Toast.LENGTH_SHORT).show();
    }

}
