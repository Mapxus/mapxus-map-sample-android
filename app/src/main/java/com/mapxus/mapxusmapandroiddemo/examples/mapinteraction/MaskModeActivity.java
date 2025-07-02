package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class MaskModeActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback, PopupWindow.OnDismissListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mask_mode);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(MaskModeActivity.this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);
        ((SwitchCompat) findViewById(R.id.switch_button)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mapxusMap != null) {
                mapxusMap.isMaskNonSelectedSite(isChecked);
            }
        });
    }

    @Override
    public void onMapReady(@NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }

    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}