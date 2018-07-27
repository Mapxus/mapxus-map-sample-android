package com.mapxus.mapxusmapandroiddemo.examples.listener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class CameraListenerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private TextView mapMoveTv;
    private TextView mapClickTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_change);

        mapView = findViewById(R.id.mapView);
        mapMoveTv = findViewById(R.id.map_move_tv);
        mapClickTv = findViewById(R.id.map_click_tv);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        // Toast instructing user to tap on the map
        Toast.makeText(
                CameraListenerActivity.this,
                getString(R.string.click_or_move_the_map),
                Toast.LENGTH_LONG
        ).show();

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onMapClick(LatLng latLng) {

                String message = String.format(getString(R.string.click_point_on_the_map), latLng.getLatitude(), latLng.getLongitude());

                mapClickTv.setText(message);

            }
        });

        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mapboxMap.getCameraPosition().target;

                String message = String.format(getString(R.string.camera_move_on), latLng.getLatitude(), latLng.getLongitude());
                mapMoveTv.setText(message);
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