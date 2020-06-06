package com.mapxus.mapxusmapandroiddemo.examples.basics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_init_with_building);
        mapboxMapView = (MapView) findViewById(R.id.mapView);
        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setBuildingId(getString(R.string.default_search_text_building_id)).setFloor("L3");
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
