package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;


public class IndoorSceneSwitchingListenerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private TextView buildingNameTv, floorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_building_and_floor_change);

        mapView = findViewById(R.id.mapView);
        buildingNameTv = findViewById(R.id.building_name_tv);
        floorTv = findViewById(R.id.floor_tv);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {

        mapxusMap.addOnBuildingChangeListener(indoorBuildingInfo -> {
            if (indoorBuildingInfo != null) {
                buildingNameTv.setText(indoorBuildingInfo.getBuildingName());
            }
        });
        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floorInfo) -> {
            if (indoorBuilding != null && floorInfo != null) {
                floorTv.setText(floorInfo.getCode());
            }
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
}