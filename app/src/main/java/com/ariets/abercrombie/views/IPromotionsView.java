package com.ariets.abercrombie.views;

import android.support.annotation.NonNull;

import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

public interface IPromotionsView {

    void showProgress(boolean show);

    void displayPromotions(@NonNull ArrayList<AfPromotion> promotions);

    void onPromotionsError();

}
