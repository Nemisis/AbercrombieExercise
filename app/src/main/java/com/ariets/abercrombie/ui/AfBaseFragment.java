package com.ariets.abercrombie.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by aaron on 8/3/15.
 */
public class AfBaseFragment extends Fragment {

    protected AppCompatActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        mActivity.setSupportActionBar(toolbar);
    }
}
