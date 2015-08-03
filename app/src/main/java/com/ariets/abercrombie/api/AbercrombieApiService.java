package com.ariets.abercrombie.api;

import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

import retrofit.http.GET;
import rx.Observable;

public interface AbercrombieApiService {

    @GET("/Feeds/promotions.json")
    Observable<ArrayList<AfPromotion>> getPromotions();

}
