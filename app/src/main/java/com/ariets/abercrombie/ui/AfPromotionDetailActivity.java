package com.ariets.abercrombie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ariets.abercrombie.R;
import com.ariets.abercrombie.model.AfPromotion;

/**
 * Created by aaron on 8/2/15.
 */
public class AfPromotionDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PROMOTION = "promotion";
    public static final String TRANSITION_IMAGE_NAME = "image";
    public static final String TRANSITION_TEXTVIEW_NAME = "title";

    public static void startActivity(@NonNull Activity activity, @NonNull AfPromotion promotion,
                                     @Nullable View transitionImage) {
        Intent intent = new Intent(activity, AfPromotionDetailActivity.class);
        intent.putExtra(EXTRA_PROMOTION, promotion);
        if (transitionImage != null ) {
            Pair<View, String> imagePair = new Pair<>(transitionImage, TRANSITION_IMAGE_NAME);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imagePair);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
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
