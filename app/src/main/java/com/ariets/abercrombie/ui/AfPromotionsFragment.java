package com.ariets.abercrombie.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariets.abercrombie.R;
import com.ariets.abercrombie.api.ApiError;
import com.ariets.abercrombie.model.AfPromotion;
import com.ariets.abercrombie.presenters.PromotionsPresenter;
import com.ariets.abercrombie.views.DividerItemDecoration;
import com.ariets.abercrombie.views.IPromotionsView;
import com.ariets.abercrombie.views.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by aaron on 8/1/15.
 */
public class AfPromotionsFragment extends AfBaseFragment implements IPromotionsView, RecyclerItemClickListener
        .OnItemClickListener {
    public static final String TAG = AfPromotionsFragment.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.promotions_recycler)
    RecyclerView mRecyclerView;
    @Bind(R.id.promotions_empty_container)
    LinearLayout mEmptyContainer;
    @Bind(R.id.promotions_empty_image)
    ImageView mEmptyImage;
    @Bind(R.id.promotions_empty_tv)
    TextView mEmptyTv;

    private PromotionsPresenter mPresenter;
    private PromotionsAdapter mAdapter;

    public static AfPromotionsFragment newInstance() {
        return new AfPromotionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = new PromotionsPresenter(this, mActivity);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.promotions_fragment, container, false);
        ButterKnife.bind(this, v);
        setSupportActionBar(mToolbar);

        // Setup RecyclerView
        if (mAdapter == null) {
            mAdapter = new PromotionsAdapter(mActivity);
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext(), LinearLayoutManager
                .VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, this));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadPromotions(mActivity);
    }

    @Override
    public void showProgress(boolean show) {
        Timber.d("");
    }

    private void invalidateEmptyState(@StringRes int emptyStateText) {
        boolean showEmptyState = mAdapter.getItemCount() == 0;
        mRecyclerView.setVisibility(showEmptyState ? View.GONE : View.VISIBLE);
        mEmptyContainer.setVisibility(showEmptyState ? View.VISIBLE : View.GONE);
        mEmptyTv.setText(emptyStateText);
    }

    @Override
    public void displayPromotions(@NonNull ArrayList<AfPromotion> promotions) {
        if (promotions.size() == 0 && mAdapter.getItemCount() == 0) {
            // Show empty state.
            invalidateEmptyState(R.string.promotions_empty_state_none);
        } else {
            mAdapter.clear();
            for (int i = 0, size = promotions.size(); i < size; i++) {
                mAdapter.addItem(promotions.get(i));
            }
            invalidateEmptyState(R.string.promotions_empty_state_none);
        }
    }

    @Override
    public void onPromotionsError(ApiError apiError) {
        switch (apiError.getErrorType()) {
            case ApiError.ERROR_TYPE_NO_NETWORK:
                invalidateEmptyState(R.string.promotions_empty_state_no_network);
                break;
            case ApiError.ERROR_TYPE_API_ERROR:
                View v = getView();
                if (v != null) {
                    Snackbar.make(getView(), R.string.promotions_api_error, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        AfPromotion promotion = mAdapter.getPromotion(position);
        AfPromotionDetailActivity.startActivity(mActivity, promotion, view.findViewById(R.id.item_promotions_image));
    }

    class PromotionsAdapter extends RecyclerView.Adapter<PromotionsAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private ArrayList<AfPromotion> promotions;
        private Context context;

        PromotionsAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            promotions = new ArrayList<>();
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.item_promotion, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            AfPromotion promotion = promotions.get(i);
            viewHolder.tvTitle.setText(promotion.getTitle());

            // Load the Image thumbnail.
            String imageUrl = promotion.getImage();
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.img_placeholder).into(viewHolder.image);
        }

        void addItem(@NonNull AfPromotion promotion) {
            promotions.add(promotion);
            int newlyAddedPos = promotions.size();
            notifyItemInserted(newlyAddedPos);
        }

        void removeItem(int position) {
            promotions.remove(position);
            notifyItemRemoved(position);
        }

        void clear() {
            for (int i = 0, size = promotions.size(); i < size; i++) {
                removeItem(0);
            }
        }

        AfPromotion getPromotion(int position) {
            return promotions.get(position);
        }

        @Override
        public int getItemCount() {
            return promotions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.item_promotions_image)
            ImageView image;
            @Bind(R.id.item_promotions_tv_name)
            TextView tvTitle;
            @Bind(R.id.item_promotion_container)
            View container;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
