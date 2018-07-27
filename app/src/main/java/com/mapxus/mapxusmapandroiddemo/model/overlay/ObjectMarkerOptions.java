package com.mapxus.mapxusmapandroiddemo.model.overlay;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class ObjectMarkerOptions extends BaseMarkerOptions<ObjectMarker, ObjectMarkerOptions> {

    public ObjectMarkerOptions() {
    }

    private Object object;

    public ObjectMarkerOptions object(Object object) {
        this.object = object;
        return getThis();
    }

    @Override
    public ObjectMarkerOptions getThis() {
        return this;
    }

    @Override
    public ObjectMarker getMarker() {
        return new ObjectMarker(this, object);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.object);
        dest.writeParcelable(this.position, flags);
        dest.writeString(this.snippet);
        dest.writeString(this.title);
        dest.writeString(this.icon.getId());
        dest.writeParcelable(this.icon.getBitmap(), flags);
    }

    protected ObjectMarkerOptions(Parcel in) {
        this.object = in.readValue(Object.class.getClassLoader());
        this.position = in.readParcelable(LatLng.class.getClassLoader());
        this.snippet = in.readString();
        this.title = in.readString();
        String iconId = in.readString();
        Bitmap iconBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.icon = IconFactory.recreate(iconId, iconBitmap);
        icon(icon);
    }

    public static final Creator<ObjectMarkerOptions> CREATOR = new Creator<ObjectMarkerOptions>() {
        @Override
        public ObjectMarkerOptions createFromParcel(Parcel source) {
            return new ObjectMarkerOptions(source);
        }

        @Override
        public ObjectMarkerOptions[] newArray(int size) {
            return new ObjectMarkerOptions[size];
        }
    };
}
