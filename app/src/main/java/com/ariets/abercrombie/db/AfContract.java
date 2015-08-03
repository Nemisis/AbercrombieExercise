package com.ariets.abercrombie.db;

import android.content.ContentResolver;
import android.net.Uri;

import com.ariets.abercrombie.model.AfButton;
import com.ariets.abercrombie.model.AfPromotion;

/**
 * A class that holds the "Contracts" for the model objects and the database.
 * <p/>
 * Created by aaron on 7/31/15.
 */
public class AfContract {

    /**
     * The contract between the {@link AfPromotion} database provider and the application.
     */
    public static class PromotionContract {
        public static final String TABLE_NAME = "promotions";

        /**
         * Gets the content URI to be used with the {@link AfPromotion} items.
         *
         * @see #getContentItemType(String, String)
         */
        public static Uri contentUri() {
            return getUri(AfContentProvider.AUTHORITY, TABLE_NAME);
        }

        /**
         * Gets the list MIME to be used with the {@link AfPromotion} items.
         *
         * @see #getContentItemType(String, String)
         */
        public static String contentListType() {
            return getContentListType(AfContentProvider.AUTHORITY, TABLE_NAME);
        }

        /**
         * Gets the item MIME to be used with the {@link AfPromotion} items.
         *
         * @see #getContentItemType(String, String)
         */
        public static String contentItemType() {
            return getContentItemType(AfContentProvider.AUTHORITY, TABLE_NAME);
        }
    }

    /**
     * The contract between the {@link AfButton} database provider and the application.
     */
    public static class ButtonsContract {
        public static final String TABLE_NAME = "buttons";

        /**
         * Gets the content URI to be used with the {@link AfButton} items.
         *
         * @see #getUri(String, String)
         */
        public static Uri contentUri() {
            return getUri(AfContentProvider.AUTHORITY, TABLE_NAME);
        }

        /**
         * Gets the list MIME type to be used with the {@link AfButton} items.
         *
         * @see #getContentListType(String, String)
         */
        public static String contentListType() {
            return getContentListType(AfContentProvider.AUTHORITY, TABLE_NAME);
        }

        /**
         * Gets the item MIME to be used with the {@link AfButton} items.
         *
         * @see #getContentItemType(String, String)
         */
        public static String contentItemType() {
            return getContentItemType(AfContentProvider.AUTHORITY, TABLE_NAME);
        }
    }

    // Helper methods for the contracts.

    /**
     * Returns the URI that should be used when interacting with the ContentProvider.
     *
     * @param authority The authority for the application. Should be {@link AfContentProvider#AUTHORITY}.
     * @param tableName The name of the table to be appended to the URI.
     */
    private static Uri getUri(String authority, String tableName) {
        return Uri.parse("content://" + authority + "/" + tableName);
    }

    /**
     * Returns the MIME type that should be used for lists of items that corresponds with the given table name within
     * the ContentProvider.
     *
     * @param authority The authority for the application. Should be {@link AfContentProvider#AUTHORITY}.
     * @param tableName The name of the table to be appended to the MIME type.
     */
    private static String getContentListType(String authority, String tableName) {
        return String.format("%s/vnd.%s.%s", ContentResolver.CURSOR_DIR_BASE_TYPE, authority, tableName);
    }

    /**
     * Returns the MIME type that should be used for a single item that corresponds with the given table name within
     * the ContentProvider.
     *
     * @param authority The authority for the application. Should be {@link AfContentProvider#AUTHORITY}.
     * @param tableName The name of the table to be appended to the MIME type.
     */
    private static String getContentItemType(String authority, String tableName) {
        return String.format("%s/vnd.%s.%s", ContentResolver.CURSOR_ITEM_BASE_TYPE, authority, tableName);
    }

}
