package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapView;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class IndoorSceneInAndOutListenerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private TextView tvInOrOut;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_change);

        mapView = findViewById(R.id.mapView);
        tvInOrOut = findViewById(R.id.tv_in_or_out);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapLibreMapViewProvider(IndoorSceneInAndOutListenerActivity.this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floor) -> {
            if (floor != null) {
                tvInOrOut.setText("Indoor now");
            } else {
                tvInOrOut.setText("Outdoor now");
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
        mapViewProvider.onDestroy();
    }
}