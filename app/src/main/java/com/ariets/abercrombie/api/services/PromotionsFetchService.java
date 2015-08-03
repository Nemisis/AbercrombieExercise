package com.ariets.abercrombie.api.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by aaron on 8/1/15.
 */
public class PromotionsFetchService extends IntentService {
    public static final String TAG = PromotionsFetchService.class.getSimpleName();

    public PromotionsFetchService() {
        super(TAG);
    }

    public static void startService(Context context) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
