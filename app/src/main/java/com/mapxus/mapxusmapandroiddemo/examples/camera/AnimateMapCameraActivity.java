package com.mapxus.mapxusmapandroiddemo.examples.camera;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.LatLngConstant;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class AnimateMapCameraActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private boolean isElement = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_camera_animate);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(AnimateMapCameraActivity.this, mapView);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        AnimateMapCameraActivity.this.mapboxMap = mapboxMap;
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngConstant.HARBOUR_CITY_LATLON, 16));//海港城

        // Toast instructing user to tap on the map
        Toast.makeText(
                AnimateMapCameraActivity.this,
                getString(R.string.tap_on_map_instruction),
                Toast.LENGTH_LONG
        ).show();

        mapboxMap.addOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        // Toast instructing user to tap on the map
        Toast.makeText(
                AnimateMapCameraActivity.this,
                getString(R.string.tap_on_map_instruction),
                Toast.LENGTH_LONG
        ).show();

        CameraPosition position = null;
        if (isElement == false) {
            position = new CameraPosition.Builder()
                    .target(LatLngConstant.HONG_KONG_CONVENTION_AND_EXHIBITION_CENTRE) // Sets the new camera position
                    .zoom(16) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            isElement = true;

        } else {
            position = new CameraPosition.Builder()
                    .target(LatLngConstant.HARBOUR_CITY_LATLON) // Sets the new camera position
                    .zoom(16) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            isElement = false;
        }


        mapboxMap.easeCamera(CameraUpdateFactory
                .newCameraPosition(position), 2000);
        return false;
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