package com.ariets.abercrombie.api;

import android.content.Context;

import com.ariets.abercrombie.BuildConfig;
import com.ariets.abercrombie.model.AfPromotion;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;

/**
 * The "API Client" to interact with the API.
 * <p/>
 * Created by aaron on 7/31/15.
 */
public class PromotionsApiClient {

    private static final String API_ENDPOINT = "http://www.abercrombie.com/anf/nativeapp";

    private PromotionsApiService mApiService;

    public PromotionsApiClient(Context context) {

        RestAdapter.Builder restBuilder = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .setConverter(new AfGsonConverter(context, AfGsonConverter.getPromotionDeserializer(),
                        AfGsonConverter.getType()));

        // Used to track Network requests
        if (BuildConfig.DEBUG) {
            OkHttpClient client = new OkHttpClient();
            client.networkInterceptors().add(new StethoInterceptor());
            restBuilder.setClient(new OkClient(client));
        }

        mApiService = restBuilder.build().create(PromotionsApiService.class);
    }

    public Observable<ArrayList<AfPromotion>> getPromotions() {
        return mApiService.getPromotions();
    }

}
