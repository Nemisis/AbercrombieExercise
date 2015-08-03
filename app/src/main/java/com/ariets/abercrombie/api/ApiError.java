package com.ariets.abercrombie.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An object representation of an error when interacting with the API.
 * Created by aaron on 8/3/15.
 */
public class ApiError {

    /**
     * An error type that signifies that there is no network connectivity.
     */
    public static final int ERROR_TYPE_NO_NETWORK = 29581951;
    /**
     * An error type that signifies that an error occurred with the API call.
     */
    public static final int ERROR_TYPE_API_ERROR = 21498124;

    @IntDef({ERROR_TYPE_NO_NETWORK, ERROR_TYPE_API_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorType {
    }

    @ErrorType
    private int errorType;

    public static ApiError noNetworkConnectivityError() {
        return new ApiError(ERROR_TYPE_NO_NETWORK);
    }

    public static ApiError errorFromApi() {
        return new ApiError(ERROR_TYPE_API_ERROR);
    }

    private ApiError(@ErrorType int errorType) {
        this.errorType = errorType;
    }

    @ErrorType
    public int getErrorType() {
        return errorType;
    }
}
