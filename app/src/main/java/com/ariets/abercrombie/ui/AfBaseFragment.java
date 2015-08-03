package com.ariets.abercrombie.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

public class AfBaseFragment extends Fragment {

    protected AfBaseActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AfBaseActivity) activity;
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        mActivity.setSupportActionBar(toolbar);
    }

}
