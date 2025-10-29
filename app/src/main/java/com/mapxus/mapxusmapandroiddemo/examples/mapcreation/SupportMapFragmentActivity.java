package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMapOptions;
import org.maplibre.android.maps.MapView;
import com.mapxus.map.mapxusmap.impl.SupportMapxusMapFragment;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Include a map fragment within your app using Android support library.
 */
public class SupportMapFragmentActivity extends AppCompatActivity {

    SupportMapxusMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_support_map_frag);
        if (savedInstanceState == null) {
            // Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MapLibreMapOptions options = MapLibreMapOptions.createFromAttributes(this);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(getString(R.string.default_lat)), Double.parseDouble(getString(R.string.default_lon))))
                    .zoom(Integer.parseInt(getString(R.string.default_zoom_level_value)))
                    .build());

            MapView mapView = new MapView(SupportMapFragmentActivity.this, options);
            // Create map fragment
            mapFragment = SupportMapxusMapFragment.newInstance(mapView);
            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapxus.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapxusMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapxus.map");
        }
    }
}
