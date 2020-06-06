package com.mapxus.mapxusmapandroiddemo.examples.annotations;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Create a default marker with an InfoWindow
 */
public class DrawMarkerActivity extends AppCompatActivity implements MapxusMap.OnBuildingChangeListener {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_annotation_marker);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            DrawMarkerActivity.this.mapxusMap = mapxusMap;

            mapxusMap.addOnBuildingChangeListener(DrawMarkerActivity.this);
        });
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
    public void onSaveInstanceState(Bundle outState) {
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
    public void onBuildingChange(IndoorBuilding indoorBuilding) {
        String buildingId = getString(R.string.default_search_text_building_id);
        if (indoorBuilding.getBuildingId().equals(buildingId)) {
            mapxusMap.removeMarkers();
            mapxusMap.addMarker(new MapxusMarkerOptions().setPosition(new LatLng(22.370779, 114.111341)));
            mapxusMap.addMarker(new MapxusMarkerOptions().setPosition(new LatLng(22.371144, 114.111062)).setFloor("L1").setBuildingId(buildingId));
            mapxusMap.addMarker(new MapxusMarkerOptions().setPosition(new LatLng(22.371003, 114.111679)).setFloor("L2").setBuildingId(buildingId));
            mapxusMap.addMarker(new MapxusMarkerOptions().setPosition(new LatLng(22.370557, 114.111291)).setFloor("L2").setBuildingId(buildingId));
            mapxusMap.addMarker(new MapxusMarkerOptions().setPosition(new LatLng(22.370603, 114.111828)).setFloor("L3").setBuildingId(buildingId));
        }
    }
}