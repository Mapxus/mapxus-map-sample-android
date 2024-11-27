package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Add a map view in a dynamically created layout
 */
public class MapxusMapInitWithBuildingActivity extends AppCompatActivity {

    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;
    private int paddingTop, paddingBottom, paddingLeft, paddingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_init_with_building);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        int selectedTab = intent.getIntExtra("selectedTab", 0);
        paddingTop = intent.getIntExtra("top", 0);
        paddingBottom = intent.getIntExtra("bottom", 0);
        paddingLeft = intent.getIntExtra("left", 0);
        paddingRight = intent.getIntExtra("right", 0);

        mapboxMapView = findViewById(R.id.mapView);

        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions();
        switch (selectedTab) {
            case 0:
                mapxusMapOptions.setFloorId(id);
                break;
            case 1:
                mapxusMapOptions.setBuildingId(id);
                break;
            default:
                mapxusMapOptions.setVenueId(id);
                break;
        }

        mapxusMapOptions.setZoomInsets(Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom));

        mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView, mapxusMapOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapboxMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapboxMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapboxMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapboxMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapboxMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapboxMapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapboxMapView.onSaveInstanceState(outState);
    }
}
