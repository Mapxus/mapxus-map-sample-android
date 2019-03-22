package com.mapxus.mapxusmapandroiddemo.examples.camera;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.impl.MapboxMapViewProvider;
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
        mapViewProvider = new MapboxMapViewProvider(AnimateMapCameraActivity.this,mapView);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        AnimateMapCameraActivity.this.mapboxMap = mapboxMap;
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngConstant.ADMIRALTY_STATION_LATLON, 19));//金钟


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
                    .target(LatLngConstant.ELEMENT_LATLON) // Sets the new camera position
                    .zoom(17) // Sets the zoom
                    .bearing(180) // Rotate the camera
                    .build(); // Creates a CameraPosition from the builder
            isElement = true;

        } else {
            position = new CameraPosition.Builder()
                    .target(LatLngConstant.ADMIRALTY_STATION_LATLON) // Sets the new camera position
                    .zoom(16) // Sets the zoom
                    .bearing(180) // Rotate the camera
                    .build(); // Creates a CameraPosition from the builder
            isElement = false;
        }


        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position));
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