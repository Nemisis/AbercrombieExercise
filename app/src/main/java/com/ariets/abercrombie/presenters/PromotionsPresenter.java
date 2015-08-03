package com.ariets.abercrombie.presenters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.ariets.abercrombie.PreferenceUtils;
import com.ariets.abercrombie.api.AbercrombieApiClient;
import com.ariets.abercrombie.api.AfGsonConverter;
import com.ariets.abercrombie.db.AfDatabaseHandler;
import com.ariets.abercrombie.model.AfPromotion;
import com.ariets.abercrombie.views.IPromotionsView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by aaron on 8/1/15.
 */
public class PromotionsPresenter implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PROMOTIONS_LOADER = 1414;

    private IPromotionsView promotionsView;
    private final AbercrombieApiClient apiClient;
    private AfDatabaseHandler databaseHandler;

    public PromotionsPresenter(IPromotionsView view, Context context) {
        promotionsView = view;
        apiClient = new AbercrombieApiClient(context.getApplicationContext());
        databaseHandler = new AfDatabaseHandler(context.getApplicationContext());
    }

    public void onResume() {
    }

    public void loadPromotions(Context context) {
        // First, load from the cache, if present:
        loadFromCache(context);

        // Now, load from the server.
        fetchFromServer();
    }

    private void loadFromCache(Context context) {
        Observable.just(PreferenceUtils.getJsonData(context)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null && s.length() > 0;
                    }
                })
                .map(new Func1<String, ArrayList<AfPromotion>>() {
                    @Override
                    public ArrayList<AfPromotion> call(String s) {
                        Type type = AfGsonConverter.getType();
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                .registerTypeAdapter(type, AfGsonConverter.getPromotionDeserializer()).create();
                        return gson.fromJson(s, type);
                    }
                })
                .subscribe(new Action1<ArrayList<AfPromotion>>() {
                    @Override
                    public void call(ArrayList<AfPromotion> afPromotions) {
                        if (afPromotions != null && promotionsView != null) {
                            promotionsView.displayPromotions(afPromotions);
                        }
                    }
                });
    }

    private void fetchFromServer() {
        showProgress(true);
        apiClient.getPromotions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<ArrayList<AfPromotion>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<AfPromotion> afPromotions) {
                        // Only let successful calls through.
                        return afPromotions != null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<AfPromotion>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof RetrofitError) {

                        }
                        showProgress(false);
                        Timber.d("");
                    }

                    @Override
                    public void onNext(ArrayList<AfPromotion> afPromotions) {
                        // Because the loader handles it, all that is needed here is to no longer display the progress:
                        showProgress(false);
                        promotionsView.displayPromotions(afPromotions);
                    }
                });
    }

    private void showProgress(boolean progress) {
        if (promotionsView != null) {
            promotionsView.showProgress(progress);
        }
    }

    public void onPause() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
