package com.mapxus.mapxusmapandroiddemo.examples.basics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.model.MapxusMapOptions;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Add a map view in a dynamically created layout
 */
public class MapxusMapWithoutOutdoorActivity extends AppCompatActivity {

    private MapView mapView;

    private MapxusMap mapxusMap;

    private Button hiddenBtn, showBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_init_without_outdoor);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        hiddenBtn = findViewById(R.id.hide);
        showBtn = findViewById(R.id.show);

        hiddenBtn.setOnClickListener(onClickListener);
        showBtn.setOnClickListener(onClickListener);

        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setHiddenOutdoor(true);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView, mapxusMapOptions);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            this.mapxusMap = mapxusMap;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.hide: {
                mapxusMap.setHiddenOutdoor(true);
                break;

            }
            case R.id.show: {
                mapxusMap.setHiddenOutdoor(false);
                break;
            }
        }
    };
}
