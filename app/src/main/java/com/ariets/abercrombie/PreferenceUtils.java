package com.ariets.abercrombie;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by aaron on 8/3/15.
 */
public class PreferenceUtils {

    private static final String PREFERENCE_NAME = "abercrombie_api.xml";
    private static final String KEY_DATA = "data";

    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void setJsonData(@NonNull Context context, @Nullable String jsonData) {
        getSharedPreferences(context).edit().putString(KEY_DATA, jsonData).apply();
    }

    public static String getJsonData(@NonNull Context context) {
        return getSharedPreferences(context).getString(KEY_DATA, "");
    }
}
