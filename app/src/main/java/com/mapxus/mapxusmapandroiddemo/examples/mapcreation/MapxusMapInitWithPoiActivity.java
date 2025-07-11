package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Add a map view in a dynamically created layout
 */
public class MapxusMapInitWithPoiActivity extends AppCompatActivity {

    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_init_with_building);
        Intent intent = getIntent();
        String poiId = intent.getStringExtra("poi_id");
        int zoomLevel = intent.getIntExtra("zoom_level", 0);
        mapboxMapView = findViewById(R.id.mapView);
        if (poiId != null && !poiId.isEmpty()) {
            MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setPoiId(poiId);
            mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView, mapxusMapOptions);
        } else {
            Toast.makeText(this, "poi id is empty", Toast.LENGTH_SHORT).show();
            mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView);
        }
        mapboxMapView.getMapAsync(mapboxMap -> mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel)));
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
