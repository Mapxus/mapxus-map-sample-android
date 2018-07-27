package com.mapxus.mapxusmapandroiddemo.model.overlay;

import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;

public class ObjectMarker extends Marker {
    private Object object;

    public ObjectMarker(BaseMarkerOptions baseMarkerOptions, Object object) {
        super(baseMarkerOptions);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}
