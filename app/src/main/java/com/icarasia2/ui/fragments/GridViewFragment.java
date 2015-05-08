package com.icarasia2.ui.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.icarasia2.R;
import com.icarasia2.adapters.GalleryAdapter;
import com.icarasia2.helper.FooterHelper;
import com.icarasia2.utils.PhotoUtils;

import static android.provider.BaseColumns._ID;
import static com.icarasia2.database.DatabaseContract.Gallery;
import static com.icarasia2.database.DatabaseContract.Gallery.COLUMN_IMAGE_NAME;

/**
 * A placeholder fragment containing a simple view.
 */
public class GridViewFragment extends CarAsiaFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int MAXIMUM_IMAGES_LIMIT = 21;
    public static final int INIT_LOADER = 1;
    private GridView mGridView;
    private PhotoUtils mPhotoUtils;
    private GalleryAdapter mGalleryAdapter;
    private FooterHelper mFooterLayoutManager;

    public GridViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGalleryAdapter = new GalleryAdapter(getActivity(),R.layout.grid_cell,
                new String[]{COLUMN_IMAGE_NAME},new int[]{R.id.image}, MAXIMUM_IMAGES_LIMIT);
        mPhotoUtils = PhotoUtils.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_gridview, container, false);
        mFooterLayoutManager = new FooterHelper(this, (LinearLayout) findViewById(R.id.Footer_container));
        mGridView = (GridView) findViewById(R.id.FragmentGridView_grid_view);
        mGridView.setAdapter(mGalleryAdapter);
        mGridView.setOnItemClickListener(mGridItemClickListener);

        getLoaderManager().initLoader(INIT_LOADER,null,this);
        return mRootView;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(1);
    }

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Bitmap bitmap = mPhotoUtils.onActivityResultHandler(requestCode, resultCode, data);
            if(null != bitmap) {
                String name = mPhotoUtils.saveImage(getActivity(),bitmap);
                if(null != name) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_IMAGE_NAME, name);

                    if(mFooterLayoutManager.isReplace()){
                        replaceImage(contentValues);
                    }else {
                        insertImage(contentValues);
                    }
                }
            }
        }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Gallery.CONTENT_URI,
                null,null,null, Gallery.COLUMN_PRIORITY+" DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGalleryAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGalleryAdapter.changeCursor(null);
    }

    // Private methods

    private void insertImage(ContentValues contentValues) {
        contentValues.put(Gallery.COLUMN_VIEW_TYPE, Gallery.ViewTypes.IMAGE_GALLERY);
        Uri uri = getActivity().getContentResolver().insert(Gallery.CONTENT_URI,
                contentValues);
        int id = (int) ContentUris.parseId(uri);
        contentValues.put(Gallery.COLUMN_PRIORITY, id);
        getActivity().getContentResolver().update(Gallery.CONTENT_URI,
                contentValues, _ID + "=?",
                new String[]{id + ""});
    }

    private void replaceImage(ContentValues contentValues) {
        mPhotoUtils.deleteImageBitmap(getActivity(), mFooterLayoutManager.getImageName());
        getActivity().getContentResolver().update(Gallery.CONTENT_URI,
                contentValues, _ID+"=?",
                new String[]{mFooterLayoutManager.getImageId()+""});
    }

    private AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mGalleryAdapter.getItemViewType(position) == 0) {
                mPhotoUtils.createImageChooseDialog(GridViewFragment.this);
            } else {
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                mFooterLayoutManager.setCursor(cursor);
            }
        }
    };

}
