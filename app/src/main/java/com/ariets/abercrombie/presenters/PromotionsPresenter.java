package com.ariets.abercrombie.presenters;

import android.content.Context;

import com.ariets.abercrombie.PreferenceUtils;
import com.ariets.abercrombie.api.AfGsonConverter;
import com.ariets.abercrombie.api.ApiError;
import com.ariets.abercrombie.api.PromotionsApiClient;
import com.ariets.abercrombie.model.AfPromotion;
import com.ariets.abercrombie.utils.NetworkUtils;
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
 * A "presenter" used to retrieve the Data. This will allow the UI to be fairly "dumb", only caring about whether or
 * not the data has been fetched.
 * <p/>
 * Created by aaron on 8/1/15.
 */
public class PromotionsPresenter {

    private IPromotionsView promotionsView;
    private final PromotionsApiClient apiClient;

    public PromotionsPresenter(IPromotionsView view, Context context) {
        promotionsView = view;
        apiClient = new PromotionsApiClient(context.getApplicationContext());
    }

    /**
     * Loads a list of promotions. Will return the promotions in the
     * {@link IPromotionsView#displayPromotions(ArrayList)} callback.
     *
     * @param context
     */
    public void loadPromotions(Context context) {
        // First, load from the cache, if present:
        loadFromCache(context);

        // Now, load from the server.
        if (NetworkUtils.isConnected(context)) {
            fetchFromServer();
        } else if (promotionsView != null) {
            promotionsView.onPromotionsError(ApiError.noNetworkConnectivityError());
        }
    }

    /**
     * Loads the promotions from the internal SharedPreferences cache.
     */
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

    /**
     * Loads the promotions from the API.
     */
    private void fetchFromServer() {
        showProgress(true);
        apiClient.getPromotions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ArrayList<AfPromotion>, ArrayList<AfPromotion>>() {
                    @Override
                    public ArrayList<AfPromotion> call(ArrayList<AfPromotion> afPromotions) {
                        return afPromotions == null ? new ArrayList<AfPromotion>() : afPromotions;
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

}
