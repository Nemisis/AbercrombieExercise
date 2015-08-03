package com.ariets.abercrombie;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Created by aaron on 7/31/15.
 */
public class AbercrombieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());

            Timber.plant(new Timber.DebugTree());
        } else {
            // Here is where I would place the Timber tree to log to a crash reporting tool (such as Crashlytics).
        }

    }
}
