package com.ariets.abercrombie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ariets.abercrombie.R;

import butterknife.ButterKnife;

/**
 * Created by aaron on 8/1/15.
 */
public class AfPromotionsActivity extends AfBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_host);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment promotionsFragment = fm.findFragmentByTag(AfPromotionsFragment.TAG);
        if (promotionsFragment == null) {
            // It has not been created/added to the UI yet.
            fm.beginTransaction().add(R.id.fragment_container, AfPromotionsFragment.newInstance(),
                    AfPromotionsFragment.TAG).commit();
        } else if (promotionsFragment.isDetached()) {
            fm.beginTransaction().attach(promotionsFragment).commit();
        }
    }
}
