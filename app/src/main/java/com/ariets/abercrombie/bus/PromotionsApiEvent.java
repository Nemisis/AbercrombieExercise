package com.ariets.abercrombie.bus;

import android.support.annotation.Nullable;

import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class PromotionsApiEvent {

    @Nullable
    private ArrayList<AfPromotion> promotions;
    @Nullable
    private RetrofitError retrofitError;

    public PromotionsApiEvent(@Nullable ArrayList<AfPromotion> promotions) {
        this.promotions = promotions;
    }

    public PromotionsApiEvent(@Nullable RetrofitError retrofitError) {
        this.retrofitError = retrofitError;
    }

    public boolean success() {
        return retrofitError == null;
    }

    @Nullable
    public ArrayList<AfPromotion> getPromotions() {
        return promotions;
    }

    @Nullable
    public RetrofitError getRetrofitError() {
        return retrofitError;
    }
}
