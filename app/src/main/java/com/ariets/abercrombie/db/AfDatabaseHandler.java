package com.ariets.abercrombie.db;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.ariets.abercrombie.api.AfApiKeys;
import com.ariets.abercrombie.db.AfContract.ButtonsContract;
import com.ariets.abercrombie.db.AfContract.PromotionContract;
import com.ariets.abercrombie.model.AfButton;
import com.ariets.abercrombie.model.AfPromotion;

import java.util.ArrayList;

import timber.log.Timber;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by aaron on 8/1/15.
 */
public class AfDatabaseHandler {

    private Context mContext;

    public AfDatabaseHandler(Context context) {
        mContext = context;
    }

    @WorkerThread
    public void insertPromotions(@Nullable ArrayList<AfPromotion> promotions) {
        if (promotions == null || promotions.size() == 0) {
            return;
        }
        ArrayList<AfButton> buttonsToInsert = new ArrayList<>();
        // First, save the promotions.
        for (int i = 0, size = promotions.size(); i < size; i++) {
            AfPromotion promotion = promotions.get(i);
            AfButton button = promotion.getButton();
            if (button != null) {
                buttonsToInsert.add(button);
            }
            ArrayList<AfButton> buttons = promotion.getButtonList();
            if (buttons != null) {
                buttonsToInsert.addAll(buttons);
            }

            AfPromotion fromDb = cupboard().withContext(mContext).query(PromotionContract.contentUri(), AfPromotion
                    .class).withSelection(getPromotionsSelection(), getPromotionsArr(promotion)).get();
            if (fromDb != null && fromDb.equals(promotion)) {
                Uri uri = getUriWithId(PromotionContract.contentUri(), fromDb.getId());
                Timber.d("");
                ContentValues cv = cupboard().withEntity(AfPromotion.class).toContentValues(promotion);
                cupboard().withContext(mContext).update(uri, cv);
            } else {
                Uri uri = cupboard().withContext(mContext).put(PromotionContract.contentUri(), promotion);
                if (uri != null) {
                    String idStr = uri.getLastPathSegment();
                    promotion.setId(idStr);
                }
            }
        }


        // TODO - need to figure it out.
    }

    private Uri getUriWithId(@NonNull Uri uri, @NonNull Long id) {
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @WorkerThread
    public void insertButton(@Nullable AfPromotion promotion, @Nullable AfButton button) {
        if (button != null && promotion != null) {
            button.setPromotionId(convertToString(promotion.getId()));
            cupboard().withContext(mContext).put(ButtonsContract.contentUri(), button);
        }
    }

    private String getPromotionsSelection() {
        return AfApiKeys.DESCRIPTION + "=? AND " + AfApiKeys.TITLE + "=? AND " + AfApiKeys.FOOTER + " =? AND "
                + AfApiKeys.IMAGE + " =?";
    }

    private String[] getPromotionsArr(AfPromotion promotion) {
        return new String[]{promotion.getDescription(), promotion.getTitle(), promotion.getFooter(),
                promotion.getImage()};
    }

    private String convertToString(Long l) {
        if (l != null) {
            return String.valueOf(l);
        }
        return "";
    }

}
