package com.ariets.abercrombie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ariets.abercrombie.R;
import com.ariets.abercrombie.model.AfPromotion;

/**
 * Created by aaron on 8/2/15.
 */
public class AfPromotionDetailActivity extends AfBaseActivity {
    public static final String EXTRA_PROMOTION = "promotion";

    public static void startActivity(@NonNull Activity activity, @NonNull AfPromotion promotion) {
        Intent intent = new Intent(activity, AfPromotionDetailActivity.class);
        intent.putExtra(EXTRA_PROMOTION, promotion);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_host);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(AfPromotionDetailFragment.TAG);
        if (fragment == null) {
            AfPromotion afPromotion = getIntent().getParcelableExtra(EXTRA_PROMOTION);
            fm.beginTransaction().add(R.id.fragment_container, AfPromotionDetailFragment.newInstance(afPromotion))
                    .commit();
        } else if (fragment.isDetached()) {
            fm.beginTransaction().attach(fragment).commit();
        }
    }
}
