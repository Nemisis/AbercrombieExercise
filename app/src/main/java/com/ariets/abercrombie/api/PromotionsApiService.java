package com.ariets.abercrombie.api;

import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

import retrofit.http.GET;
import rx.Observable;

public interface PromotionsApiService {

    /**
     * Retrieves a list of Promotions from the API.
     */
    @GET("/Feeds/promotions.json")
    Observable<ArrayList<AfPromotion>> getPromotions();

}
