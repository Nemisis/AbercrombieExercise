package com.ariets.abercrombie.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ariets.abercrombie.db.AfContract.ButtonsContract;
import com.ariets.abercrombie.db.AfContract.PromotionContract;
import com.ariets.abercrombie.model.AfButton;
import com.ariets.abercrombie.model.AfPromotion;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by aaron on 7/31/15.
 */
public class AfContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.ariets.abercrombie.provider";

    private AfSqLiteOpenHelper mDbHelper;

    private static final int LIST_PROMOTION = 10;
    private static final int ITEM_PROMOTION = 11;
    private static final int LIST_BUTTON = 20;
    private static final int ITEM_BUTTON = 21;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PromotionContract.TABLE_NAME, LIST_PROMOTION);
        sUriMatcher.addURI(AUTHORITY, PromotionContract.TABLE_NAME + "/#", ITEM_PROMOTION);
        sUriMatcher.addURI(AUTHORITY, ButtonsContract.TABLE_NAME, LIST_BUTTON);
        sUriMatcher.addURI(AUTHORITY, ButtonsContract.TABLE_NAME + "/#", ITEM_BUTTON);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new AfSqLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case LIST_PROMOTION:
                cursor = cupboard().withDatabase(db).query(AfPromotion.class).withProjection(projection).
                        withSelection(selection, selectionArgs).orderBy(sortOrder).getCursor();
                break;
            case ITEM_PROMOTION:
                cursor = cupboard().withDatabase(db).query(AfPromotion.class)
                        .byId(ContentUris.parseId(uri)).getCursor();
                break;
            case LIST_BUTTON:
                cursor = cupboard().withDatabase(db).query(AfButton.class).withProjection(projection).
                        withSelection(selection, selectionArgs).orderBy(sortOrder).getCursor();
                break;
            case ITEM_BUTTON:
                cursor = cupboard().withDatabase(db).query(AfButton.class)
                        .byId(ContentUris.parseId(uri)).getCursor();
                break;
        }

        if (cursor == null) {
            cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }
        if (cursor != null) {
            cursor.moveToFirst();
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        checkSupportedUri(uri);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long insertedId = -1;

        switch (sUriMatcher.match(uri)) {
            case LIST_PROMOTION:
            case ITEM_PROMOTION:
                insertedId = cupboard().withDatabase(db).put(AfPromotion.class, values);
                break;
            case LIST_BUTTON:
            case ITEM_BUTTON:
                insertedId = cupboard().withDatabase(db).put(AfButton.class, values);
                break;
        }

        if (insertedId > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, insertedId);
            getContext().getContentResolver().notifyChange(uri, null);
            return itemUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Check that the given URI is valid.
     */
    private void checkSupportedUri(@Nullable Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("The given URI was null. To use " + AfContentProvider.class
                    .getSimpleName() + ", you must pass a URI.");
        }
        int match = sUriMatcher.match(uri);
        // If it doesn't match one of the defined uris above, then it is not valid!
        if (match != LIST_PROMOTION && match != ITEM_PROMOTION && match != LIST_BUTTON && match != ITEM_BUTTON) {
            throw new IllegalArgumentException("You must pass in a defined Uri (Check " +
                    AfContentProvider.class.getSimpleName() + " for the defined list. Your uri: " + uri.toString());
        }
    }
}
