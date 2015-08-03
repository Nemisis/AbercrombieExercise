package com.ariets.abercrombie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.ariets.abercrombie.api.AfApiKeys;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nl.qbusict.cupboard.annotation.Column;
import nl.qbusict.cupboard.annotation.Ignore;
import timber.log.Timber;

/**
 * Created by aaron on 7/31/15.
 */
public class AfPromotion implements Parcelable {

    private Long _id;
    @Ignore
    private AfButton button;
    @Ignore
    private ArrayList<AfButton> buttonList;
    @Expose
    @SerializedName(AfApiKeys.DESCRIPTION)
    @Column(AfApiKeys.DESCRIPTION)
    private String description;
    @Expose
    @SerializedName(AfApiKeys.FOOTER)
    @Column(AfApiKeys.FOOTER)
    private String footer;
    @Expose
    @SerializedName(AfApiKeys.IMAGE)
    @Column(AfApiKeys.IMAGE)
    private String image;
    @Expose
    @SerializedName(AfApiKeys.TITLE)
    @Column(AfApiKeys.TITLE)
    private String title;

    @Nullable
    public Long getId() {
        return _id;
    }

    public void setId(@Nullable String idStr) {
        if (idStr != null) {
            try {
                _id = Long.valueOf(idStr);
            } catch (NumberFormatException e) {
                Timber.e(e, "Error converting ID to a Long. ID: %s", idStr);
            }
        }
    }

    @Nullable
    public AfButton getButton() {
        return button;
    }

    @Nullable
    public ArrayList<AfButton> getButtonList() {
        return buttonList;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getFooter() {
        return footer;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfPromotion promotion = (AfPromotion) o;

        if (button != null ? !button.equals(promotion.button) : promotion.button != null) return false;
        if (buttonList != null ? !buttonList.equals(promotion.buttonList) : promotion.buttonList != null) return false;
        if (description != null ? !description.equals(promotion.description) : promotion.description != null)
            return false;
        if (footer != null ? !footer.equals(promotion.footer) : promotion.footer != null) return false;
        if (image != null ? !image.equals(promotion.image) : promotion.image != null) return false;
        return !(title != null ? !title.equals(promotion.title) : promotion.title != null);

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (button != null ? button.hashCode() : 0);
        result = 31 * result + (buttonList != null ? buttonList.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (footer != null ? footer.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    protected AfPromotion(Parcel in) {
        _id = in.readLong();
        button = in.readParcelable(AfButton.class.getClassLoader());
        buttonList = in.createTypedArrayList(AfButton.CREATOR);
        description = in.readString();
        footer = in.readString();
        image = in.readString();
        title = in.readString();
    }

    public static final Creator<AfPromotion> CREATOR = new Creator<AfPromotion>() {
        @Override
        public AfPromotion createFromParcel(Parcel in) {
            return new AfPromotion(in);
        }

        @Override
        public AfPromotion[] newArray(int size) {
            return new AfPromotion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            _id = 0L;
        }
        dest.writeLong(_id);
        dest.writeParcelable(button, flags);
        dest.writeTypedList(buttonList);
        dest.writeString(description);
        dest.writeString(footer);
        dest.writeString(image);
        dest.writeString(title);
    }

    public static class Deserializer implements JsonDeserializer<ArrayList<AfPromotion>> {
        private ArrayList<AfPromotion> promotions;

        public Deserializer() {
            promotions = new ArrayList<>();
        }

        @Override
        public ArrayList<AfPromotion> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            // Lots of if checks to prevent any Exceptions.
            if (json.isJsonObject()) {
                JsonObject outerJson = json.getAsJsonObject();
                if (outerJson.has(AfApiKeys.PROMOTIONS)) {
                    JsonElement promotionsJson = outerJson.get(AfApiKeys.PROMOTIONS);
                    if (promotionsJson.isJsonArray()) {
                        JsonArray promotionsArray = promotionsJson.getAsJsonArray();
                        for (int i = 0, size = promotionsArray.size(); i < size; i++) {
                            JsonElement promotionElement = promotionsArray.get(i);
                            if (promotionElement != null && promotionElement.isJsonObject()) {
                                JsonObject promotionJson = promotionElement.getAsJsonObject();
                                AfPromotion promotion = new AfPromotion();
                                if (promotionJson.has(AfApiKeys.DESCRIPTION)) {
                                    promotion.description = promotionJson.get(AfApiKeys.DESCRIPTION).getAsString();
                                }
                                if (promotionJson.has(AfApiKeys.IMAGE)) {
                                    promotion.image = promotionJson.get(AfApiKeys.IMAGE).getAsString();
                                }
                                if (promotionJson.has(AfApiKeys.FOOTER)) {
                                    promotion.footer = promotionJson.get(AfApiKeys.FOOTER).getAsString();
                                }
                                if (promotionJson.has(AfApiKeys.TITLE)) {
                                    promotion.title = promotionJson.get(AfApiKeys.TITLE).getAsString();
                                }

                                if (promotionJson.has(AfApiKeys.BUTTON)) {
                                    // Here, it could be either a list or a single object
                                    JsonElement buttonJson = promotionJson.get(AfApiKeys.BUTTON);
                                    if (buttonJson != null && buttonJson.isJsonObject()) {
                                        Type type = new TypeToken<AfButton>() {
                                        }.getType();
                                        promotion.button = context.deserialize(buttonJson, type);
                                    } else if (buttonJson != null && buttonJson.isJsonArray()) {
                                        Type type = new TypeToken<ArrayList<AfButton>>() {
                                        }.getType();
                                        promotion.buttonList = context.deserialize(buttonJson, type);
                                    }
                                }

                                promotions.add(promotion);
                            }
                        }
                    }
                }
            }
            return promotions;
        }
    }

    public AfPromotion() {
    }

}
