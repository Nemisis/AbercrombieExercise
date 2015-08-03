package com.ariets.abercrombie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ariets.abercrombie.api.AfApiKeys;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model object that represents a button from the JSON.
 * <p/>
 * Created by aaron on 7/31/15.
 */
public class AfButton implements Parcelable {

    private Long _id;
    @Expose
    @SerializedName(AfApiKeys.TARGET)
    private String target;
    @Expose
    @SerializedName(AfApiKeys.TITLE)
    private String title;
    private String promotionId;

    protected AfButton(Parcel in) {
        _id = in.readLong();
        target = in.readString();
        title = in.readString();
        promotionId = in.readString();
    }

    public static final Creator<AfButton> CREATOR = new Creator<AfButton>() {
        @Override
        public AfButton createFromParcel(Parcel in) {
            return new AfButton(in);
        }

        @Override
        public AfButton[] newArray(int size) {
            return new AfButton[size];
        }
    };

    @Nullable
    public Long getId() {
        return _id;
    }

    @Nullable
    public String getTarget() {
        return target;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setPromotionId(@NonNull String promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AfButton afButton = (AfButton) o;

        if (_id != null ? !_id.equals(afButton._id) : afButton._id != null) return false;
        if (target != null ? !target.equals(afButton.target) : afButton.target != null) return false;
        if (title != null ? !title.equals(afButton.title) : afButton.title != null) return false;
        return !(promotionId != null ? !promotionId.equals(afButton.promotionId) : afButton.promotionId != null);

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (promotionId != null ? promotionId.hashCode() : 0);
        return result;
    }

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
        dest.writeString(target);
        dest.writeString(title);
        dest.writeString(promotionId);
    }
}
