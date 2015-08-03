package com.ariets.abercrombie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ariets.abercrombie.R;

import butterknife.ButterKnife;

/**
 * The activity that displays the list of promotions. This is done via {@link AfPromotionsFragment}.
 * <p/>
 * Created by aaron on 8/1/15.
 */
public class AfPromotionsActivity extends AppCompatActivity {

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
