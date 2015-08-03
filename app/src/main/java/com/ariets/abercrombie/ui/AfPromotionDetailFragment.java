package com.ariets.abercrombie.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariets.abercrombie.R;
import com.ariets.abercrombie.model.AfButton;
import com.ariets.abercrombie.model.AfPromotion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by aaron on 8/2/15.
 */
public class AfPromotionDetailFragment extends AfBaseFragment {
    public static final String TAG = AfPromotionDetailFragment.class.getSimpleName();
    private static final String EXTRA_PROMOTION = "promotion";

    @Bind(R.id.promotion_detail_image)
    ImageView mImageView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.promotions_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.promotion_tv_description)
    TextView mTvDescription;
    @Bind(R.id.promotion_tv_title)
    TextView mTvTitle;
    @Bind(R.id.promotion_tv_footer)
    TextView mTvFooter;
    @Bind(R.id.promotion_detail_button_container)
    LinearLayout mButtonContainer;

    private AfPromotion mPromotion;

    public static AfPromotionDetailFragment newInstance(AfPromotion promotion) {
        AfPromotionDetailFragment fragment = new AfPromotionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PROMOTION, promotion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPromotion = args.getParcelable(EXTRA_PROMOTION);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.promotions_detail_fragment, container, false);
        ButterKnife.bind(this, v);
        mTvFooter.setMovementMethod(LinkMovementMethod.getInstance());
        ViewCompat.setTransitionName(mImageView, AfPromotionDetailActivity.TRANSITION_IMAGE_NAME);
        ViewCompat.setTransitionName(mTvTitle, AfPromotionDetailActivity.TRANSITION_TEXTVIEW_NAME);

        setupView();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupView() {
        if (mPromotion != null) {
            String imageUrl = mPromotion.getImage();
            Picasso.with(mActivity).load(imageUrl).placeholder(R.drawable.img_placeholder).into(mImageView);

            String title = mPromotion.getTitle();
            mCollapsingToolbarLayout.setTitle(title);
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
            mTvTitle.setText(title);

            String description = mPromotion.getDescription();
            if (description != null) {
                mTvDescription.setText(description);
            } else {
                mTvDescription.setVisibility(View.GONE);
            }
            setFooterText(mPromotion.getFooter());

            AfButton button = mPromotion.getButton();
            if (button != null) {
                mButtonContainer.addView(getButtonFromAfButton(button));
            }
            ArrayList<AfButton> buttons = mPromotion.getButtonList();
            if (buttons != null) {
                for (int i = 0, size = buttons.size(); i < size; i++) {
                    mButtonContainer.addView(getButtonFromAfButton(buttons.get(i)));
                }
            }
        }
    }

    private Button getButtonFromAfButton(final AfButton afButton) {
        Button button = new Button(mActivity);
        button.setText(afButton.getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(afButton.getTarget());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Timber.e(e, "Error setting Intent. Target: %s", afButton.getTarget());
                }
            }
        });
        return button;
    }

    private void setFooterText(@Nullable String footer) {
        if (footer == null) {
            mTvFooter.setVisibility(View.GONE);
            return;
        }
        Observable.just(footer).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .map(new Func1<String, Spanned>() {
                    @Override
                    public Spanned call(String s) {
                        return s == null ? null : Html.fromHtml(s);
                    }
                })
                .filter(new Func1<Spanned, Boolean>() {
                    @Override
                    public Boolean call(Spanned spanned) {
                        return spanned != null;
                    }
                })
                .subscribe(new Action1<Spanned>() {
                    @Override
                    public void call(Spanned spanned) {
                        mTvFooter.setText(spanned);
                    }
                });
    }
}
