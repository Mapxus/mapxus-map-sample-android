package com.mapxus.mapxusmapandroiddemo.examples.mapediting;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusPointAnnotationOptions;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;


public class DrawMarkerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, OnMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mapboxMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_annotation_marker);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnBuildingChangeListener(indoorBuilding -> {
            if (indoorBuilding != null && mapboxMap.getMarkers().isEmpty()) {
                String buildingId = getString(R.string.default_search_text_building_id);
                String[] latArray = getResources().getStringArray(R.array.default_marker_lat);
                String[] lngArray = getResources().getStringArray(R.array.default_marker_lng);
                String[] floorArray = getResources().getStringArray(R.array.default_marker_floor);
                for (int i = 0; i < latArray.length; i++) {
                    if (TextUtils.isEmpty(floorArray[i])) {
                        mapxusMap.addMapxusPointAnnotation(new MapxusPointAnnotationOptions().setPosition(new LatLng(Double.parseDouble(latArray[i]), Double.parseDouble(lngArray[i]))));
                    } else {
                        mapxusMap.addMapxusPointAnnotation(new MapxusPointAnnotationOptions().setPosition(new LatLng(Double.parseDouble(latArray[i]), Double.parseDouble(lngArray[i]))).setFloor(floorArray[i]).setBuildingId(buildingId));
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }
}