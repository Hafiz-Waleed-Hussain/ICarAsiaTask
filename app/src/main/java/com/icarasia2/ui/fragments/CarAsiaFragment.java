package com.icarasia2.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Hafiz Waleed Hussain on 4/27/2015.
 */
public abstract class CarAsiaFragment extends Fragment{

    protected View mRootView;

    protected View findViewById(int id){
        return mRootView.findViewById(id);
    }
}
