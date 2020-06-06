package com.mapxus.mapxusmapandroiddemo.examples.basics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

public class MyMapView extends MapView {
    public MyMapView(@NonNull Context context) {
        super(context);
    }

    public MyMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setContentDescription("");
        setFocusFalse();
    }

    public MyMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentDescription("");
        setFocusFalse();
    }

    public MyMapView(@NonNull Context context, @Nullable MapboxMapOptions options) {
        super(context, options);
        setContentDescription("");
        setFocusFalse();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
            setContentDescription("");
        }
    }

    private void setFocusFalse() {
        setClickable(false);
        setLongClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
        requestDisallowInterceptTouchEvent(false);
    }
}
