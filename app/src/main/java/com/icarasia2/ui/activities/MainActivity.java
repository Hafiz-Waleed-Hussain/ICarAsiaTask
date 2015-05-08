package com.icarasia2.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.icarasia2.R;
import com.icarasia2.ui.fragments.GridViewFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GridViewFragment())
                    .commit();
        }
    }
}
