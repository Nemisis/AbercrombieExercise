package com.ariets.abercrombie.views;

import android.support.annotation.NonNull;

import com.ariets.abercrombie.api.ApiError;
import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

/**
 * Represents an item that will display a list of {@link AfPromotion}'s.
 */
public interface IPromotionsView {

    void showProgress(boolean show);

    void displayPromotions(@NonNull ArrayList<AfPromotion> promotions);

    void onPromotionsError(ApiError error);

}
