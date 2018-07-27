package com.mapxus.mapxusmapandroiddemo.examples.listener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.model.IndoorBuilding;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class BuildingAndFloorChangeListenerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private TextView buildingNameTv;
    private TextView floorTv;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_building_and_floor_change);

        mapView = findViewById(R.id.mapView);
        buildingNameTv = findViewById(R.id.building_name_tv);
        floorTv = findViewById(R.id.floor_tv);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap beeMap) {

        // Toast instructing user to tap on the map
        Toast.makeText(BuildingAndFloorChangeListenerActivity.this,
                getString(R.string.change_building_and_floor),
                Toast.LENGTH_LONG
        ).show();

//        beeMap.setOnMapClickListener(this);
        beeMap.addOnBuildingChangeListener(new MapxusMap.OnBuildingChangeListener() {
            @Override
            public void onBuildingChange(IndoorBuilding indoorBuildingInfo) {
                if (indoorBuildingInfo != null) {
                    buildingNameTv.setText(getString(R.string.building_name_tips) + indoorBuildingInfo.getBuildingName());
                } else {
                    buildingNameTv.setText(getString(R.string.building_name_tips));
                }

            }
        });
        beeMap.addOnFloorChangeListener(new MapxusMap.OnFloorChangeListener() {
            @Override
            public void onFloorChange(IndoorBuilding indoorBuilding, String floor) {
                floorTv.setText(getString(R.string.floor_tips) + floor);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}